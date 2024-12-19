# Spring Boot

To activate additional metrics to be used in analysis.

## Actuator Prometheus

Activate the Prometheus scrape pages. Via the `application.properties`:

```
management.endpoint.metrics.enabled=true
management.endpoints.web.exposure.include=*
management.endpoint.prometheus.enabled=true
management.metrics.export.prometheus.enabled=true
```

Also add tags to the metrics so they can be identified in Perfana.
These are: `system_under_test`, `test_environment` and `service`.

The system under test and test environment should match the tags in the Perfana test run.
There can be multiple services, for instance two SpringBoot services in one load test.

Example:
```
management.metrics.tags.system_under_test=tiny-bank
management.metrics.tags.test_environment=silver
management.metrics.tags.service=tiny-bank-service
```

Check with:

    curl -sS [host:port]/actuator/prometheus

## Connection pool

When using Apache HTTP connection pool, we recommend to activate additional metrics for the HTTP connection pool.
See: [activate connection pool metrics](activate-connection-pool-metrics.md)

Enable metrics for actual calls.
See: [add connection pool calls to actuator](add-connection-pool-calls-to-actuator.md)

## Distributed tracing

To activate distributed tracing via Sleuth, config and code changes need to made to SpringBoot 2.x applications.

See: [add baggage to http headers](add-baggage-to-http-headers.md)


