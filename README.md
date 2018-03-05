# Verify Sauce Connect

This sample project can be used as a starting point for testing connectivity from the Sauce Labs cloud to your private network via Sauce Connect.

## Getting Started

1. Start the Sauce Connect proxy

    Download the latest version of Sauce Connect.  For instructions on how to download and start Sauce Connect, visit this link: https://wiki.saucelabs.com/display/DOCS/Basic+Sauce+Connect+Proxy+Setup.

    Start the Sauce Connect proxy and wait for the message indicating the tunnel is connected and ready for tests:

    ```
    $ sc --user=${SAUCE_USERNAME} --api-key=${SAUCE_ACCESS_KEY}
    5 Mar 13:10:49 - Sauce Connect 4.4.12, build 3905 74cd761 -dirty
    5 Mar 13:10:49 - Using CA certificate bundle /etc/ssl/certs/ca-bundle.crt.
    5 Mar 13:10:49 - Using CA certificate verify path /etc/ssl/certs.
    5 Mar 13:10:49 - *** WARNING: open file limit 1024 is too low!
    5 Mar 13:10:49 - *** Sauce Labs recommends setting it to at least 8000.
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

2. Start the include sample website that will be used to test connectivity from Sauce Labs to this network segment:

    ```bash
    $ mvn clean verify org.codehaus.cargo:cargo-maven2-plugin:run
    ```

3. Sign into [saucelabs.com]() and start a **Live Testing** session and navigate to your hostname and this specific URI:

    ```
        http://<yourhostname>:8080/verify-sauce-connect
    ```
    
    ![](result.png)