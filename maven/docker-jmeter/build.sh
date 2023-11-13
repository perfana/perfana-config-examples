#!/bin/bash

if [ ! -f files/pom.xml ]
then
    echo "Generate pom.xml for a jMeter run first, check README.md"
    exit 1
fi

if [ ! -f files/j2i ] 
then
    wget -O files/j2i -q "https://github.com/perfana/gatling-to-influxdb/releases/download/j2i-1.0.0/j2i"
fi
chmod u+x files/j2i

docker build -t my-maven-loadtest:v0.2 .
