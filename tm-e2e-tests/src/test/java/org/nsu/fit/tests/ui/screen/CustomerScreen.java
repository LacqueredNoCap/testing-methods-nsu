package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

public class CustomerScreen extends Screen {

    public CustomerScreen(Browser browser) {
        super(browser);
    }

    public CustomerScreen topUpBalance(int balance) {
        browser.waitForElement(By.linkText("Top up balance"));
        browser.click(By.linkText("Top up balance"));
        browser.waitForElement(By.name("money"));
        browser.typeText(By.name("money"), String.valueOf(balance));
        browser.click(By.xpath("//button[@type = 'submit']"));
        return new CustomerScreen(browser);
    }

    public LoginScreen logout() {
        browser.waitForElement(By.linkText("Logout"));
        browser.click(By.linkText("Logout"));
        browser.waitForElement(By.xpath("//div[@class='Login']"));
        return new LoginScreen(browser);
    }

}
