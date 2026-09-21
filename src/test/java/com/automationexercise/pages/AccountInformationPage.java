package com.automationexercise.pages;

import com.automationexercise.framework.UiElement;
import com.microsoft.playwright.Page;
import com.automationexercise.utils.ScreenshotManager;

public class AccountInformationPage {
    private final UiElement firstName;
    private final UiElement lastName;
    private final UiElement password;
    private final UiElement genderMr;
    private final UiElement day;
    private final UiElement month;
    private final UiElement year;
    private final UiElement newsletter;
    private final UiElement offers;
    private final UiElement company;
    private final UiElement address1;
    private final UiElement address2;
    private final UiElement country;
    private final UiElement state;
    private final UiElement city;
    private final UiElement zipCode;
    private final UiElement phone;
    private final UiElement createAccount;
    private final Page page;

    public AccountInformationPage(Page page) {
        this.page = page;
        this.firstName = new UiElement(page, "accountInformation", "firstName");
        this.lastName = new UiElement(page, "accountInformation", "lastName");
        this.password = new UiElement(page, "accountInformation", "password");
        this.genderMr = new UiElement(page, "accountInformation", "genderMr");
        this.day = new UiElement(page, "accountInformation", "day");
        this.month = new UiElement(page, "accountInformation", "month");
        this.year = new UiElement(page, "accountInformation", "year");
        this.newsletter = new UiElement(page, "accountInformation", "newsletter");
        this.offers = new UiElement(page, "accountInformation", "offers");
        this.company = new UiElement(page, "accountInformation", "company");
        this.address1 = new UiElement(page, "accountInformation", "address1");
        this.address2 = new UiElement(page, "accountInformation", "address2");
        this.country = new UiElement(page, "accountInformation", "country");
        this.state = new UiElement(page, "accountInformation", "state");
        this.city = new UiElement(page, "accountInformation", "city");
        this.zipCode = new UiElement(page, "accountInformation", "zipCode");
        this.phone = new UiElement(page, "accountInformation", "phone");
        this.createAccount = new UiElement(page, "accountInformation", "createAccount");
    }

    public void enterFirstName(String value) {
        firstName.type(value);
    }

    public void enterLastName(String value) {
        lastName.type(value);
    }

    public void enterPassword(String value) {
        password.type(value);
    }

    public void fillAdditionalDetails(
            String gender, String dayValue, String monthValue, String yearValue,
            String companyValue, String address1Value, String address2Value,
            String countryValue, String stateValue, String cityValue,
            String zipValue, String phoneValue
    ) {
        if ("Mr".equalsIgnoreCase(gender)) {
            genderMr.check();
        }
        day.selectOption(dayValue);
        month.selectOption(monthValue);
        year.selectOption(yearValue);
        newsletter.check();
        offers.check();
        company.type(companyValue);
        address1.type(address1Value);
        address2.type(address2Value);
        country.selectOption(countryValue);
        state.type(stateValue);
        city.type(cityValue);
        zipCode.type(zipValue);
        phone.type(phoneValue);
    }

    public void createAccount() {
        createAccount.click();
        ScreenshotManager.capture(page, "accountInformation_createAccount");
    }
}
