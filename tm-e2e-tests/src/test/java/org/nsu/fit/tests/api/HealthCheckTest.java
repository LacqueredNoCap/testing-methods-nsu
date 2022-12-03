package org.nsu.fit.tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.nsu.fit.services.rest.RestClient;
import org.nsu.fit.services.rest.data.HealthCheckPojo;

public class HealthCheckTest {

    private static final String OK_STATUS = "OK";

    private RestClient restClient;

    @BeforeClass
    private void setUp() {
        restClient = new RestClient();
    }

    @Test(description = "Health check test.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Health check feature")
    public void healthCheck(){
        HealthCheckPojo healthCheckPojo = restClient.healthCheck();

        Assert.assertNotNull(healthCheckPojo);

        Assert.assertEquals(
                healthCheckPojo.status,
                OK_STATUS
        );
        Assert.assertEquals(
                healthCheckPojo.dbStatus,
                OK_STATUS
        );
    }

}

