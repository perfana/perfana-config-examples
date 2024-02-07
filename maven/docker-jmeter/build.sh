#!/bin/bash

set -o errexit

VERSION="0.0.3"

# use "local" or "remote" as first parameter to build locally or push to docker hub
local_or_remote="${1:-local}"

if [ "$local_or_remote" == "remote" ]; then
    echo "Building for docker hub."
    docker buildx build --no-cache --platform linux/amd64,linux/arm64 -t perfana/maven-jmeter-loadtest:$VERSION --push .
else
    echo "Building locally."
    docker build -t perfana/maven-jmeter-loadtest:$VERSION-SNAPSHOT .
fi

