# Task 5 — Full-Stack Application

A Vue 3 + TypeScript frontend and a Java Spring Boot backend visualizing the results of Tasks 1-3 (age classification, pi calculation, unpaid invoices) against the Oracle Autonomous Database.

## Architecture

```
Vue 3 SPA (TypeScript)  →  Spring Boot REST API (Java)  →  Oracle Autonomous DB (SB_CORE package)
```

Each of the three domains (Age, Pi, Invoices) follows the same layered shape on the backend:

```
Controller → Service (interface + impl) → Repository (interface + impl, SimpleJdbcCall) → SB_CORE package function
```

Interface + implementation per service/repository is the deliberate SOLID choice here — controllers depend on interfaces (`AgeService`, `PiService`, `InvoiceService`), not concrete classes, which is what makes the unit tests below possible without a live DB connection.

## Project Structure
```
sb_app/
├── db/
│   └── sb_core.pck          -- includes GET_UNPAID_INVOICES (Task 5 addition), reused from Tasks 1 & 2
├── backend/                 -- Spring Boot + Gradle
│   └── src/main/java/com/sbhome/backend/
│       ├── controller/      AgeController, PiController, InvoiceController
│       ├── service/         *Service (interfaces) + *ServiceImpl
│       ├── repository/      *Repository (interfaces) + *RepositoryImpl (SimpleJdbcCall)
│       ├── dto/              AgeTextResponse, PiValueResponse, InvoiceResponse
│       ├── exception/        BusinessValidationException, GlobalExceptionHandler
│       ├── config/           CorsConfig
│       └── util/             OracleErrorUtils
└── frontend/                 -- Vue 3 + TypeScript + Vite
    └── src/
        ├── views/            AgeView.vue, PiView.vue, InvoiceView.vue
        ├── services/         api.ts
        └── router/           index.ts
```

## Prerequisites
- JDK 17+ (or newer — project currently builds against JDK 25)
- Node.js + npm
- Oracle Autonomous Database wallet (downloaded from OCI Console), extracted to a local folder outside the repository
- Google Chrome installed (required for the Selenium UI test suite)

## Setup

### Database
1. Run `db/sb_core.pck` against your Autonomous DB (requires the Task 1-3 tables and seed data to already exist).
2. Download the ATP wallet, extract it locally (e.g. `C:/oracle_wallet`), and edit `sqlnet.ora` inside it — replace the `?/network/admin` placeholder with the absolute path to the wallet folder.

### Backend
`backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:oracle:thin:@<your_tns_alias>?TNS_ADMIN=C:/oracle_wallet
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```
Set `DB_USERNAME`/`DB_PASSWORD` as environment variables — never commit real credentials or the wallet folder (see `.gitignore`).

Run:
```cmd
cd backend
gradlew.bat bootRun
```
Serves on `http://localhost:8080`.

### Frontend
```cmd
cd frontend
npm install
npm run dev
```
Serves on `http://localhost:5173`. CORS is configured backend-side to allow this origin specifically (`config/CorsConfig.java`).

## API Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/api/age-text?age={n}` | Returns the age-group text for a given age |
| GET | `/api/pi?precision={n}` | Returns π computed to the given precision |
| GET | `/api/invoices/unpaid` | Returns all invoices not fully paid |

## Key Design Decisions

- **`BigDecimal`, not `double`, for the pi value** — a Java `double` can only hold ~15-17 significant digits, which would silently discard most of the precision Task 2 was specifically built to preserve (up to Oracle `NUMBER`'s 38-digit ceiling).
- **Pi value serialized as a JSON string** (`@JsonFormat(shape = JsonFormat.Shape.STRING)`), not a raw JSON number — JavaScript's `JSON.parse` converts any JSON number to a native `number` (IEEE754 double), which would undo the `BigDecimal` precision the moment the frontend parsed the response. Serializing as a string sidesteps numeric parsing entirely.
- **Business-rule errors from PL/SQL surface as clean 400s.** `raise_application_error(-20000, ...)` calls in `sb_core.pck` (negative age, negative precision) are caught at the repository layer, stripped of Oracle's internal diagnostic text (schema name, line numbers, stack trace), and returned to the frontend as a plain `{"error": "..."}` message — the same pattern is used for malformed/missing query parameters (`GlobalExceptionHandler`).
- **Unit tests mock the repository layer**, not the database — `AgeServiceImplTest`, `PiServiceImplTest`, `InvoiceServiceImplTest` all run without a live Oracle connection, so `gradlew test` is fast and portable (doesn't require DB access to pass, e.g. in CI).

## Testing
- **Backend unit tests:** JUnit 5 + Mockito, one per service (`AgeServiceImplTest`, `PiServiceImplTest`, `InvoiceServiceImplTest`), repository layer mocked out — no live DB connection needed. Run via:
  ```cmd
  gradlew.bat test
  ```
- **UI tests (Selenium):** isolated in a separate `uiTest` Gradle source set, since they require both the backend and frontend actually running plus a real Chrome install — kept out of the fast `test` task so normal unit test runs stay quick and DB/browser-independent. Run via:
  ```cmd
  :: terminal 1
  gradlew.bat bootRun
  :: terminal 2 (from frontend/)
  npm run dev
  :: terminal 3
  gradlew.bat uiTest
  ```
  Covers all three views: Age (valid submission, negative-age error), Pi (valid precision, negative-precision error), Invoices (unpaid invoices table renders rows).
- **Frontend:** Vitest scaffolded via `create-vue`; component-level tests not yet written.
- **Not yet implemented:** backend integration tests against a real DB.

## Bonus Points Status
| Item | Status |
|---|---|
| Vue.js framework | ✅ Done |
| SOLID principles | ✅ Interface-based services/repositories throughout |
| UI tests (Selenium) | ✅ Done — Age, Pi, and Invoices views all covered |
