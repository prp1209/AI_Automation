package com.automationexercise.jira;

import com.automationexercise.config.ConfigManager;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class JiraExecutionExtension implements AfterTestExecutionCallback {
    private final JiraReporter reporter = new JiraReporter();

    @Override
    public void afterTestExecution(ExtensionContext context) {
        JiraTestCase metadata = context.getRequiredTestMethod().getAnnotation(JiraTestCase.class);
        if (metadata == null || context.getExecutionException().isPresent()) {
            return;
        }

        if (!metadata.newTestScript() && ConfigManager.JIRA_ISSUE_KEY.isBlank()) {
            return;
        }

        if (!reporter.isConfigured()) {
            return;
        }

        String actualResult = "Test completed successfully. JUnit test: "
                + context.getRequiredTestClass().getSimpleName() + "#"
                + context.getRequiredTestMethod().getName()
                + ". Browser: " + System.getProperty("browser", "chrome")
                + ". Base URL: " + ConfigManager.BASE_URL;

        reporter.createOrUpdate(metadata, actualResult);
    }
}
