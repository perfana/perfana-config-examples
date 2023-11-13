# inject jfr-exporter agent

To inject a JFR agent into a container with a Java process, apply this patch on the deployment.

    kubectl -n default patch deployments/afterburner --patch-file=jfr-patch.yaml
    
An init-container is used to download the jfr-exporter jar and mount it into the target container.
