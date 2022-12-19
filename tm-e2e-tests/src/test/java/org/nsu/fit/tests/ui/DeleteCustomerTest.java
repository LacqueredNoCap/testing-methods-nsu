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
import org.nsu.fit.tests.ui.screen.AdminScreen;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.nsu.fit.utils.TestUtils;

public class DeleteCustomerTest {

    private LoginScreen loginScreen;
    private Browser browser;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
        loginScreen = new LoginScreen(browser);
    }

    @Test(description = "Delete customer.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Delete customer feature")
    public void deleteCustomer() {
        CustomerPojo customer = TestUtils.randomCustomer();

        AdminScreen adminScreen = loginScreen
                .loginAsAdmin()
                .createCustomer()
                .fillEmail(customer.login)
                .fillPassword(customer.pass)
                .fillFirstName(customer.firstName)
                .fillLastName(customer.lastName)
                .clickSubmit();

        int customerIndex = browser.getCustomerIndex(customer);
        Assert.assertTrue(customerIndex >= 0);

        adminScreen.deleteCustomer(customerIndex);

        customerIndex = browser.getCustomerIndex(customer);
        Assert.assertEquals(customerIndex, -1);
    }

    @AfterClass
    public void afterClass() {
        loginScreen.close();
    }
}
