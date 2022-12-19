package org.nsu.fit.tests.ui;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import org.openqa.selenium.By;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.nsu.fit.utils.TestUtils;

public class CreateWrongCustomerTryTest {

    private LoginScreen loginScreen;
    private Browser browser;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
        loginScreen = new LoginScreen(browser);
    }

    @Test(description = "Create wrong customer try")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Create customer feature")
    public void createCustomer() {
        CustomerPojo customer = TestUtils.randomCustomer();

        Assert.assertThrows(Exception.class, () ->
                loginScreen
                        .loginAsAdmin()
                        .createCustomer()
                        .fillEmail(customer.login)
                        .fillPassword("1")
                        .fillFirstName(customer.firstName)
                        .fillLastName(customer.lastName)
                        .clickSubmit()
        );

        browser.waitForElement(By.xpath("//button[@type = 'button']"));
        browser.click(By.xpath("//button[@type = 'button']"));

        Assert.assertEquals(
                browser.currentPage(),
                "http://localhost:8090/tm-frontend/admin"
        );

        int customerIndex = browser.getCustomerIndex(customer);
        Assert.assertEquals(customerIndex, -1);

    }

    @AfterClass
    public void afterClass() {
        loginScreen.close();
    }
}
