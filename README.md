# Task 1 — Age to Text Classifier

## Overview
Given a person's age (a number), returns a text description of their age group. The mapping is stored in a database table rather than hardcoded, so the rules can be updated without changing PL/SQL code.

## Environment
- Oracle Cloud 19c

## Database Objects

### Table: `SB_AGE_TO_TEXT`
Holds the age-range → text rules used by the classifier.

| Column        | Type          | Description                          |
|---------------|---------------|---------------------------------------|
| ID            | NUMBER        | PK |
| AGE_FROM      | NUMBER        | Age number lower value |
| AGE_TO        | NUMBER        | Age number higher value. Can be null |
| AGE_TEXT      | VARCHAR2(60)  | Age to text, corresponding to age interval  |
| CREATED_BY    | VARCHAR2(30)  | Who created |
| CREATION_DATE | DATE          | When created |
| UPDATED_BY    | VARCHAR2(30)  | Who updated |
| UPDATE_DATE   | DATE          | When updated |

> Adjust the column names/types above if your actual DDL differs — this is a template based on the seed data described below.

### Seed data: `sb_insert_seed_data`
Populates `SB_AGE_TO_TEXT` with the following rules from the spec:

| Age range        | Text                  |
|-------------------|------------------------|
| <= 7              | You are infant         |
| 7 < age <= 18     | You are schoolchild    |
| 18 < age < 40     | You are adult          |
| 40 <= age < 55    | You are in middle-age  |
| age >= 55         | You are aged           |

### Package: `SB_CORE`
Business logic package for Task 1 (and reusable home for future task logic).

**Function: `GET_AGE_TEXT(pn_age NUMBER) RETURN VARCHAR2`**
- Input: `pn_age` — a person's age
- Output: the matching text from `SB_AGE_TO_TEXT`
- Looks up the row where `pn_age` falls between `AGE_FROM` and `AGE_TO`
- Raises/handles invalid input (non-existent value or negative age)

## Usage
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

## Testing
Tested manually against boundary values (7, 8, 18, 19, 39, 40, 54, 55) and edge cases (NULL, negative age) using an anonymous PL/SQL block with `DBMS_OUTPUT.PUT_LINE`.

## Files
- `sb_age_to_text.sql` — table DDL
- `sb_insert_seed_data.sql` — seed data script
- `sb_core.pck` — package spec and body

