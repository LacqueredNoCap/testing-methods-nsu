package org.nsu.fit.tests.ui;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.nsu.fit.services.browser.BrowserService;
import org.nsu.fit.services.rest.data.ContactPojo;
import org.nsu.fit.tests.ui.screen.AdminScreen;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.nsu.fit.utils.TestUtils;

public class LoginAsCustomerTest {

    private LoginScreen loginScreen;

    @BeforeClass
    public void beforeClass() {
        loginScreen = new LoginScreen(BrowserService.openNewBrowser());
    }

    @Test(description = "Login as customer")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Login as customer feature")
    public void loginAsCustomer() {
        ContactPojo customer = TestUtils.randomCustomer();

        AdminScreen screen = loginScreen
                .loginAsAdmin()
                .createCustomer()
                .fillEmail(customer.login)
                .fillPassword(customer.pass)
                .fillFirstName(customer.firstName)
                .fillLastName(customer.lastName)
                .clickSubmit();

        screen.logout().loginAsCustomer(customer.login, customer.pass);
    }

    @AfterClass
    public void afterClass() {
        loginScreen.close();
    }
}
