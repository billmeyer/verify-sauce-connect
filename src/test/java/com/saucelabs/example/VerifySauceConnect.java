package com.saucelabs.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * @author Bill Meyer
 */
public class VerifySauceConnect
{
    private WebDriver driver;
    protected String userName = System.getenv("SAUCE_USERNAME");
    protected String accessKey = System.getenv("SAUCE_ACCESS_KEY");

    @Before
    public void setUp()
    throws MalformedURLException
    {
        //construct the DesiredCapabilities using the environment variables set by the Sauce CI plugin
        DesiredCapabilities caps = DesiredCapabilities.edge();
        caps.setCapability("platform", "Windows 10");
        caps.setCapability("version", "14.14393");
        caps.setCapability("recordVideo", "true");
        caps.setCapability("recordScreenshots", "true");
        caps.setCapability("screenResolution", "1280x800");

        caps.setCapability("username", userName);
        caps.setCapability("accesskey", accessKey);

        Date startDate = new Date();
        caps.setCapability("name", String.format("%s - %s [%s]", this.getClass().getSimpleName(), caps.getBrowserName(), startDate));

        URL url = new URL("https://ondemand.saucelabs.com:443/wd/hub");
        driver = new RemoteWebDriver(url, caps);
    }

    @After
    public void tearDown()
    {
        driver.quit();
    }

    /**
     * Start a web server locally, and have Sauce OnDemand connect to the local server.
     */
    @Test
    public void fullRun()
    {
        String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();

        System.out.println("SauceOnDemandSessionID=" + sessionId);
        driver.get("http://localhost:8080/verify-sauce-connect/snoop.jsp");

        // if the server really hit our Jetty, we should see the same title that includes the secret code.
        assertTrue(driver.getPageSource().contains("Cargo Ping Component used to verify if the container is started."));
    }
}
