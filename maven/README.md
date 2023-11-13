# Maven

Run a load test and connect to Perfana via Maven.

Generate a `pom.xml` for jMeter or Gatling.

## Docker image for jMeter

Build a docker image for jMeter and Maven in the `docker-jmeter` directory.
It builds an image with all needed files and config inside.

Run in a cluster as:

    kubectl run -it --rm --restart=Never --image my-maven-loadtest:v0.2 loadtest -- sh
    
At the shell command, type `mvn verify` to start the load test.
Or replace `sh` with `mvn verify` to run directly.

### Steps

To generate the pom.xml, package the image and run it in a k3d cluster:

```sh
export PERFANA_CONFIG_EXAMPLES_DIR=<path>
export PERFANA_CLIENT=mycompanyx
export TEMPLATE_DIR=$PERFANA_CONFIG_EXAMPLES_DIR/template
export PERFANA_API_KEY=<perfana-api-key>
export LOAD_TEST_TOOL=jmeter
$PERFANA_CONFIG_EXAMPLES_DIR/template/transform.kts pom > files/pom.xml
./build.sh
k3d image import my-maven-loadtest:v0.2 -c companyx
kubectl run -it --rm --restart=Never --image my-maven-loadtest:v0.2 loadtest -- mvn verify
```

## Docker image for Gatling

In the `docker-gatling` directory a complete docker image can be build to run load tests with
Gatling.

Run in a cluster as:

    kubectl run -it --rm --restart=Never --image my-maven-loadtest:v0.2 loadtest -- sh

Start with `mvn events-gatling:test`.

### Steps

Same steps as the jMeter run, only change `LOAD_TEST_TOOL=jmeter` to `LOAD_TEST_TOOL=gatling`

## Upload image into cluster

For a k3d cluster, in this example called `companyx`, use this command to upload the image:

   k3d image import my-maven-loadtest:v0.2 -c companyx
