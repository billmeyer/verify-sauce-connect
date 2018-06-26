package com.saucelabs.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class VerifySauceConnectPage
{
    private WebDriver driver;

    @FindBy(css = "#requestedURL")
    private WebElement requestedURL;

    public VerifySauceConnectPage(WebDriver driver)
    {
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    public String getFromPageRequestedURL()
    {
        return requestedURL.getText().trim();
    }
}
