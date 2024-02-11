#!/bin/bash

set -o errexit

ZIP_FILE=$1
TARGET_DIR=${2:-/loadtest}

UNZIP_DIR=unzip

# Define cleanup procedure
cleanup() {
    echo "Cleaning up..."
    [ -d "$UNZIP_DIR" ] && rm -rf "$UNZIP_DIR"
}

trap cleanup EXIT

echo "generating config from $ZIP_FILE"
echo "current dir: $(pwd)"

mkdir -p "$TARGET_DIR"

# -j is for junk paths so no subdirs are created
unzip -j -o "$ZIP_FILE" -d $UNZIP_DIR

source $UNZIP_DIR/setup.sh

# override some settings for local generation
export HOMEDIR=/files

export PERFANA_CONFIG_EXAMPLES_DIR=$HOMEDIR/perfana-config-examples
export TEMPLATE_DIR=$PERFANA_CONFIG_EXAMPLES_DIR/template

export PSG_TLS_KEY=$UNZIP_DIR/tls.key
export PSG_TLS_CRT=$UNZIP_DIR/tls.crt

cp $TEMPLATE_DIR/SETUP.md "$TARGET_DIR"

# generate config
$TEMPLATE_DIR/transform.kts psg > "$TARGET_DIR"/psg-values.yaml
$TEMPLATE_DIR/transform.kts otel-collector > "$TARGET_DIR"/otel-collector-gen.yaml
$TEMPLATE_DIR/transform.kts pom > "$TARGET_DIR"/pom.xml
$TEMPLATE_DIR/transform.kts afterburner > "$TARGET_DIR"/afterburner.yaml
$TEMPLATE_DIR/transform.kts metrics > "$TARGET_DIR"/grafana-agent.yaml
$TEMPLATE_DIR/transform.kts loadtest > "$TARGET_DIR"/loadtest.yaml
$TEMPLATE_DIR/transform.kts start > "$TARGET_DIR"/start-cluster.sh
chmod u+x "$TARGET_DIR"/start-cluster.sh
$TEMPLATE_DIR/transform.kts stop > "$TARGET_DIR"/stop-cluster.sh
chmod u+x "$TARGET_DIR"/stop-cluster.sh

# this is default load script, later generate one based on open-api spec
LOAD_SCRIPT_DIR=$PERFANA_CONFIG_EXAMPLES_DIR/maven/docker-$LOAD_TEST_TOOL/load-script/src
tar -C "$(dirname $LOAD_SCRIPT_DIR)" -cvzf "$TARGET_DIR"/loadtest-"$LOAD_TEST_TOOL".tar.gz "$(basename $LOAD_SCRIPT_DIR)"
