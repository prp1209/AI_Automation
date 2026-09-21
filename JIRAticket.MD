# Jira Ticket

## Summary
Implement a maintainable UI automation framework for Automation Exercise with externalized and fallback-based locators.

## Issue Type
Story

## Description
Create a reusable Playwright Java and JUnit automation framework for testing:

https://www.automationexercise.com/

Test scripts must use logical element names instead of hardcoded CSS or XPath selectors. Locator definitions must be maintained outside the test logic so that UI locator changes can be handled without modifying existing test cases.

## Business Value
Externalized and resilient locators reduce maintenance effort and prevent UI selector changes from affecting the business logic of automated test scripts.

## Scope
- Configure Maven dependencies for Playwright Java, JUnit 5, Jackson, and AssertJ.
- Add Playwright browser-context setup and teardown support.
- Support Chromium, Firefox, and WebKit execution.
- Support headless browser execution.
- Add page object classes for Home, Login, and Products pages.
- Add external JSON locator configuration.
- Support multiple fallback locator strategies for each logical element.
- Add reusable UI element actions such as click, type, visibility checks, text retrieval, and attribute retrieval.
- Add smoke tests for the Home, Login, and Products flows.

## Acceptance Criteria
- [ ] Test scripts do not contain hardcoded CSS selectors or XPath expressions.
- [ ] Locator definitions are stored in `src/test/resources/locators/automation-exercise.json`.
- [ ] Each logical element can define more than one locator strategy.
- [ ] The framework tries fallback locators when the primary locator is not available.
- [ ] Locator changes can be made in the external configuration without changing test logic.
- [ ] A Playwright browser context and page are created and closed automatically for each test.
- [ ] Tests support Chromium, Firefox, and WebKit through a system property.
- [ ] Tests run headlessly for every execution.
- [ ] The project compiles successfully with Maven.
- [ ] Home page loading and navigation to Login and Products pages are covered by automated tests.

## Technical Notes

### Browser Selection

```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

### Browser Execution

```bash
mvn test -Dbrowser=chromium
```

All Playwright tests run in headless mode by default. Headless mode is enforced by the framework for every test execution.

### Environment URL Override

```bash
mvn test -DbaseUrl=https://www.automationexercise.com
```

### Locator Configuration Example

```json
{
  "loginEmail": [
    {
      "type": "CSS",
      "value": "input[data-qa='login-email']"
    },
    {
      "type": "XPATH",
      "value": "//input[@name='email' and @placeholder='Email Address']"
    }
  ]
}
```

## Out of Scope
- Production user account management
- Payment processing validation
- API automation
- Automatic discovery and modification of locator files at runtime

## Definition of Done
- [ ] Framework code is implemented and organized into reusable packages.
- [ ] Page objects use logical locator names.
- [ ] Locator configuration contains fallback strategies.
- [ ] Smoke tests are available.
- [ ] Maven compilation succeeds.
- [ ] Usage instructions are documented.

## Jira Execution Rules

### Successful test execution

When a new test annotated with `@JiraTestCase(newTestScript = true)` completes successfully:

- If no Jira issue key is supplied, create a Jira `Task`.
- Create the Task in Jira project `ABC`.
- Create the Task under Epic `ABC-1` by default.
- Generate and include detailed:
  - Acceptance Criteria
  - Steps to Reproduce
  - Expected Result
  - Actual Result
  - Execution details

### Re-running an existing test case

Existing tests use `newTestScript = false` by default and do not create new Jira tickets.

The user must provide the existing Jira issue key when rerunning a test case. The reporter updates that Jira Task instead of creating a new one:

```bash
mvn test -Dtest=HomePageTest#homePageLoadsAndLoginFormIsAccessible -Djira.issueKey=ABC-123
```

Without `-Djira.issueKey`, a successful execution of an existing test does not create a Jira ticket.

### Jira configuration

Credentials must be provided through environment variables or JVM properties. Do not commit credentials:

```powershell
$env:JIRA_BASE_URL = "https://your-company.atlassian.net"
$env:JIRA_USER_EMAIL = "automation-user@example.com"
$env:JIRA_API_TOKEN = "<token>"
$env:JIRA_PROJECT_KEY = "ABC"
$env:JIRA_EPIC_KEY = "ABC-1"
$env:SIGNUP_NAME = "your-test-name"
$env:SIGNUP_EMAIL = "your-test-email@example.com"

mvn test -Dheadless=true
```

Supported JVM property equivalents are `jira.baseUrl`, `jira.userEmail`, `jira.apiToken`, `jira.projectKey`, `jira.epicKey`, `jira.issueKey`, `signup.name`, and `signup.email`.

The default Jira Epic Link field is `customfield_10014`. For Jira configurations using another Epic Link custom field, provide:

```powershell
$env:JIRA_EPIC_LINK_FIELD = "customfield_XXXXX"
```

Jira reporting is skipped when the Jira URL, user email, or API token is not configured. A test failure is never reported as a successful result. Only successful new tests trigger Task creation; existing tests trigger an update only when `-Djira.issueKey` is supplied.
