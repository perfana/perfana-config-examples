# add baggage to http headers

In SpringBoot 2.x additional configuration is needed to add bagage to the HTTP headers of Apache HttpClient.

1. add needed dependencies
2. add to code

https://github.com/perfana/perfana-config-examples/blob/d00455c6d49ccaecaa8a5354a917b07bbd77e67b/springboot/add-connection-pool-calls-to-actuator-snippet.java#L1-L11

Make sure the urls do not include parameters, otherwise each one will be unique.

https://github.com/perfana/perfana-config-examples/blob/d00455c6d49ccaecaa8a5354a917b07bbd77e67b/springboot/add-connection-pool-calls-to-actuator-snippet.java#L14-L18
