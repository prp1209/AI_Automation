package com.automationexercise.config;

public final class ConfigManager {
    public static final String BASE_URL = System.getProperty("baseUrl", "https://www.automationexercise.com");
    public static final String JIRA_BASE_URL = value("jira.baseUrl", "JIRA_BASE_URL", "");
    public static final String JIRA_USER_EMAIL = valueWithFallback(
            "jira.userEmail",
            "JIRA_USER_EMAIL",
            "JIRA_EMAIL",
            ""
    );
    public static final String JIRA_API_TOKEN = value("jira.apiToken", "JIRA_API_TOKEN", "");
    public static final String JIRA_PROJECT_KEY = value("jira.projectKey", "JIRA_PROJECT_KEY", "ABC");
    public static final String JIRA_EPIC_KEY = value("jira.epicKey", "JIRA_EPIC_KEY", "ABC-1");
    public static final String JIRA_ISSUE_KEY = value("jira.issueKey", "JIRA_ISSUE_KEY", "");
    public static final String JIRA_EPIC_LINK_FIELD =
            value("jira.epicLinkField", "JIRA_EPIC_LINK_FIELD", "customfield_10014");

    private ConfigManager() {
    }

    private static String value(String propertyName, String environmentName, String defaultValue) {
        String propertyValue = System.getProperty(propertyName);
        if (propertyValue != null && !propertyValue.isBlank()) {
            return propertyValue;
        }

        String environmentValue = System.getenv(environmentName);
        return environmentValue == null || environmentValue.isBlank() ? defaultValue : environmentValue;
    }

    private static String valueWithFallback(
            String propertyName,
            String primaryEnvironmentName,
            String fallbackEnvironmentName,
            String defaultValue
    ) {
        String propertyValue = System.getProperty(propertyName);
        if (propertyValue != null && !propertyValue.isBlank()) {
            return propertyValue;
        }

        String primaryEnvironmentValue = System.getenv(primaryEnvironmentName);
        if (primaryEnvironmentValue != null && !primaryEnvironmentValue.isBlank()) {
            return primaryEnvironmentValue;
        }

        String fallbackEnvironmentValue = System.getenv(fallbackEnvironmentName);
        return fallbackEnvironmentValue == null || fallbackEnvironmentValue.isBlank()
                ? defaultValue
                : fallbackEnvironmentValue;
    }

    public static String signupName() {
        return requiredValue("signup.name", "SIGNUP_NAME");
    }

    public static String signupEmail() {
        return requiredValue("signup.email", "SIGNUP_EMAIL");
    }

    public static String signupPassword() {
        return requiredValue("signup.password", "SIGNUP_PASSWORD");
    }

    public static String signupFirstName() {
        return requiredValue("signup.firstName", "SIGNUP_FIRST_NAME");
    }

    public static String signupLastName() {
        return requiredValue("signup.lastName", "SIGNUP_LAST_NAME");
    }

    public static String signupGender() { return requiredValue("signup.gender", "SIGNUP_GENDER"); }
    public static String signupDay() { return requiredValue("signup.day", "SIGNUP_DAY"); }
    public static String signupMonth() { return requiredValue("signup.month", "SIGNUP_MONTH"); }
    public static String signupYear() { return requiredValue("signup.year", "SIGNUP_YEAR"); }
    public static String signupCompany() { return requiredValue("signup.company", "SIGNUP_COMPANY"); }
    public static String signupAddress1() { return requiredValue("signup.address1", "SIGNUP_ADDRESS1"); }
    public static String signupAddress2() { return requiredValue("signup.address2", "SIGNUP_ADDRESS2"); }
    public static String signupCountry() { return requiredValue("signup.country", "SIGNUP_COUNTRY"); }
    public static String signupState() { return requiredValue("signup.state", "SIGNUP_STATE"); }
    public static String signupCity() { return requiredValue("signup.city", "SIGNUP_CITY"); }
    public static String signupZipCode() { return requiredValue("signup.zipCode", "SIGNUP_ZIP_CODE"); }
    public static String signupPhone() { return requiredValue("signup.phone", "SIGNUP_PHONE"); }

    private static String requiredValue(String propertyName, String environmentName) {
        String configuredValue = value(propertyName, environmentName, "");
        if (configuredValue.isBlank()) {
            throw new IllegalStateException(
                    "Missing required signup configuration. Set JVM property '-D" + propertyName
                            + "' or environment variable '" + environmentName + "'.");
        }
        return configuredValue;
    }
}
