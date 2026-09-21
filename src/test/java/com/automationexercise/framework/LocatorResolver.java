package com.automationexercise.framework;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class LocatorResolver {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String DEFAULT_LOCATOR_RESOURCE = "/locators/automation-exercise.json";
    private final Map<String, Map<String, List<LocatorConfig>>> pageLocators;

    public LocatorResolver() {
        this(DEFAULT_LOCATOR_RESOURCE);
    }

    public LocatorResolver(String resourcePath) {
        this.pageLocators = load(resourcePath);
    }

    private Map<String, Map<String, List<LocatorConfig>>> load(String resourcePath) {
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalStateException("Locator resource not found: " + resourcePath);
            }

            TypeReference<Map<String, Map<String, Map<String, List<LocatorConfig>>>>> type =
                    new TypeReference<>() {};
            return MAPPER.readValue(inputStream, type).getOrDefault("pages", Map.of());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load locator configuration from " + resourcePath, e);
        }
    }

    public Locator resolve(Page page, String pageName, String elementName) {
        List<LocatorConfig> candidates = candidates(pageName, elementName);
        RuntimeException lastFailure = null;

        for (LocatorConfig candidate : candidates) {
            try {
                Locator locator = toLocator(page, candidate);
                if (locator.count() > 0) {
                    return locator;
                }
            } catch (RuntimeException failure) {
                lastFailure = failure;
            }
        }

        throw new IllegalStateException(
                "Element '" + elementName + "' on page '" + pageName
                        + "' was not found using " + candidates.size() + " locator strategies",
                lastFailure);
    }

    public boolean isConfigured(String pageName, String elementName) {
        return !pageLocators.getOrDefault(pageName, Map.of())
                .getOrDefault(elementName, List.of()).isEmpty();
    }

    private List<LocatorConfig> candidates(String pageName, String elementName) {
        List<LocatorConfig> candidates = pageLocators.getOrDefault(pageName, Map.of()).get(elementName);
        if (candidates == null || candidates.isEmpty()) {
            throw new IllegalArgumentException(
                    "No locators configured for page='" + pageName + "', element='" + elementName + "'");
        }
        return candidates;
    }

    private Locator toLocator(Page page, LocatorConfig config) {
        if (config == null || config.getType() == null || config.getValue() == null) {
            throw new IllegalArgumentException("Locator config is incomplete");
        }

        return switch (config.getType()) {
            case CSS -> page.locator(config.getValue());
            case XPATH -> page.locator("xpath=" + config.getValue());
            case ID -> page.locator("#" + config.getValue());
            case NAME -> page.locator("[name='" + config.getValue() + "']");
            case TAG -> page.locator(config.getValue());
            case CLASS -> page.locator("." + config.getValue());
            case LINK_TEXT -> page.getByRole(com.microsoft.playwright.options.AriaRole.LINK,
                    new Page.GetByRoleOptions().setName(config.getValue()).setExact(true));
            case PARTIAL_LINK_TEXT -> page.getByRole(com.microsoft.playwright.options.AriaRole.LINK,
                    new Page.GetByRoleOptions().setName(config.getValue()));
        };
    }
}
