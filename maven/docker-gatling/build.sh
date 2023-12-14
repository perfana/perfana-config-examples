#!/usr/bin/env bash

set -o errexit

VERSION="0.0.2"

# use "local" or "remote" as first parameter to build locally or push to docker hub
local_or_remote="${1:-local}"

if [ "$local_or_remote" == "remote" ]; then
    echo "Building for docker hub."
    docker buildx build --platform linux/amd64,linux/arm64 -t perfana/maven-gatling-loadtest:$VERSION --push .
else
    echo "Building locally."
    docker build -t perfana/maven-gatling-loadtest:$VERSION-SNAPSHOT .
fi
