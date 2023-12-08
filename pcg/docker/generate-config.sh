#!/bin/bash

set -o errexit

ZIP_FILE=$1
TARGET_DIR=$2

UNZIP_DIR=unzip

echo "generating config from $ZIP_FILE"
echo "current dir: $(pwd)"

mkdir -p $TARGET_DIR

unzip -j -o $ZIP_FILE -d $UNZIP_DIR

source unzip/setup.sh

# override some settings for local generation
export HOMEDIR=/files

export PERFANA_CONFIG_EXAMPLES_DIR=$HOMEDIR/perfana-config-examples
export TEMPLATE_DIR=$PERFANA_CONFIG_EXAMPLES_DIR/template

export PSG_TLS_KEY=$UNZIP_DIR/tls.key
export PSG_TLS_CRT=$UNZIP_DIR/tls.crt

# generate config
$TEMPLATE_DIR/transform.kts psg > $TARGET_DIR/psg-values.yaml
$TEMPLATE_DIR/transform.kts otel-collector > $TARGET_DIR/otel-collector-gen.yaml
$TEMPLATE_DIR/transform.kts pom > $TARGET_DIR/pom.xml
$TEMPLATE_DIR/transform.kts afterburner > $TARGET_DIR/afterburner.yaml
$TEMPLATE_DIR/transform.kts metrics > $TARGET_DIR/grafana-agent.yaml
$TEMPLATE_DIR/transform.kts start > $TARGET_DIR/start-cluster.sh
chmod u+x $TARGET_DIR/start-cluster.sh
$TEMPLATE_DIR/transform.kts stop > $TARGET_DIR/stop-cluster.sh
chmod u+x $TARGET_DIR/stop-cluster.sh
