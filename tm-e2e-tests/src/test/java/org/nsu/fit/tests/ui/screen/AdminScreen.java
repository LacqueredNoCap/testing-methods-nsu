package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

public class AdminScreen extends Screen {

    public AdminScreen(Browser browser) {
        super(browser);
    }

    public CreateCustomerScreen createCustomer() {
        browser.waitForElement(By.xpath("//button[@title = 'Add Customer']"));
        browser.click(By.xpath("//button[@title = 'Add Customer']"));
        return new CreateCustomerScreen(browser);
    }

    public AdminScreen deleteCustomer(int indexInCustomerTable) {
        String pathToTable = "//*[@id=\"root\"]/div/div/div/div/div[1]/div[2]/div/div/div/table/tbody/";
        browser.waitForElement(By.xpath(pathToTable + "tr[" + (indexInCustomerTable+1) + "]/td[1]/div/button"));
        browser.click(By.xpath(pathToTable + "tr[" + (indexInCustomerTable+1) + "]/td[1]/div/button"));
        browser.waitForElement(By.xpath(pathToTable + "tr[" + (indexInCustomerTable+1) + "]/td[1]/div/button"));
        browser.click(By.xpath(pathToTable + "tr[" + (indexInCustomerTable+1) + "]/td[1]/div/button"));
        return new AdminScreen(browser);
    }

    public LoginScreen logout() {
        browser.waitForElement(By.linkText("Logout"));
        browser.click(By.linkText("Logout"));
        browser.waitForElement(By.xpath("//div[@class='Login']"));
        return new LoginScreen(browser);
    }

}
