# perfana-config-generator

Docker image to easily generate Perfana configuration files.

# Run

    docker run --rm -d -p 9600:8080 --name perfana-config-gen perfana/perfana-config-gen:0.0.3

Browse to http://localhost:9600 to use the application or use the REST API with curl as described below.

Run without `-d` to see logging and stop with `ctrl-c`.

To stop and remove this container:

    docker stop perfana-config-gen

# Create config files

Upload the perfana-package.zip received from Perfana. 
This zip package contains the basic configuration files, keys and certificates.

    curl -F "file=@perfana-package.zip" http://localhost:9600/upload

You get a reply like the following:

    {"projectId":"perfana-starter-231208-125614","downloadUrl":"/download/perfana-starter-231208-125614"}
    
You can use that `downloadUrl` to download the resulting zip file:

    curl -sS --remote-header-name -O localhost:9600/download/perfana-starter-231208-125614

Unzip the file:

    unzip perfana-starter-231208-125614.zip

Now run the script to run everything in your (local) cluster.

    cd perfana-starter-231208-125614   
    chmod u+x *.sh
    setup-cluster.sh

Connect to local running Afterburner:

    time curl -v -X GET localhost:8080/delay

If needed, point to local kubernetes cluster, for k3d:

    export KUBECONFIG="$(k3d kubeconfig write $CLIENT_NAME)"

Start a load test in the loadtest container that is part of the setup:

    kubectl -n perfana-starter exec -it deploy/loadtest -- mvn events-gatling:test

To remove the local cluster:

    stop-cluster.sh

# References

* afterburner: https://github.com/perfana/afterburner
* jmeter-generator: https://github.com/stokpop/jmeter-generator
* wiremock-generator: https://github.com/stokpop/wiremock-generator
* openapi-generator: https://github.com/stokpop/openapi-generator

