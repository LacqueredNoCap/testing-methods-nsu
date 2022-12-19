package org.nsu.fit.tests.ui;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;
import org.nsu.fit.services.rest.data.ContactPojo;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.nsu.fit.utils.TestUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginAsCustomerTest {

    private Browser browser;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test
    @Description("Create customer via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Create customer feature")
    public void createCustomer() {
        ContactPojo customer = TestUtils.randomCustomer();

        new LoginScreen(browser)
                .loginAsAdmin()
                .createCustomer()
                .fillEmail(customer.login)
                .fillPassword(customer.pass)
                .fillFirstName(customer.firstName)
                .fillLastName(customer.lastName);

        // Лабораторная 4: Проверить что customer создан с ранее переданными полями.
        // Решить проблему с генерацией случайных данных.
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
