# otel-collector

Basic installation of otel-collector in a Kubernetes cluster.

Use the otel-collector to send distibuted traces to Perfana cloud using mTLS.

This configuration receives Zipkin on port 9411 (e.g. from SpringBoot Sleuth with Zipkin) and sends them along as oltp-grpc.

Sends the `perfana-request-name` and `perfana-test-run-id` http headers along as metadata (baggage).

Based on [yaml](https://raw.githubusercontent.com/open-telemetry/opentelemetry-collector/main/examples/k8s/otel-config.yaml)

To install:

```shell
kubectl apply -f otel-collector.yaml
```

To remove:

```shell
kubectl delete -f otel-collector.yaml
```