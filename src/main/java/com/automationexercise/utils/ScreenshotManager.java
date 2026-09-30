package com.automationexercise.utils;

import io.qameta.allure.Allure;
import com.microsoft.playwright.Page;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

public final class ScreenshotManager {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");
    private static final ThreadLocal<String> TEST_NAME = new ThreadLocal<>();

    private ScreenshotManager() {
    }

    public static void setTestName(String className, String methodName) {
        if ("LoginTest".equalsIgnoreCase(className)
                && "loginWithSignupCredentials".equalsIgnoreCase(methodName)) {
            TEST_NAME.set("LOGINTEST_LoginWithSignupCredentials");
            return;
        }

        TEST_NAME.set("SIGNUPTEST_SignupwithSIgnupCredentials");
    }

    public static Path capture(Page page, String stepName) {
        String testName = TEST_NAME.get();
        if (testName == null || testName.isBlank()) {
            testName = "unassigned_test";
        }

        Path directory = Path.of("target", "screenshots", testName);
        String safeStepName = safeName(stepName);
        Path target = directory.resolve(LocalDateTime.now().format(FORMATTER) + "_" + safeStepName + ".png");

        try {
            Files.createDirectories(directory);
            page.screenshot(new Page.ScreenshotOptions().setPath(target).setFullPage(true));
            try (ByteArrayInputStream screenshot = new ByteArrayInputStream(Files.readAllBytes(target))) {
                Allure.addAttachment(safeStepName, "image/png", screenshot, ".png");
            }
            return target;
        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException("Unable to save screenshot for step: " + stepName, e);
        }
    }

    public static void clearTestName() {
        TEST_NAME.remove();
    }

    private static String safeName(String value) {
        return value.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
