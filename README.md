# perfana-config-examples

This repository contains configuration and code examples to quickly get started with Perfana.

Check the README.md in sub directories for more information.

There are examples for multiple setups. For instance:
* jmeter, gatling or k6
* java, springboot
* jenkins, github actions

First path is jmeter with jenkins and a SpringBoot system under test.
Find a minimal maven pom and related config files.

# High over steps

Get access to Perfana Cloud. Get a Perfana API key plus key and certificate for secure communication.

Configure and run Perfana Secure Gateway (PSG).

Generate a `pom.xml` file for target environment.

The pom contains tags that are send to Perfana Cloud.
Based on these tags, dashboards are selected for the test runs.
For instance: `jmeter`, `spring-boot` tags.

Data from the load generator is sent to the InfluxDB endpoints of PSG.
We recommend to send raw data using one of the Perfana provided open source
'X2i' tools. These are: `j2i`, `g2i` and `k2i` for jMeter, Gatling and K6.

Data of the System Under Test (SUT) and the environment is sent to the 
Prometheus endpoints of PSG.
Use your existing Prometheus forwarders or install a local Grafana Agent.

Metrics and load generator data needs to be tagged, e.g. with the Perfana test run id
or the name of the SUT.
Make sure tags are supplied via the SUT metrics scraper. For instance,
in a SpringBoot application add the application name.


# Local cluster

First approach is a local SUT and load test that connect to Perfana Cloud.

Steps to start:

1. install a k8s cluster, for example [k3d](https://k3d.io/)
2. create acme cluster 
3. generate config from templates
4. apply the config to the cluster

To run a load test you can build the docker container in `maven/docker-$LOADTEST`.
Use the `build.sh` to create a local image.

To run in the cluster:

    k3d image import maven-gatling-loadtest:v0.1 -c acme
    kubectl run -it --rm --restart=Never --image maven-gatling-loadtest:v0.1 loadtest -- mvn events-gatling:test

# Local without cluster

To connect without a cluster run both the `PSG` and the `otel-collector` locally.

Steps:
1. install nginx and otel-collector
2. configure [nginx](psg/README.md) and [otel-collector](otel-collector/README.md) via instructions in the README.md of the respective directories
3. run you local SUT and load test
4. wrap your load test in a Maven script or via code, such as in Java or in Groovy

The last step is needed to register the test run in Perfana Cloud and make sure
the metrics are received and processed _during_ the test run.
