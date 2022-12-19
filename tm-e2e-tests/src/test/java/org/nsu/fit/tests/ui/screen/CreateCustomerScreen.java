package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

public class CreateCustomerScreen extends Screen {

    public CreateCustomerScreen(Browser browser) {
        super(browser);
    }

    public CreateCustomerScreen fillFirstName(String firstName) {
        browser.typeText(By.name("firstName"), firstName);
        return this;
    }

    public CreateCustomerScreen fillLastName(String lastName) {
        browser.typeText(By.name("lastName"), lastName);
        return this;
    }

    public CreateCustomerScreen fillEmail(String email) {
        browser.typeText(By.name("login"), email);
        return this;
    }

    public CreateCustomerScreen fillPassword(String password) {
        browser.typeText(By.name("pass"), password);
        return this;
    }

    // Лабораторная 4: Подумайте как обработать ситуацию,
    // когда при нажатии на кнопку Submit ('Create') не произойдет переход на AdminScreen,
    // а будет показана та или иная ошибка на текущем скрине.
    public AdminScreen clickSubmit() {
        browser.waitForElement(By.xpath("//button[@type = 'submit']"));
        browser.click(By.xpath("//button[@type = 'submit']"));
        try
        {
            browser.waitForElement(By.xpath("//button[@title = 'Add Customer']"), 2);
            return new AdminScreen(browser);
        }
        catch (TimeoutException e)
        {
            String message = browser.getText(By.xpath("/html/body/div[1]/div/div/div[1]"));
            throw new TimeoutException(message);
        }
    }

    public AdminScreen clickCancel() {
        browser.click(By.xpath("//button[@type = 'button']"));
        return new AdminScreen(browser);
    }

}