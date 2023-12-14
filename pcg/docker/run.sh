#!/bin/bash
docker run --rm -d -p 9600:8080 --name perfana-config-gen perfana/perfana-config-gen:0.0.2
