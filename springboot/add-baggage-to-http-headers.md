# add baggage to http headers

In SpringBoot 2.x additional configuration is needed to add bagage to the HTTP headers of Apache HttpClient.

1. add needed dependencies
2. add to code

```java
    // WORKAROUND: should be present automatically: traceHttpClientBuilder, via org.springframework.cloud.sleuth.autoconfig.brave.instrument.web.client.BraveWebClientAutoConfiguration
    // but for some reason not?
    @Bean
    HttpClientBuilder traceHttpClientBuilder(HttpTracing httpTracing) {
        return TracingHttpClientBuilder.create(httpTracing);
    }
```

and use that bean in other http client config setup:

```java
    // Need to inject traceHttpClientBuilder to have spans in http headers
    @Bean
    public CloseableHttpClient createHttpClient(@Qualifier("traceHttpClientBuilder") HttpClientBuilder builder) {

        ...

        CloseableHttpClient httpClient = builder
            .setConnectionManager(connectionManager)
            ...
            .build();

        ...

        return httpClient;
    }
```

