# SB Home — Interview Homework

Solutions for the PL/SQL / Oracle Forms modernization / full-stack homework assignment.

## Environment
- Oracle Cloud 19c (Autonomous Database)
- Schema prefix: `SB_` for all objects (tables, packages)

## Repository Structure
```
sb_core.pck            -- main package: age classifier (Task 1) + pi calculator (Task 2)
sb_age_to_text.sql      -- Task 1 table DDL
sb_insert_seed_data.sql -- Task 1 seed data
create_table.sql        -- Task 2 table DDL (SB_PI_CALCULATIONS)
README.md               -- this file
```

## AI Assistance Disclosure
AI assistance (Claude, Anthropic) was used throughout this project's development, covering: design and architecture discussions, code review, debugging, testing support, and documentation generation. All PL/SQL code, edge-case handling, and final design decisions were written and approved by the author; AI assistance was used for discussion, review, and drafting support rather than autonomous code generation.

---

## Task 1 — Age to Text Classifier

### Overview
Given a person's age (a number), returns a text description of their age group. The mapping is stored in a database table rather than hardcoded, so the rules can be updated without changing PL/SQL code.

### Database Objects

**Table: `SB_AGE_TO_TEXT`**

| Column        | Type          | Description                          |
|---------------|---------------|---------------------------------------|
| ID            | NUMBER        | PK |
| AGE_FROM      | NUMBER        | Age number lower value |
| AGE_TO        | NUMBER        | Age number higher value. Can be null |
| AGE_TEXT      | VARCHAR2(60)  | Age to text, corresponding to age interval |
| CREATED_BY    | VARCHAR2(30)  | Who created |
| CREATION_DATE | DATE          | When created |
| UPDATED_BY    | VARCHAR2(30)  | Who updated |
| UPDATE_DATE   | DATE          | When updated |

**Seed data (`sb_insert_seed_data`)**

| Age range        | Text                  |
|-------------------|------------------------|
| <= 7              | You are infant         |
| 7 < age <= 18     | You are schoolchild    |
| 18 < age < 40     | You are adult          |
| 40 <= age < 55    | You are in middle-age  |
| age >= 55         | You are aged           |

**Package function: `SB_CORE.GET_AGE_TEXT(pn_age NUMBER) RETURN VARCHAR2`**
- Looks up the row where `pn_age` falls between `AGE_FROM` and `AGE_TO`
- Raises a clear error on negative age or age not covered by any rule (rather than failing silently)

### Usage
```sql
SET SERVEROUTPUT ON;
DECLARE
  lv_result VARCHAR2(100);
BEGIN
  lv_result := sb_core.get_age_text(pn_age => 25);
  DBMS_OUTPUT.PUT_LINE(lv_result); -- You are adult
END;
/
```
Or directly in SQL:
```sql
SELECT sb_core.get_age_text(pn_age => 25) FROM dual;
```

### Testing
Tested manually against boundary values (7, 8, 18, 19, 39, 40, 54, 55) and edge cases (NULL, negative age) using an anonymous PL/SQL block with `DBMS_OUTPUT.PUT_LINE`.

---

## Task 2 — Pi Calculation (Bailey-Borwein-Plouffe formula)

### Overview
Computes an approximation of π to a given precision using the Bailey-Borwein-Plouffe (BBP) series:

```
pi = SUM(k=0 to n) [ 1/16^k * ( 4/(8k+1) - 2/(8k+4) - 1/(8k+5) - 1/(8k+6) ) ]
```

Each term of the series is persisted to a table as an intermediate result, and the table is cleared at the start of every run.

### Database Objects

**Table: `SB_PI_CALCULATIONS`**

| Column         | Type          | Description                                    |
|----------------|---------------|-------------------------------------------------|
| PI_ITERATION   | INTEGER       | Loop index / term number of the BBP series      |
| PI_VALUE       | NUMBER        | The individual term's value at that iteration   |
| CREATED_BY     | VARCHAR2(30)  | Who created                                     |
| CREATION_DATE  | DATE          | When created                                    |

**Package procedure: `SB_CORE.RETRIEVE_PI_VALUE(pii_precision IN INTEGER, pon_pi_value OUT NUMBER)`**
- Does the actual work: validates precision, clears the previous run's rows, computes and persists each BBP term, accumulates the running total
- A procedure rather than a function, since it performs DML — not callable from SQL regardless, because the task requires persisting intermediate results

**Package function: `SB_CORE.GET_PI_VALUE(pi_precision INTEGER) RETURN NUMBER`**
- Satisfies the task's required function signature
- Delegates all persistence to `RETRIEVE_PI_VALUE` — functions shouldn't own DML directly (Command–Query Separation); still not SQL-callable itself, since its call chain includes DML

### Known Limitations
- **Precision is capped at 38.** Oracle's `NUMBER` type supports a maximum of 38 significant digits total. Requesting a higher precision is silently clamped to 38 rather than erroring, since the BBP series' fast convergence (~1.2 correct decimal digits per iteration) means anything beyond that produces terms too small to affect the accumulated `NUMBER` value — verified empirically by observing the running total stabilize around iteration 30-32.
- Precision below 0 raises an explicit error rather than returning a misleading result.

### Design Notes
- `DELETE FROM sb_pi_calculations` is hinted `NO_PARALLEL` and the whole delete+insert sequence commits once, at the end, as a single transaction. Without the hint, Oracle Autonomous DB's auto-parallelism policy can silently run the `DELETE` in parallel, which then conflicts with the subsequent same-transaction `INSERT`s (`ORA-12838`).

### Usage
```sql
SET SERVEROUTPUT ON;
DECLARE
  ln_pi NUMBER;
BEGIN
  ln_pi := sb_core.get_pi_value(20);
  DBMS_OUTPUT.PUT_LINE(TO_CHAR(ln_pi, 'FM9.999999999999999999999999999999999999'));
END;
/
```
Or via the procedure directly:
```sql
DECLARE
  ln_pi NUMBER;
BEGIN
  sb_core.retrieve_pi_value(pii_precision => 20, pon_pi_value => ln_pi);
  DBMS_OUTPUT.PUT_LINE(TO_CHAR(ln_pi, 'FM9.999999999999999999999999999999999999'));
END;
/
```

### Testing
Tested with precision values 0, 15, 20, 35, and 38+ (to confirm clamping); confirmed intermediate rows are written and cleared correctly per run; confirmed negative precision raises an error instead of returning a misleading result.
