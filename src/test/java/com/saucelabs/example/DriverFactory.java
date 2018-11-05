package com.saucelabs.example;

import com.google.gson.Gson;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DriverFactory
{
    protected static final String userName = System.getenv("SAUCE_USERNAME");
    protected static final String accessKey = System.getenv("SAUCE_ACCESS_KEY");
    protected static final String toAccessKey = System.getenv("TESTOBJECT_API_KEY");

    public static RemoteWebDriver getDriverInstance(String browser, String version)
    {
        return getDriverInstance(browser, version, "");
    }

    public static RemoteWebDriver getDriverInstance(String browser, String version, String platform)
    {
        return getDriverInstance(browser, version, platform, null);
    }

    public static RemoteWebDriver getDriverInstance(String browser, String version, String platform, MutableCapabilities addlCaps)
    {
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("browserName", browser);
        caps.setCapability("platform", platform);
        caps.setCapability("version", version);

        Date startDate = new Date();
        caps.setCapability("name", "Verify Sauce Connect");

        RemoteWebDriver driver;

        if (Util.runLocal)
        {
            String browserName = caps.getBrowserName();
            switch (browserName)
            {
                case BrowserType.CHROME:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.merge(caps);
                    if (addlCaps != null) chromeOptions.merge(addlCaps);
                    driver = new ChromeDriver(chromeOptions);
                    break;

                case BrowserType.EDGE:
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.merge(caps);
                    if (addlCaps != null) edgeOptions.merge(addlCaps);
                    driver = new EdgeDriver(edgeOptions);
                    break;

                case BrowserType.FIREFOX:
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.merge(caps);
                    if (addlCaps != null) firefoxOptions.merge(addlCaps);
                    driver = new FirefoxDriver(firefoxOptions);
                    break;

                case BrowserType.IE:
                    InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                    ieOptions.merge(caps);
                    if (addlCaps != null) ieOptions.merge(addlCaps);
                    driver = new InternetExplorerDriver(ieOptions);
                    break;

                case BrowserType.SAFARI:
                    SafariOptions safariOptions = new SafariOptions();
                    safariOptions.merge(caps);
                    if (addlCaps != null) safariOptions.merge(addlCaps);
                    driver = new SafariDriver(safariOptions);
                    break;

                default:
                    throw new RuntimeException("Unsupported browser: " + browserName);
            }
        }
        else
        {
            // See https://wiki.saucelabs.com/display/DOCS/Test+Configuration+Options for all capabilities...
            caps.setCapability("username", userName);
            caps.setCapability("accesskey", accessKey);
            caps.setCapability("tags", "QATeam");
            caps.setCapability("recordVideo", "true");
            caps.setCapability("recordScreenshots", "true");
            caps.setCapability("screenResolution", "1600x1200");
            caps.setCapability("recordMp4", "true");

            if (addlCaps != null)
                caps.merge(addlCaps);

            URL url = null;
            try
            {
                // For local testing...
//                url = new URL("http://localhost:4444/wd/hub");

                // For connecting to Sauce Labs...
                url = new URL("https://ondemand.saucelabs.com:443/wd/hub");
            }
            catch (MalformedURLException ignored)
            {
            }
            driver = new RemoteWebDriver(url, caps);
        }

        String sessionId = driver.getSessionId().toString();
        Util.log("Started %s, session ID=%s.\n", new Date().toString(), sessionId);

        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);

        return driver;
    }

    public static RemoteWebDriver getMobileEmulationDriverInstance(String deviceName)
    {
        ChromeOptions options = new ChromeOptions();
        options.setCapability("platform", "macOS 10.13");
        options.setCapability("version", "65");

        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", deviceName);
        options.setExperimentalOption("mobileEmulation", mobileEmulation);

        Date startDate = new Date();
        options.setCapability("name", String.format("Verify Sauce Connect - %s [%s]", options.getBrowserName(), startDate));

        RemoteWebDriver driver = new ChromeDriver(options);

        String sessionId = driver.getSessionId().toString();
        Util.log("Started %s, session ID=%s.\n", new Date().toString(), sessionId);

        // Need page load timeout because of lingering request to https://pixel.jumptap.com taking 78+ seconds
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);

        return driver;
    }

    public static RemoteWebDriver getMobileDriverInstance(String deviceName, String platformName, String platformVersion)
    {
        return getMobileDriverInstance(deviceName, platformName, platformVersion, null);
    }

    public static RemoteWebDriver getMobileDriverInstance(String deviceName, String platformName, String platformVersion, MutableCapabilities addlCaps)
    {
        RemoteWebDriver driver;

        Util.isMobile = true;

        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("deviceName", deviceName);
        caps.setCapability("platformName", platformName);
        caps.setCapability("platformVersion", platformVersion);

        if (addlCaps != null)
            caps.merge(addlCaps);

        if (Util.runLocal == true)
        {
            URL url = null;
            try
            {
                url = new URL("http://127.0.0.1:4723/wd/hub");
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }

            switch (platformName)
            {
                case BrowserType.ANDROID:
                    caps.setCapability("browserName", "Chrome");
                    driver = new AndroidDriver(url, caps);
                    break;

                case "iOS":
                    caps.setCapability("browserName", "Safari");
                    caps.setCapability("automationName", "XCUITest");
                    caps.setCapability("xcodeOrgId", "5A5HY5JGF5");
                    caps.setCapability("xcodeSigningId", "iPhone Developer");
                    caps.setCapability("wdaLocalPost", "8100");
                    caps.setCapability("port", "4723");

                    driver = new IOSDriver(url, caps);
                    break;

                default:
                    return null;
            }
        }
        else
        {
            caps.setCapability("testobject_api_key", toAccessKey);
            caps.setCapability("appiumVersion", "1.8.0");
            caps.setCapability("deviceOrientation", "portrait");
            caps.setCapability("recordVideo", "true");
            caps.setCapability("recordScreenshots", "true");
            caps.setCapability("recordMp4", "true");

            URL url = null;
            try
            {
                url = new URL("https://us1.appium.testobject.com/wd/hub");
            }
            catch (MalformedURLException ignored)
            {
            }

            switch (platformName)
            {
                case BrowserType.ANDROID:
                    caps.setCapability("browserName", "Chrome");
                    driver = new AndroidDriver(url, caps);
                    break;

                case BrowserType.IPAD:
                case BrowserType.IPHONE:
                    caps.setCapability("browserName", "Safari");
                    driver = new IOSDriver(url, caps);
                    break;

                default:
                    return null;
            }
        }

        String sessionId = driver.getSessionId().toString();
        Util.log("Started %s, session ID=%s.\n", new Date().toString(), sessionId);

        // Need page load timeout because of lingering request to https://pixel.jumptap.com taking 78+ seconds
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);

        String jsonCaps = new Gson().toJson(driver.getCapabilities().asMap());
        Util.log("Capabilities: %s\n", jsonCaps);

        return driver;
    }
}
