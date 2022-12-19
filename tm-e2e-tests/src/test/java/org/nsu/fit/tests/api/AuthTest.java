package org.nsu.fit.tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.CustomerPojo;

public class AuthTest {

    private RestClient restClient;
    private AccountTokenPojo adminToken;

    @BeforeClass
    void setUp() {
        restClient = new RestClient();
    }

    // Лабораторная 3: Разобраться с аннотациями, как они влияют на итоговый отчет.
    @Test(description = "Authenticate as admin.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Authentication feature")
    public void authAsAdminTest() {
        adminToken = restClient.authenticate("admin", "setup");

        Assert.assertNotNull(adminToken);
    }

    @Test(dependsOnMethods = {"authAsAdminTest"}, description = "Authenticate as admin.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Authentication feature")
    public void authAsCustomerTest() {
        CustomerPojo customerPojo = restClient.createAutoGeneratedCustomer(adminToken);

        AccountTokenPojo token = restClient.authenticate(customerPojo.login, customerPojo.pass);

        Assert.assertNotNull(token);
    }

}
