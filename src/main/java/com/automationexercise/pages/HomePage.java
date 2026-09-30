package com.automationexercise.pages;

import com.automationexercise.config.ConfigManager;
import com.automationexercise.framework.UiElement;
import com.automationexercise.framework.LocatorResolver;
import com.automationexercise.utils.ScreenshotManager;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;

import java.time.Duration;

public class HomePage {
    private final Page page;
    private final UiElement logo;
    private final UiElement loginLink;
    private final UiElement productsLink;
    private final UiElement optionalPopupClose;
    private final LocatorResolver locatorResolver;

    public HomePage(Page page) {
        this.page = page;
        this.logo = new UiElement(page, "home", "logo");
        this.loginLink = new UiElement(page, "home", "loginLink");
        this.productsLink = new UiElement(page, "home", "productsLink");
        this.optionalPopupClose = new UiElement(page, "home", "optionalPopupClose");
        this.locatorResolver = new LocatorResolver();
    }

    public HomePage open() {
        page.navigate(ConfigManager.BASE_URL);
        logo.get().waitFor();
        ScreenshotManager.capture(page, "home_page_loaded");
        return this;
    }

    public void handleOptionalPopup() {
        if (locatorResolver.isConfigured("home", "optionalPopupClose")) {
            try {
                if (optionalPopupClose.isDisplayed()) {
                    optionalPopupClose.click();
                    return;
                }
            } catch (TimeoutError ignored) {
                // The popup is optional; continue with the documented wait when it is absent.
            }
        }

        try {
            Thread.sleep(Duration.ofSeconds(5).toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for the optional popup", e);
        }
        ScreenshotManager.capture(page, "optional_popup_checked");
    }

    public boolean isLogoVisible() {
        return logo.isDisplayed();
    }

    public LoginPage openLoginPage() {
        loginLink.get().waitFor();
        loginLink.click();
        return new LoginPage(page);
    }

    public ProductsPage openProductsPage() {
        productsLink.get().waitFor();
        productsLink.click();
        return new ProductsPage(page);
    }
}
