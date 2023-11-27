# Maven

Run a load test and connect to Perfana via Maven.

Generate a `pom.xml` for jMeter, K6 or Gatling.

## Docker image for jMeter

Build a docker image for jMeter and Maven in the `docker-jmeter` directory using `build.sh`.
It builds an image with all needed files and config inside.

Run in a cluster as:

    kubectl run -it --rm --restart=Never --image maven-jmeter-loadtest:v0.1 loadtest -- sh
    
At the shell command, type `mvn verify` to start the load test.
Or replace `sh` with `mvn verify` to run directly.

### Steps

To generate the pom.xml, package the image and run it in a k3d cluster:

```sh
export PERFANA_CONFIG_EXAMPLES_DIR=<path>
export PERFANA_CLIENT=acme
export TEMPLATE_DIR=$PERFANA_CONFIG_EXAMPLES_DIR/template
export PERFANA_API_KEY=<perfana-api-key>
export LOAD_TEST_TOOL=jmeter
$PERFANA_CONFIG_EXAMPLES_DIR/template/transform.kts pom > files/pom.xml
./build.sh
k3d image import maven-jmeter-loadtest:v0.1 -c acme
kubectl run -it --rm --restart=Never --image maven-jmeter-loadtest:v0.1 loadtest -- mvn verify
```

## Docker image for Gatling

In the `docker-gatling` directory a complete docker image can be build to run load tests with
Gatling.

Run in a cluster as:

    kubectl run -it --rm --restart=Never --image maven-gatling-loadtest:v0.1 loadtest -- sh

Start with `mvn events-gatling:test`.

## Docker image for K6

In the `docker-k6` directory a complete docker image can be build to run load tests with
K6.

Run in a cluster as:

    kubectl run -it --rm --restart=Never --image maven-k6-loadtest:v0.1 loadtest -- sh

Start with `mvn event-scheduler:test`.

### Steps

Same steps as the jMeter run, only change `LOAD_TEST_TOOL=jmeter` to `LOAD_TEST_TOOL=gatling`

## Upload image into cluster

For a k3d cluster, in this example called `acme`, use this command to upload the image:

   k3d image import maven-jmeter-loadtest:v0.1 -c acme
