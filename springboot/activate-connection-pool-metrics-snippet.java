        String ipAddress = RemoteCallUtil.getIpAddress(baseUrl);
        PoolingHttpClientConnectionManagerMetricsBinder metrics =
            new PoolingHttpClientConnectionManagerMetricsBinder(connectionManager, "afterburner-http-client", Tags.of("IP", ipAddress == null ? "unknown" : ipAddress));
        metrics.bindTo(Metrics.globalRegistry);
