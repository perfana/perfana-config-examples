#!/bin/bash

if [ ! -f files/pom.xml ]
then
    echo "Generate pom.xml for a k6  run first, check README.md"
    exit 1
fi

if [ ! -f files/k2i ] 
then
    wget -O files/k2i -q "https://github.com/perfana/gatling-to-influxdb/releases/download/k2i-0.0.1/k2i"
fi
chmod u+x files/k2i

docker build -t my-maven-loadtest:v0.2 .
