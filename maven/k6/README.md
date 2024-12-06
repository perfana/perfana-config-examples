# Basic Maven k6 setup

The `pom.xml` contains a basic setup for running a k6 load script with Perfana.

## Configuration

This setup is for running locally with the Perfana Secure Gateway (PSG) and the OpenTelemetry Collector (otel-collector).

1. Make sure you have a recent version of Maven installed.
2. [Download](https://github.com/perfana/x2i/releases) the most recent `x2i` tool to parse the k6 output and send it to Perfana, put the executable in same directory where you have the Maven `pom.xml`. Make sure it is named `x2i` and the file is executable (`chmod +x x2i`).
3. Put your k6 script in same directory including needed csv files.

Change the `pom.xml` to match your setup:

* add your Perfana API key (either replace `${ENV.PERFANA_API_KEY}` with your Perfana key or set the `PERFANA_API_KEY` environment variable)
* replace the acme part in `influxDb` with the account name you received from Perfana, e.g. <account>_jfr
* replace `systemUnderTest` and `testEnvironment` with your system under test and test environment names
* check the tags, these will make sure dashboards with similar tags are attached to the test run, if you include jfr, configure your sut to contain the jfr-exporter agent
* change the `onStartTest` in the `Run k6 script` CommandRunner to match your k6 script variables, script path/name and sut endpoint.
* in the `ActuatorSpringBootEvent` change the tag to match your sut name and point to the correct actuator endpoint (make sure `actuator/env` endpoint is enabled for this to work)

## Run

To run the test, execute:

```shell
mvn test-scheduler:test
```
