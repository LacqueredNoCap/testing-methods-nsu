package org.nsu.fit.tests.ui;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.nsu.fit.utils.TestUtils;

public class BalanceUpdateTest {

    private LoginScreen loginScreen;
    private Browser browser;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
        loginScreen = new LoginScreen(browser);
    }

    @Test(description = "Top up balance.")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Top up balance feature")
    public void createCustomer() {
        int topUpBalance = 1234;
        CustomerPojo customer = TestUtils.randomCustomer();

        loginScreen
                .loginAsAdmin()
                .createCustomer()
                .fillEmail(customer.login)
                .fillPassword(customer.pass)
                .fillFirstName(customer.firstName)
                .fillLastName(customer.lastName)
                .clickSubmit()
                .logout();

        loginScreen
                .loginAsCustomer(customer.login, customer.pass)
                .topUpBalance(topUpBalance)
                .logout();

        loginScreen.loginAsAdmin();

        customer.balance += topUpBalance;

        int customerIndex = browser.getCustomerIndex(customer);
        Assert.assertTrue(customerIndex >= 0);
    }

    @AfterClass
    public void afterClass() {
        loginScreen.close();
    }

}
