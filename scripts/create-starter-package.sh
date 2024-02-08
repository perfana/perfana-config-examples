#!/usr/bin/env bash

set -e

if [ "$#" -ne 1 ];  then
    echo "Usage: $0 CONFIG_DIR"
    exit 1
fi

CONFIG_DIR=$1
VAULT_ADDR=https://vault.perfana.cloud

if [ ! -d "$CONFIG_DIR" ]; then
    echo "Error: $CONFIG_DIR is not a valid directory."
    exit 1
fi
if [ ! -f $CONFIG_DIR/setup.sh ]; then
    echo "Error: $CONFIG_DIR is not a valid configuration directory. There is no 'setup.sh'"
    exit 1
fi

source $CONFIG_DIR/setup.sh

if [[ ! -f $CONFIG_DIR/tls.crt ]]; then
  echo "Generating mTLS cert and key..."
  openssl req -new -newkey rsa:4096 -nodes -keyout $CONFIG_DIR/tls.key -out $CONFIG_DIR/csr.pem -subj '/CN=perfana-mtls-$PERFANA_CLIENT'
  vault write --field certificate shared/client/$PERFANA_CLIENT/sign/mtls ttl=900h csr=@$CONFIG_DIR/csr.pem > tls.crt
fi

zip -r $CONFIG_DIR.zip $CONFIG_DIR

echo "Generating the Perfana starter package for $PERFANA_CLIENT..."
PACKAGE_NAME=`curl -X POST -F file=@$CONFIG_DIR.zip http://localhost:9600/upload`
curl -sS --remote-header-name -O localhost:9600/download/$$PACKAGE_NAME
