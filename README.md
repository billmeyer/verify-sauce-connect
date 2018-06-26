# Verify Sauce Connect

This sample project can be used as a starting point for testing connectivity from the Sauce Labs cloud to your private network via Sauce Connect.

## Getting Started

### Prerequisites

Follow the instructions located at https://wiki.saucelabs.com/display/DOCS/Basic+Sauce+Connect+Proxy+Setup.  Most importantly, this link will provide instruction for:

 * Whitelisting the appropriate IP address ranges for the Sauce Cloud,
 * Downloading the most current release of Sauce Connect.
 
Once the IP white lists are put in place and Sauce Connect is downloaded, proceed to the next section.  

### Start Sauce Connect
    
1. Start the Sauce Connect proxy

    To start the Sauce Connect proxy, use the following command:

    ```
    $ sc --user=${SAUCE_USERNAME} --api-key=${SAUCE_ACCESS_KEY} --tunnel-identifier mytunnel1
    ```

    where:
    
    * `${SAUCE_USERNAME}` is username (or email address) user to sign into the Sauce Labs dashboard,
    * `${SAUCE_ACCESS_KEY}` is the access key.  This can be found by signing into https://saucelabs.com, clicking on your user name in the top right corner, selecting __My Account__.  The __Access Key__ is found about half way down the web page,
    * `mytunnel1` is a user-friendly name to give the tunnel.

2. Wait for the tunnel to completely start

    The Sauce Connect start up will be complete when you see `Sauce Connect is up, you may start your tests.` in the output:

    ```
    $ sc --user=${SAUCE_USERNAME} --api-key=${SAUCE_ACCESS_KEY} --tunnel-identifier mytunnel1
    5 Mar 13:10:49 - Sauce Connect 4.4.12, build 3905 74cd761 -dirty
    5 Mar 13:10:49 - Using CA certificate bundle /etc/ssl/certs/ca-bundle.crt.
    5 Mar 13:10:49 - Using CA certificate verify path /etc/ssl/certs.
    5 Mar 13:10:49 - Starting up; pid 6199
    5 Mar 13:10:49 - Command line arguments: sc
    5 Mar 13:10:49 - Log file: /tmp/sc.log
    5 Mar 13:10:49 - Pid file: /tmp/sc_client.pid
    5 Mar 13:10:49 - Timezone: CST GMT offset: -6h
    5 Mar 13:10:49 - Using no proxy for connecting to Sauce Labs REST API.
    5 Mar 13:10:54 - Started scproxy on port 38195.
    5 Mar 13:10:54 - Please wait for 'you may start your tests' to start your tests.
    5 Mar 13:11:16 - Secure remote tunnel VM provisioned.
    5 Mar 13:11:16 - Tunnel ID: 105bb7fcf311480791289f85186b1d01
    5 Mar 13:11:16 - Using no proxy for connecting to tunnel VM.
    5 Mar 13:11:16 - Starting Selenium listener...
    5 Mar 13:11:16 - Establishing secure TLS connection to tunnel...
    5 Mar 13:11:16 - Selenium listener started on port 4445.
    5 Mar 13:11:17 - Sauce Connect is up, you may start your tests.
    ```     

### Verify Connectivity with a Sample Web App and Live Testing

This git repo contains a sample web application that can be used to verify connectivity from the device under test on the Sauce Cloud to your private network where the Sauce Connect proxy is running.

To verify Sauce Connect using this sample web app, follow these steps:

1. Package the sample web app into a .war file:

        $ mvn clean install -DskipTests=true
 
2. Start the include sample website that will be used to test connectivity from Sauce Labs to this network segment:

        $ mvn org.codehaus.cargo:cargo-maven2-plugin:run

3. Sign into https://saucelabs.com and start a **Live Testing** session and navigate to your hostname and this specific URI:

    ```
        http://<yourhostname>:8080/verify-sauce-connect
    ```
    
    ![](result.png)

### Verify Connectivity with a Sample Web App and Automated Testing

To enable your test suite to use the Sauce Connect tunnel instance, a capability named `tunnelIdentifier` needs to be added to your Desired Capabilities specifying the tunnel identifier specifyed when starting the tunnel.  In the example above, we specifed a tunnel ID of `mytunnel1`.

Here's an example from the test suite included in this git repo: 

```Java
    capabilities.setCapability("tunnelIdentifier", sauceTunnelId);
```

See `src/test/java/com/saucelabs/example/VerifySauceConnectTest.java` for more details.

The sample test suite can be executed with:

```
    $ mvn test
```

```
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building Verify Sauce Connect Utility 1.0.0
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ verify-sauce-connect ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory /Volumes/Duo1/github/billmeyer/verify-sauce-connect/src/main/resources
[INFO]
[INFO] --- maven-compiler-plugin:3.6.1:compile (default-compile) @ verify-sauce-connect ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ verify-sauce-connect ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory /Volumes/Duo1/github/billmeyer/verify-sauce-connect/src/test/resources
[INFO]
[INFO] --- maven-compiler-plugin:3.6.1:testCompile (default-testCompile) @ verify-sauce-connect ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- maven-surefire-plugin:2.20.1:test (default-test) @ verify-sauce-connect ---
[INFO]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.saucelabs.example.VerifySauceConnectTest
Jun 26, 2018 2:35:57 PM org.openqa.selenium.remote.ProtocolHandshake createSession
INFO: Detected dialect: OSS
[main][String] Tue Jun 26 14:35:57 CDT 2018SauceOnDemandSessionID=f54578acd72d43c9aca4c2492a6d7a63
We successfully accessed our internal test page!
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 25.428 s - in com.saucelabs.example.VerifySauceConnectTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 27.057 s
[INFO] Finished at: 2018-06-26T14:36:02-05:00
[INFO] Final Memory: 19M/773M
[INFO] ------------------------------------------------------------------------
```
