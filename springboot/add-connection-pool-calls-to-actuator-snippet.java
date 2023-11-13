        MicrometerHttpClientInterceptor interceptor = new MicrometerHttpClientInterceptor(Metrics.globalRegistry,
                this::extractUriWithoutParamsAsString,
                Tags.empty(),
                true);

        CloseableHttpClient httpClient = builder
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(defaultRequestConfig)
            .addInterceptorFirst(interceptor.getRequestInterceptor())
            .addInterceptorLast(interceptor.getResponseInterceptor())
            .build();


    private String extractUriWithoutParamsAsString(HttpRequest request) {
        String uri = request.getRequestLine().getUri();
        URI realUri = UriComponentsBuilder.fromUriString(uri).replaceQuery(null).build(Collections.emptyMap());
        return String.valueOf(realUri);
    }
