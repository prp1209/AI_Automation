package com.automationexercise.pages;

import com.automationexercise.framework.UiElement;
import com.microsoft.playwright.Page;

public class LoginPage {
    private final Page page;
    private final UiElement loginFormTitle;
    private final UiElement signupFormTitle;
    private final UiElement loginEmail;
    private final UiElement loginPassword;
    private final UiElement loginButton;
    private final UiElement signupName;
    private final UiElement signupEmail;
    private final UiElement signupButton;
    private final UiElement loggedInUser;
    private final UiElement logoutLink;

    public LoginPage(Page page) {
        this.page = page;
        this.loginFormTitle = new UiElement(page, "login", "loginFormTitle");
        this.signupFormTitle = new UiElement(page, "login", "signupFormTitle");
        this.loginEmail = new UiElement(page, "login", "loginEmail");
        this.loginPassword = new UiElement(page, "login", "loginPassword");
        this.loginButton = new UiElement(page, "login", "loginButton");
        this.signupName = new UiElement(page, "login", "signupName");
        this.signupEmail = new UiElement(page, "login", "signupEmail");
        this.signupButton = new UiElement(page, "login", "signupButton");
        this.loggedInUser = new UiElement(page, "login", "loggedInUser");
        this.logoutLink = new UiElement(page, "login", "logoutLink");
    }

    public boolean isLoginFormVisible() {
        return loginFormTitle.isDisplayed();
    }

    public boolean isSignupFormVisible() {
        return signupFormTitle.isDisplayed();
    }

    public void login(String email, String password) {
        loginEmail.get().waitFor();
        loginEmail.type(email);
        loginPassword.type(password);
        loginButton.click();
    }

    public void signup(String name, String email) {
        signupName.get().waitFor();
        signupName.type(name);
        signupEmail.type(email);
        signupButton.click();
    }

    public AccountInformationPage signupAndOpenAccountInformation(String name, String email) {
        signup(name, email);
        return new AccountInformationPage(page);
    }

    public boolean isLoggedIn() {
        return loggedInUser.isDisplayed();
    }

    public void logout() {
        logoutLink.get().waitFor();
        logoutLink.click();
    }
}
