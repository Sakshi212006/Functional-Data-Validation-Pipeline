# Functional Data Validation Pipeline

**Course:** Functional Programming in Practice  
**Assignment:** Group Level Mini-Project  
**Semester:** 5th Semester  
**Language:** Scala 2.13

## Project overview
This project validates sample person records before they are accepted. It checks ID, name, age and email. Correct records are marked VALID; incorrect records show error messages.

## Rules
1. ID must be present and a whole number.
2. Name must not be empty.
3. Age must be a whole number from 1 to 120.
4. Email must follow a basic email format.

## Functional programming concepts
- **Immutability:** case classes hold immutable fields; input records are not edited.
- **ADTs:** `ValidationError` has cases for missing fields, invalid numbers, out-of-range values and invalid email.
- **Pattern matching:** matches `Right` and `Left` to display results.
- **Option:** represents missing values or failed number conversion.
- **Either:** `Right` means success; `Left` contains errors.
- **Error handling:** multiple errors can be reported for one record.

## Project flow
Raw input -> clean fields -> convert numeric fields -> check rules -> return valid record or error list -> show summary.

## Run the project
Install a JDK and sbt. Open a terminal in this folder and run:

```bash
sbt run
```

Run validation checks:

```bash
sbt "Test / runMain ValidationChecks"
```

## Expected output
```text
FUNCTIONAL DATA VALIDATION PIPELINE
-----------------------------------
VALID   | id=101, name=Asha, age=21, email=asha@example.com
VALID   | id=102, name=Rohan, age=17, email=rohan@example.com
INVALID | id must be a whole number (received 'abc'); name is required; age must be between 1 and 120 (received 150); email 'wrong-email' is not valid
INVALID | age must be a whole number (received 'twenty')
INVALID | id is required; email is required
-----------------------------------
Summary: 5 records checked, 2 valid, 3 invalid.
Input records are immutable; validation returns results without changing them.
```

## Demo points
Show one valid and one invalid row. Explain Option, Either, pattern matching, ADTs and immutability.

## Suggested contribution log
Edit this to reflect your group's actual work:
- Member 1: data model and validation rules
- Member 2: error types and messages
- Member 3: test cases and output checking
- Member 4: documentation and presentation
- Member 5: integration and final review

## Limitations
This learning demo uses sample records written in code. It does not connect to a database or import CSV files. Email validation is basic and does not verify that an inbox exists.
