package com.automationexercise.tests;

import com.automationexercise.base.BaseTest;
import com.automationexercise.config.ConfigManager;
import com.automationexercise.jira.JiraTestCase;
import com.automationexercise.pages.HomePage;
import com.automationexercise.pages.LoginPage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginTest extends BaseTest {

    @Test
    @JiraTestCase(
            summary = "Login to Automation Exercise with configured signup credentials",
            newTestScript = true,
            acceptanceCriteria = {
                    "The Automation Exercise login page is accessible",
                    "The configured signup email and password are accepted",
                    "The user is shown as logged in after submitting the login form"
            },
            steps = {
                    "Launch https://www.automationexercise.com/",
                    "Click Signup / Login",
                    "Enter the configured signup email",
                    "Enter the configured signup password",
                    "Click Login",
                    "Verify the logged-in user indicator",
                    "Click Logout"
            },
            expectedResult = "The user logs in successfully and can sign out using the configured signup email and password."
    )
    void loginWithSignupCredentials() {
        HomePage homePage = new HomePage(page).open();
        LoginPage loginPage = homePage.openLoginPage();

        assertThat(loginPage.isLoginFormVisible()).isTrue();
        loginPage.login(ConfigManager.signupEmail(), ConfigManager.signupPassword());

        assertThat(loginPage.isLoggedIn()).isTrue();
        loginPage.logout();
    }
}
