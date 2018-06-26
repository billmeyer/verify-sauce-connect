package com.saucelabs.example;

import com.saucelabs.example.pages.VerifySauceConnectPage;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

/**
 * @author Bill Meyer
 */
public class VerifySauceConnectTest
{
    protected final String userName = Util.getenv("SAUCE_USERNAME");
    protected final String accessKey = Util.getenv("SAUCE_ACCESS_KEY");
    protected final String sauceTunnelId = Util.getenv("SAUCE_TUNNEL_ID");
    protected final String rdcTunnelId = Util.getenv("RDC_TUNNEL_ID");
    protected final String internalTestHost = Util.getenv("INTERNAL_TEST_HOST", "localhost");
    protected final String internalTestPort = Util.getenv("INTERNAL_TEST_PORT", "8080");
    protected final String requestUrl = String.format("http://%s:%s/verify-sauce-connect/index.jsp", internalTestHost, internalTestPort);

    @BeforeClass
    public void setUp()
    throws MalformedURLException
    {
    }

    @AfterClass
    public void tearDown()
    {
    }

    /**
     * Start a web server locally, and have Sauce OnDemand connect to the local server.
     */
    @Test
    public void testDesktop()
    {
        MutableCapabilities addlCaps = new MutableCapabilities();

        if (sauceTunnelId != null)
        {
            addlCaps.setCapability("tunnelIdentifier", sauceTunnelId);
        }

        RemoteWebDriver driver = DriverFactory.getDriverInstance(BrowserType.EDGE, "14.14393", "Windows 10", addlCaps);
        execTest(driver);
    }

    //    @Test
    public void testIOS()
    {
        MutableCapabilities addlCaps = new MutableCapabilities();

        if (sauceTunnelId != null)
        {
            addlCaps.setCapability("tunnelIdentifier", rdcTunnelId);
        }

        RemoteWebDriver driver = DriverFactory.getMobileDriverInstance("iPhone 6", BrowserType.IPHONE, "11.3", addlCaps);
        execTest(driver);
    }

    //    @Test
    public void testAndroid()
    {
        MutableCapabilities addlCaps = new MutableCapabilities();

        if (sauceTunnelId != null)
        {
            addlCaps.setCapability("tunnelIdentifier", rdcTunnelId);
        }

        RemoteWebDriver driver = DriverFactory.getMobileDriverInstance("Google Pixel 2 XL", BrowserType.ANDROID, "8.1", addlCaps);
        execTest(driver);
    }

    private void execTest(RemoteWebDriver driver)
    {
        VerifySauceConnectPage verifySauceConnectPage = new VerifySauceConnectPage(driver);

        String sessionId = driver.getSessionId().toString();
        System.out.println("SauceOnDemandSessionID=" + sessionId);

        driver.get(requestUrl);

        String fromPageRequestedURL = verifySauceConnectPage.getFromPageRequestedURL();

        // if the server really hit our Jetty, we should see the same title that includes the secret code.
        Assert.assertEquals(fromPageRequestedURL, requestUrl);

        if (fromPageRequestedURL.equals(requestUrl))
        {
            System.out.println("We successfully accessed our internal test page!");
            Util.reportSauceLabsResult(driver, true);
        }
        else
        {
            Util.reportSauceLabsResult(driver, false);
        }

        driver.quit();
    }
}
