package com.automationexercise.utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.Locale;

public final class BrowserSession implements AutoCloseable {
    private final Playwright playwright;
    private final Browser browser;
    private final BrowserContext context;
    private final Page page;

    private BrowserSession(Playwright playwright, Browser browser, BrowserContext context, Page page) {
        this.playwright = playwright;
        this.browser = browser;
        this.context = context;
        this.page = page;
    }

    public static BrowserSession create() {
        String browserName = System.getProperty("browser", "chromium").toLowerCase(Locale.ROOT);
        boolean headless = true;

        Playwright playwright = Playwright.create();
        BrowserType browserType = switch (browserName) {
            case "firefox" -> playwright.firefox();
            case "webkit", "safari" -> playwright.webkit();
            case "chrome", "chromium", "" -> playwright.chromium();
            default -> throw new IllegalArgumentException("Unsupported Playwright browser: " + browserName);
        };

        Browser browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(headless));
        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setIgnoreHTTPSErrors(true));
        return new BrowserSession(playwright, browser, context, context.newPage());
    }

    public Page page() {
        return page;
    }

    @Override
    public void close() {
        context.close();
        browser.close();
        playwright.close();
    }
}
