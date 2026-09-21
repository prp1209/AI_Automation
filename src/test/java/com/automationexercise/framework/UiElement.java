package com.automationexercise.framework;

import com.automationexercise.utils.ScreenshotManager;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class UiElement {
    private final Page page;
    private final LocatorResolver locatorResolver;
    private final String pageName;
    private final String elementName;

    public UiElement(Page page, String pageName, String elementName) {
        this.page = page;
        this.locatorResolver = new LocatorResolver();
        this.pageName = pageName;
        this.elementName = elementName;
    }

    public Locator get() {
        return locatorResolver.resolve(page, pageName, elementName);
    }

    public boolean isDisplayed() {
        return get().isVisible();
    }

    public void click() {
        get().click();
        ScreenshotManager.capture(page, pageName + "_" + elementName + "_click");
    }

    public void type(String text) {
        get().fill(text);
        ScreenshotManager.capture(page, pageName + "_" + elementName + "_type");
    }

    public void selectOption(String value) {
        Locator locator = get();
        Object selected = locator.evaluate(
                "(element, requested) => {" +
                        "const wanted = String(requested).trim().toLowerCase();" +
                        "const option = Array.from(element.options).find(item => " +
                        "item.value.trim().toLowerCase() === wanted || " +
                        "item.text.trim().toLowerCase() === wanted);" +
                        "if (!option) return false;" +
                        "element.value = option.value;" +
                        "element.dispatchEvent(new Event('input', {bubbles: true}));" +
                        "element.dispatchEvent(new Event('change', {bubbles: true}));" +
                        "return true;" +
                        "}",
                value
        );
        if (!Boolean.TRUE.equals(selected)) {
            throw new IllegalStateException(
                    "No dropdown option matched configured value for " + pageName + "." + elementName);
        }
        ScreenshotManager.capture(page, pageName + "_" + elementName + "_select");
    }

    public void check() {
        get().check();
        ScreenshotManager.capture(page, pageName + "_" + elementName + "_check");
    }

    public String getText() {
        return get().innerText();
    }

    public String getAttribute(String attributeName) {
        return get().getAttribute(attributeName);
    }
}
