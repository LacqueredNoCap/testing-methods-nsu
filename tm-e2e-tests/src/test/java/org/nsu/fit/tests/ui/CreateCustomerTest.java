package org.nsu.fit.tests.ui;

import io.qameta.allure.Description;
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
import org.nsu.fit.utils.TestUtils;
import org.nsu.fit.tests.ui.screen.LoginScreen;

public class CreateCustomerTest {

    private LoginScreen loginScreen;
    private Browser browser;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
        loginScreen = new LoginScreen(browser);
    }

    @Test
    @Description("Create customer via UI.")
    @Severity(SeverityLevel.BLOCKER)
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
                .clickSubmit();

        Assert.assertEquals(
                browser.currentPage(),
                "http://localhost:8090/tm-frontend/admin"
        );

        // Лабораторная 4 (DONE): Проверить что customer создан с ранее переданными полями.
        // Решить проблему с генерацией случайных данных.
        int customerIndex = browser.getCustomerIndex(customer);
        Assert.assertTrue(customerIndex >= 0);

    }

    @AfterClass
    public void afterClass() {
        loginScreen.close();
    }

}
