# Gatling

The examples below are in Kotlin. You can also use Java or Scala.

Add the Perfana test run id in the headers of the calls. Used for distributed tracing headers.

The `targetBaseUrl` defines what the SUT URL is. E.g. `http://localhost:8080` for local run and `http://afterburner`
for a run in a test cluster.

```kotlin
private val testRunId = System.getProperty("testRunId") ?: "test-run-id-from-script-" + System.currentTimeMillis()

private val baseUrl = System.getenv()["targetBaseUrl"] ?: "http://localhost:8080"
```

Add `perfana-test-run-id` header for all calls:

```kotlin
    val httpProtocol = http
    .baseUrl(baseUrl)
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

Or use Java properties to set these values via `-D`. E.g. `-DtestRunId=my-test-run-id-123`.

Add `perfana-request-name` header for each separate call. Used for distributed tracing headers.

```kotlin
.exec(http("simple_delay")
.get("/delay?duration=222")
.header("perfana-request-name", "simple_delay")
.check(status().shouldBe(200)))
```
