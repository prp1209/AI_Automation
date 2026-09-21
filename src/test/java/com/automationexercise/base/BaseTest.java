package com.automationexercise.base;

import com.automationexercise.jira.JiraExecutionExtension;
import com.automationexercise.utils.BrowserSession;
import com.automationexercise.utils.ScreenshotManager;
import com.microsoft.playwright.Page;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
@ExtendWith({JiraExecutionExtension.class, AllureJunit5.class})
public abstract class BaseTest {
    protected BrowserSession browserSession;
    protected Page page;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        ScreenshotManager.setTestName(
                testInfo.getTestClass().map(Class::getSimpleName).orElse("unknown_test"),
                testInfo.getTestMethod().map(method -> method.getName()).orElse("unknown_method")
        );
        browserSession = BrowserSession.create();
        page = browserSession.page();
    }

    @AfterEach
    void tearDown() {
        if (browserSession != null) {
            browserSession.close();
        }
        ScreenshotManager.clearTestName();
    }
}
