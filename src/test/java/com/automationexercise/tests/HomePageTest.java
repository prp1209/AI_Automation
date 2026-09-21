package com.automationexercise.tests;

import com.automationexercise.base.BaseTest;
import com.automationexercise.jira.JiraTestCase;
import com.automationexercise.pages.HomePage;
import com.automationexercise.pages.LoginPage;
import com.automationexercise.pages.ProductsPage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HomePageTest extends BaseTest {

    @Test
    @JiraTestCase(
            summary = "Validate Automation Exercise home page and login navigation",
            newTestScript = false,
            acceptanceCriteria = {
                    "The Automation Exercise home page is available",
                    "The site logo is visible",
                    "The Login and Signup forms are accessible"
            },
            steps = {
                    "Open https://www.automationexercise.com/",
                    "Verify the site logo is displayed",
                    "Click Signup / Login",
                    "Verify the Login to your account form",
                    "Verify the New User Signup form"
            },
            expectedResult = "The home page loads and both authentication forms are displayed without errors."
    )
    void homePageLoadsAndLoginFormIsAccessible() {
        HomePage homePage = new HomePage(page).open();

        assertThat(homePage.isLogoVisible()).isTrue();

        LoginPage loginPage = homePage.openLoginPage();

        assertThat(loginPage.isLoginFormVisible()).isTrue();
        assertThat(loginPage.isSignupFormVisible()).isTrue();
    }

    @Test
    @JiraTestCase(
            summary = "Validate Automation Exercise products page availability",
            newTestScript = false,
            acceptanceCriteria = {
                    "The Products page is reachable",
                    "The Products page exposes the product search controls"
            },
            steps = {
                    "Open https://www.automationexercise.com/products",
                    "Wait for the Products page content to load",
                    "Verify the page title or product search control is displayed"
            },
            expectedResult = "The Products page loads successfully and is ready for product-search operations."
    )
    void productsPageLoads() {
        ProductsPage productsPage = new ProductsPage(page).open();

        assertThat(productsPage.isLoaded()).isTrue();
    }
}
