# Github Actions

The Github actions starts a Perfana test run via Maven.

In the current format, the run can be started via the Github Action UI.
A popup is displayed with input values for the test run.

Other ways to run from Github action is via triggers on git commits for instance.

Add the needed secrets to the Github Action secrets:

* PERFANA_APIKEY - the Perfana API key as added via the Perfana web interface 
* INFLUXDB_USER - to store data in Influx - use your own Influx or write to the PSG
* INFLUXDB_PASSWORD - to store data in Influx 
* (optional) KUBECONFIG_TEST - a kubeconfig for the test environment --- base64 encoded
* (optional) ELASTIC_PASSWORD - only if elastic is in use

The j2i (jmeter2influx) tool is downloaded to be send raw data to Influx.

The commit message is fetched and used as annotation to the test file.

The `testRunId` is not really needed. Preferably use the Perfana generated test run id.
