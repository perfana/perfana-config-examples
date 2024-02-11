#!/usr/bin/env bash

set -o errexit

VERSION="0.0.6"

# Define cleanup procedure
cleanup() {
    echo "Cleaning up..."
    [ -f app.jar ] && rm -v app.jar
    [ -d files ] && rm -rf files
    echo "Done."
}

trap cleanup EXIT

# use "local" or "remote" as first parameter to build locally or push to docker hub
local_or_remote="${1:-local}"

JAVA_VER=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | sed '/^1\./s///' | cut -d'.' -f1)
if [ "$JAVA_VER" != "21" ]; then echo "ERROR: use java 21 instead of $JAVA_VER"; exit 1; fi

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
echo "$SCRIPT_DIR"
PCG_DIR=$(dirname "$SCRIPT_DIR")

mkdir -p "$SCRIPT_DIR/files"
cp -v "$SCRIPT_DIR/generate-config.sh" "$SCRIPT_DIR/files"
rsync -a --exclude-from=exclude-list "$PCG_DIR/.." "$SCRIPT_DIR/files/perfana-config-examples"

cd "$PCG_DIR" || exit
"./mvnw" clean package
cp "$PCG_DIR/target/perfana-config-generator-$VERSION.jar" "$SCRIPT_DIR/app.jar"

cd "$SCRIPT_DIR" || exit
if [ "$local_or_remote" == "remote" ]; then
    echo "Building for docker hub."
    docker buildx build --platform linux/amd64,linux/arm64 -t perfana/perfana-config-gen:$VERSION --push .
else
    echo "Building locally."
    docker build -t perfana/perfana-config-gen:$VERSION-SNAPSHOT .
fi

