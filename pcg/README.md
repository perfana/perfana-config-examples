# perfana-config-generator

Docker image to easily generate Perfana configuration files.

# Run

    docker run --rm -d -p 9600:8080 --name perfana-config-gen perfana/perfana-config-gen:0.0.1

Run without `-d` to see logging and stop with `ctrl-c`.

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

Start a load test, follow steps to build your own load test image first.

    k3d image import maven-gatling-loadtest:v0.1 -c <cluster-name>
    kubectl run -it --rm --restart=Never --image maven-gatling-loadtest:v0.1 loadtest -- mvn events-gatling:test

To remove the local cluster:

    stop-cluster.sh

# References

* afterburner: https://github.com/perfana/afterburner
* jmeter-generator: https://github.com/stokpop/jmeter-generator
* wiremock-generator: https://github.com/stokpop/wiremock-generator
* openapi-generator: https://github.com/stokpop/openapi-generator

