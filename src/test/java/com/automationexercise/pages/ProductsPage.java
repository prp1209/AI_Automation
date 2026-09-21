package com.automationexercise.pages;

import com.automationexercise.framework.UiElement;
import com.automationexercise.utils.ScreenshotManager;
import com.microsoft.playwright.Page;

public class ProductsPage {
    private final Page page;
    private final UiElement pageTitle;
    private final UiElement searchInput;
    private final UiElement searchButton;

    public ProductsPage(Page page) {
        this.page = page;
        this.pageTitle = new UiElement(page, "products", "pageTitle");
        this.searchInput = new UiElement(page, "products", "searchInput");
        this.searchButton = new UiElement(page, "products", "searchButton");
    }

    public ProductsPage open() {
        page.navigate("https://www.automationexercise.com/products");
        ScreenshotManager.capture(page, "products_page_loaded");
        return this;
    }

    public boolean isLoaded() {
        return pageTitle.isDisplayed() || searchInput.isDisplayed();
    }

    public void searchFor(String keyword) {
        searchInput.type(keyword);
        searchButton.click();
    }
}
