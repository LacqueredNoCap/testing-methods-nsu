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

public class CancelCreateCustomerTest {

    private LoginScreen loginScreen;
    private Browser browser;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
        loginScreen = new LoginScreen(browser);
    }

    @Test(description = "cancel create customer ")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Create customer feature")
    public void createCustomer() {
        CustomerPojo customer = TestUtils.randomCustomer();

        loginScreen
                .loginAsAdmin()
                .createCustomer()
                .fillEmail(customer.login)
                .fillPassword(customer.pass)
                .fillFirstName(customer.firstName)
                .fillLastName(customer.lastName)
                .clickCancel();

        int index = browser.getCustomerIndex(customer);
        Assert.assertEquals(index, -1);

        Assert.assertEquals(
                browser.currentPage(),
                "http://localhost:8090/tm-frontend/admin"
        );
    }

    @AfterClass
    public void afterClass() {
        loginScreen.close();
    }

}
