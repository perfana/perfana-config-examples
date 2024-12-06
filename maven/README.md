# Maven

Run a load test and connect to Perfana via Maven.

Create a `pom.xml` for jMeter, K6 or Gatling.

## Basic setup

The following directories contain a basic setup for running a load test with Perfana:

* [k6](k6/README.md)
* Gatling - TODO
* jMeter - TODO

## Docker images

To run in docker or in a cluster, build the docker images in the respective directories.

### jMeter

The jmeter image contains maven, j2i and pre-loaded dependencies for jMeter.

Run as docker image with mapping the pom.xml and jmeter script files:

    docker run -it --rm --name loadtest --entrypoint sh \
    -v $(pwd)/pom.xml:/loadtest/pom.xml \
    -v $(pwd)/src:/loadtest/src \
    perfana/maven-jmeter-loadtest:0.0.5

Run in a kubernetes as demonstrated in `loadtest-example.yaml` file.

At the shell command, type `mvn verify` to start the load test.
Or replace `sh` with `mvn verify` to run directly.

Build a new docker image for jMeter and Maven in the `docker-jmeter` directory using `build.sh`.

#### Steps

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

### Gatling

In the `docker-gatling` directory a complete docker image can be build to run load tests with
Gatling.

Run in a cluster as:

    kubectl run -it --rm --restart=Never --image maven-gatling-loadtest:v0.1 loadtest -- sh

Start with `mvn events-gatling:test`.

### K6

In the `docker-k6` directory a complete docker image can be build to run load tests with
K6.

Run in a cluster as:

    kubectl run -it --rm --restart=Never --image maven-k6-loadtest:v0.1 loadtest -- sh

Start with `mvn event-scheduler:test`.

#### Steps

Same steps as the jMeter run, only change `LOAD_TEST_TOOL=jmeter` to `LOAD_TEST_TOOL=gatling`

## Upload image into cluster

For a k3d cluster, in this example called `acme`, use this command to upload the image:

   k3d image import maven-jmeter-loadtest:v0.1 -c acme
