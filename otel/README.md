# Open Telemetry agent

Use an Open Telemetry agent to send traces and metrics.

## Java

For Java applications, place the otel agent jar on the classpath.

See example Kubernetes patch `ole-patch.yaml` that download the jar via an initcontainer and
places it on the startup command.

Use via:

    kubectl -n my-app patch deployments/carts --patch-file=otel-patch.yaml

    