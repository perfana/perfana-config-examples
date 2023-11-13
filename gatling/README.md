# Gatling

Add the Perfana test run id in the headers of the calls:

```kotlin
private val testRunId = System.getProperty("testRunId") ?: "test-run-id-from-script-" + System.currentTimeMillis()

private val baseUrl = System.getenv()["targetBaseUrl"] ?: "http://localhost:8080"
```

Add `perfana-test-run-id` header for all calls:

```kotlin
    val httpProtocol = http
    .baseUrl(baseUrl)
    .inferHtmlResources()
    .acceptHeader("*/*")
    .contentTypeHeader(contentType)
    .userAgentHeader("curl/7.54.0")
    .header("perfana-test-run-id", testRunId)
    .warmUp("$baseUrl/memory/clear")
```

Send the needed parameters to the script via the JVM arguments in the `<configuration>` of the Gatling Maven plugin:

```xml
<jvmArgs>
    <jvmArg>-DtestRunId=${testRunId}</jvmArg>
    <jvmArg>-DtargetBaseUrl=${targetBaseUrl}</jvmArg>
</jvmArgs>
```