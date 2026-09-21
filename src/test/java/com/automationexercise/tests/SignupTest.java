package com.automationexercise.tests;

import com.automationexercise.base.BaseTest;
import com.automationexercise.config.ConfigManager;
import com.automationexercise.jira.JiraTestCase;
import com.automationexercise.pages.HomePage;
import com.automationexercise.pages.LoginPage;
import com.automationexercise.pages.AccountInformationPage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SignupTest extends BaseTest {

    @Test
    @JiraTestCase(
            summary = "Sign up to Automation Exercise",
            newTestScript = true,
            acceptanceCriteria = {
                    "The Automation Exercise home page loads successfully",
                    "The Signup / Login page displays the New User Signup form",
                    "The user can enter a name and email address",
                    "The account information page accepts first name and last name",
                    "Submitting the signup form proceeds to the account creation flow",
                    "The configured password and account information are entered"
            },
            steps = {
                    "Launch https://www.automationexercise.com/",
                    "Close the popup if it appears; otherwise wait for five seconds",
                    "Click Signup / Login",
                    "Click the Name field under New User Signup",
                    "Enter the configured signup name in the Name field",
                    "Enter the configured signup email in the Email Address field",
                    "Click the Signup button",
                    "Enter the configured first name in the First Name field",
                    "Enter the configured last name in the Last Name field",
                    "Enter the configured signup password and account details",
                    "Click Create Account"
            },
            expectedResult = "The signup form accepts the supplied name and email and proceeds to the account information page."
    )
    void signupWithNewUserDetails() {
        HomePage homePage = new HomePage(page).open();
        homePage.handleOptionalPopup();

        LoginPage loginPage = homePage.openLoginPage();
        assertThat(loginPage.isSignupFormVisible()).isTrue();

        AccountInformationPage accountInformationPage =
                loginPage.signupAndOpenAccountInformation(ConfigManager.signupName(), ConfigManager.signupEmail());
        accountInformationPage.enterFirstName(ConfigManager.signupFirstName());
        accountInformationPage.enterLastName(ConfigManager.signupLastName());
        accountInformationPage.enterPassword(ConfigManager.signupPassword());
        accountInformationPage.fillAdditionalDetails(
                ConfigManager.signupGender(),
                ConfigManager.signupDay(),
                ConfigManager.signupMonth(),
                ConfigManager.signupYear(),
                ConfigManager.signupCompany(),
                ConfigManager.signupAddress1(),
                ConfigManager.signupAddress2(),
                ConfigManager.signupCountry(),
                ConfigManager.signupState(),
                ConfigManager.signupCity(),
                ConfigManager.signupZipCode(),
                ConfigManager.signupPhone()
        );
        accountInformationPage.createAccount();
    }
}
