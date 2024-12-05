# otel-collector 

Use the otel-collector to send metrics and distributed traces to Perfana cloud using mTLS.

The otel-collector can also be used to scrape metrics from Prometheus endspoints, 
like the SpringBoot `/actuator/prometheus` endpoint and send them to Perfana cloud.

## Kubernetes

Basic installation of otel-collector in a Kubernetes cluster.

This configuration receives Zipkin on port 9411 (e.g. from SpringBoot Sleuth with Zipkin) and sends them along as oltp-grpc.

Sends the `perfana-request-name` and `perfana-test-run-id` http headers along as metadata (baggage).

Based on [yaml](https://raw.githubusercontent.com/open-telemetry/opentelemetry-collector/main/examples/k8s/otel-config.yaml)

To send data to Perfana cloud, add your account name in place of `acme`:

    x-scope-orgid: acme

You need your key and certificate from Perfana to send data to Perfana cloud.
They are mounted secrets mapped to `/secrets`:

    tls:
      insecure: false
      cert_file: /secrets/tls.crt
      key_file: /secrets/tls.key

You will find these inside the starter package you can download when you log into Perfana Cloud.

To install (make sure you are in the context of the appropriate cluster and namespace):

```shell
kubectl apply -f otel-collector.yaml
```

To remove:

```shell
kubectl delete -f otel-collector.yaml
```

## Stand alone

To run the otel-collector stand alone (there might be newer versions available):

```shell
curl -OL https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.114.0/otelcol-contrib_0.114.0_darwin_arm64.tar.gz
tar -xzf otelcol-contrib_0.114.0_darwin_arm64.tar.gz
cd otelcol-contrib_0.114.0_darwin_arm64
./otelcol-contrib --config=file:otel-local-config.yml
```

