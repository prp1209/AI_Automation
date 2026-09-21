# Test Script for AI_Automation

## Jira ID

`ABC-XXXX`

> Replace `ABC-XXXX` with the Jira issue key returned after the successful test execution creates the Task.

## 1. Purpose
This script validates that the Java project builds successfully and runs without errors. It is intended for manual verification of the current Maven-based project configuration.

## 2. Project Information
- Project name: AI_Automation
- Language: Java
- Build tool: Maven
- Main class: `org.example.Main`

## 3. Prerequisites
Before running the tests, confirm the following are installed and available in PATH:
- JDK 17 or compatible Java version
- Maven 3.9+ or newer
- Playwright browser binaries installed
- Jira access configured when a new test script must create a ticket
- Jira ID: `ABC-XXXX` (replace with the actual Jira ID returned after successful creation)
- Signup test data configured through environment variables or JVM properties

Check prerequisites:

```bash
java -version
mvn -v
```

## 4. Test Environment
- Operating System: Windows / any OS with Java, Maven, and Playwright browser binaries installed
- Working directory: project root (`AI_Automation`)

Install Playwright browser binaries when setting up a new machine:

```powershell
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

## 5. Test Execution Steps

### Step 0: Run the generated test script

The test steps supplied in:

```text
C:\Users\pawan\OneDrive - ganavitech.com 1\Desktop\Test_cases.txt
```

are implemented in `SignupTest#signupWithNewUserDetails`.

Run only this new test with:

```powershell
mvn test -Dtest=SignupTest#signupWithNewUserDetails -Dbrowser=chromium
```

Before running with Jira reporting, configure `JIRA_BASE_URL`, `JIRA_EMAIL` (or `JIRA_USER_EMAIL`), and `JIRA_API_TOKEN`. Because this is a new test script, a successful run creates a Jira Task in project `ABC` under Epic `ABC-1`. Copy the returned Jira key into the prerequisite above, replacing `ABC-XXXX`.

The signup name and email are never hardcoded in the test script. Configure them in the same PowerShell session:

```powershell
$env:SIGNUP_NAME = "your-test-name"
$env:SIGNUP_EMAIL = "your-test-email@example.com"
$env:SIGNUP_PASSWORD = "your-test-password"
$env:SIGNUP_FIRST_NAME = "your-first-name"
$env:SIGNUP_LAST_NAME = "your-last-name"
$env:SIGNUP_GENDER = "Mr"
$env:SIGNUP_DAY = "12"
$env:SIGNUP_MONTH = "September"
$env:SIGNUP_YEAR = "1999"
$env:SIGNUP_COMPANY = "your-company"
$env:SIGNUP_ADDRESS1 = "your-address-1"
$env:SIGNUP_ADDRESS2 = "your-address-2"
$env:SIGNUP_COUNTRY = "India"
$env:SIGNUP_STATE = "your-state"
$env:SIGNUP_CITY = "your-city"
$env:SIGNUP_ZIP_CODE = "your-zip-code"
$env:SIGNUP_PHONE = "your-phone-number"
$env:JIRA_PROJECT_KEY = "ABC"
$env:JIRA_EPIC_KEY = "ABC-1"
```

Maven passes these environment variables to the test JVM through `pom.xml`; the actual values are not stored in `pom.xml`. The password and personal data are never printed or included in Jira descriptions.

Then run:

```powershell
mvn test -Dtest=SignupTest#signupWithNewUserDetails -Dbrowser=chromium
```

JVM properties can be used instead:

```powershell
mvn test -Dtest=SignupTest#signupWithNewUserDetails -Dsignup.name="your-test-name" -Dsignup.email="your-test-email@example.com"
```

The test fails with a clear configuration error if either value is missing.

### Login test

The login test reuses `SIGNUP_EMAIL` and `SIGNUP_PASSWORD`; no separate login credentials are hardcoded:

```powershell
mvn test -Dtest=LoginTest#loginWithSignupCredentials -Dbrowser=chromium
```

The test is implemented in `LoginTest#loginWithSignupCredentials` and verifies that the logged-in user indicator is displayed after login.

All Playwright tests run headlessly by default. The framework enforces headless mode.

### Step 1: Build the project
Run:

```bash
mvn clean test -Dbrowser=chromium
```

Expected result:
- Maven downloads dependencies if required
- Project compiles successfully
- Test phase completes without build errors

If the project has no unit tests yet, the command still validates compilation and configuration.

### Step 2: Run the application
Run:

```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```

If `exec-maven-plugin` is not configured, use:

```bash
java -cp target/classes org.example.Main
```

Expected result:
- Console output begins with: `Hello and welcome!`
- Program prints `i = 1` through `i = 5`
- Application exits normally without exceptions

## 6. Expected Output
The console should display output similar to:

```text
Hello and welcome!i = 1
i = 2
i = 3
i = 4
i = 5
```

Note: formatting may vary slightly depending on the Java runtime and print statements.

## 7. Acceptance Criteria
The test is considered successful when all of the following are true:
- Maven build completes without errors
- Java source compiles successfully
- Main application runs without crashes
- Console output matches the expected startup message and loop output

## 8. Failure Handling
If any step fails:
1. Confirm Java and Maven are installed correctly
2. Verify the project root is the correct directory
3. Check the `pom.xml` for the correct Java version and configuration
4. Review compiler or runtime errors in the console output
5. Re-run the build after fixing the issue

## 9. Test Summary
This project currently contains a simple Java application. The smoke test focuses on verifying that the project can compile, run, and print the expected welcome message and loop values.

## 10. Jira Ticket Rules

### New test scripts

When a newly created test script completes successfully, it must create a Jira Task under Epic `ABC-1`. The test must be explicitly marked as a new script:

```java
@JiraTestCase(
    summary = "Validate a new Automation Exercise flow",
    newTestScript = true,
    acceptanceCriteria = {"The flow is available to the user"},
    steps = {"Open the application", "Execute the new flow"},
    expectedResult = "The flow completes successfully"
)
```

The generated Jira Task contains the Acceptance Criteria, Steps to Reproduce, Expected Result, Actual Result, and execution details.

### Existing test scripts

Existing test scripts must use `newTestScript = false` (the default). A successful run of an existing script must not create a new Jira ticket. If the user wants to rerun an existing test against an already-created Jira ticket, the Jira issue key must be supplied:

```powershell
mvn test -Dtest=HomePageTest#homePageLoadsAndLoginFormIsAccessible -Djira.issueKey=ABC-123
```

The supplied issue is updated only when Jira reporting is configured. This allows an existing test script to update its existing Jira Task without creating a duplicate.

## 11. Screenshot Rule

After every executed UI action or page-navigation step, the framework captures a screenshot for reference. Screenshots are saved automatically under:

```text
target/screenshots/<TestClass>_<testMethod>/
```

Screenshot file names include the timestamp and logical step name, for example:

```text
target/screenshots/SignupTest_signupWithNewUserDetails/20260921_105500_123_login_signupEmail_type.png
```

Page objects and test scripts should use the reusable Playwright framework actions such as `UiElement.click()` and `UiElement.type()` so screenshots are captured consistently. New UI actions must also call `ScreenshotManager.capture(...)` immediately after the action.

Each test script gets its own screenshot folder. For example:

```text
target/screenshots/SIGNUPTEST_SignupwithSIgnupCredentials/
target/screenshots/LOGINTEST_LoginWithSignupCredentials/
```

The login test uses `LOGINTEST_LoginWithSignupCredentials`. All other test scripts use `SIGNUPTEST_SignupwithSIgnupCredentials`.

### Screenshot refresh rule

Every time a test script is executed, it must create a fresh screenshot for each
page navigation and UI action in that test's respective subfolder under
`target/screenshots/`. Existing screenshots must not be reused as evidence for a
new execution. Screenshot filenames include the execution timestamp, so each
run adds updated evidence without overwriting earlier results.

For example, every `LoginTest` execution writes new files to:

```text
target/screenshots/LOGINTEST_LoginWithSignupCredentials/
```

Every other test script writes new files to:

```text
target/screenshots/SIGNUPTEST_SignupwithSIgnupCredentials/
```

## 12. Build the Solution

After generating or modifying test scripts, build the complete solution from the project root:

```powershell
mvn clean test
```

Expected result:
- The project compiles successfully.
- All test classes are discovered by JUnit.
- The generated signup script executes successfully.
- Screenshots are available under `target/screenshots/`.
- If Jira is configured, the new test creates a Task and returns its Jira ID.

## 13. Allure Report

Run a test to create Allure results and generate the HTML report:

```powershell
mvn test -Dtest=LoginTest#loginWithSignupCredentials
mvn io.qameta.allure:allure-maven:2.15.0:report
```

The generated report is available at:

```text
target/site/allure-maven-plugin/index.html
```

Screenshots captured during UI actions are attached to the corresponding Allure test steps. To view the report in a local browser, run:

```powershell
mvn io.qameta.allure:allure-maven:2.15.0:serve
```
