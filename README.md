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

Get access to Perfana cloud. Get a Perfana API key. 

Configure and run Perfana Secure Gateway (PSG).

Generate a `pom.xml` file for target enviroment.

The pom contains tags that are send to Perfana Cloud.
Based on these tags, dashboards are selected for the test runs.
For instance: `jmeter`, `spring-boot` tags.

Data from the load generator is sent to the InfluxDB endpoints of PSG.
We recommend to send raw data using one of the Perfana provided open source
'X2i' tools. These are: j2i, g2i and k2i for jMeter, Gatling and K6.

Data of the Sytem Under Test (SUT) and the environment is sent to the 
Prometheus endpoints of PSG.
Use your existing Prometheus forwarders or install a local Grafana Agent.

Metrics and load generator data needs to be tagged, e.g. with the Perfana test run id
or the name of the SUT.
Make sure this tags are supplied via the SUT metrics scraper. For instance,
in a SpringBoot application add the application name.


# Local cluster

First approach is a local SUT and load test that connect to Perfana cloud.

Steps to start:

1. install k3d
2. create companyx cluster 
3. generate config from templates
4. apply the config to the cluster

To run a load test you can build the docker container in `maven/docker`.
Use the `build.sh` to create a local image.

To run in the cluster:

    k3d image import my-maven-loadtest:v0.2 -c companyx
    kubectl run -it --rm --restart=Never --image my-maven-loadtest:v0.2 loadtest -- mvn verify

