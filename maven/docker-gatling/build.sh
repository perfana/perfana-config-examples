#!/bin/bash

if [ ! -f files/pom.xml ]
then
    echo "Generate pom.xml for a Gatling run first, check README.md"
    exit 1
fi

if [ ! -f files/g2i ] 
then
    wget -O files/g2i -q "https://github.com/perfana/gatling-to-influxdb/releases/download/g2i-1.0.0/g2i"
    chmod u+x files/g2i
fi

docker build -t my-maven-loadtest:v0.2 .
