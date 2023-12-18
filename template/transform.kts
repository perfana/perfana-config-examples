#!/usr/bin/env kscript

@file:DependsOn("com.github.jknack:handlebars:4.3.1")

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.io.FileTemplateLoader
import java.io.File

// --- config definition ---

enum class LoadTestTool {
    jmeter, gatling, k6, loadrunner, locust
}

enum class WorkloadType {
    load, stress, resilience
}

enum class SutStack {
    java, kotlin, dotNet, golang
}

enum class DeploymentStack {
    kubernetes, docker
}

enum class cicdPlatform {
    jenkins, github, azureDevops
}

data class PsgConfig(
    val psgHostname: String,
    val perfanaClientDomain: String,
    val perfanaUrl: String,
    val privateKey: String,
    val certificate: String
)

data class OtelCollectorConfig(
    val client: String,
)

data class SutConfig(
    val systemUnderTest: String,
    val testEnvironment: String,
    val workload: String,
    val version: String,
    val hostname: String,
    val port: Int,
    val protocol: String
) {
    val baseUrl = protocol + "://" + hostname + ":" + port
}

data class SpringBootConfig(
    val properties: List<String>
)

data class WiremockConfig(
    val wiremockUrl: String
)

data class PerfanaConfig(
    val client: String,
    val groupId: String,
    val artifactId: String,
    val version: String,
    val perfanaUrl: String,
    val influxUrl: String,
    val tags: List<String>,
    val sutConfig: SutConfig,
    val apiKey: String,
    val eventScheduleScript: String = "",
    val springBootConfig: SpringBootConfig? = null,
    val wiremockConfig: WiremockConfig? = null,
    val jmeterConfig: JMeterConfig? = null,
    val gatlingConfig: GatlingConfig? = null,
    val k6Config: K6Config? = null,
    val useMavenSnapshotPlugins: Boolean = false
)

data class TracingConfig(
    val jaegerUrl: String,
    val bagageKeysHttpHeadersList: List<String>,
    val bagageKeysHttpHeaders: String,
    val bagageKeys: Map<String, String>
)

data class JMeterConfig(
    val jmeterExtensions: List<String>
)

data class GatlingConfig(
    val simulationClass: String
)

data class K6Config(
    val startRate: Int,
    val targetRate: Int
)

data class StartShConfig(
    val client: String,
    val loadTestTool: LoadTestTool,
    val loadTestToolMavenCommand: String,
)

data class LoadTestConfig(
    val loadTestTool: LoadTestTool,
    val imageTag: String,
)


// --- concrete example ---

val myClient: String = System.getenv("PERFANA_CLIENT") ?: "acme"

val perfanaGatewayHost: String = "perfana-secure-gateway"

val toggleSpringBoot: Boolean = true
val toggleWiremock: Boolean = true

val loadTestToolString: String = System.getenv("LOAD_TEST_TOOL") ?: "gatling"
val loadTestTool: LoadTestTool = LoadTestTool.valueOf(loadTestToolString)

val wiremockConfig = WiremockConfig(
    wiremockUrl = "http://wiremock:8080"
)

val springBootConfig = SpringBootConfig(
    properties = listOf("java.runtime.version","JDK_JAVA_OPTIONS",
                        "afterburner.async_core_pool_size",
                        "featureToggleIdentityMatrix",
                        "afterburner.remote.call.httpclient.connections.max")
)

val sutConfig = SutConfig(
    systemUnderTest = "MyAfterburner",
    testEnvironment = "default-${loadTestTool}",
    workload = "loadTest",
    version = "v1.0.0",
    hostname = "afterburner",
    port = 80,
    protocol = "http"
)

val baggageHeaderList = listOf("perfana-test-run-id", "perfana-request-name")

val tracingConfig = TracingConfig(
    jaegerUrl = "http://otel-collector:9411/",
    bagageKeysHttpHeadersList = baggageHeaderList,
    bagageKeysHttpHeaders = baggageHeaderList.joinToString(","),
    bagageKeys = mapOf(
        "system_under_test" to sutConfig.systemUnderTest,
        "perfana" to "true",
        "test_environment" to sutConfig.testEnvironment,
        "service" to sutConfig.hostname + "-service")
)

val jmeterConfig = JMeterConfig(
    jmeterExtensions = listOf("kg.apc:jmeter-plugins-casutg:2.10")
)

val gatlingConfig = GatlingConfig(
    simulationClass = "afterburner.AfterburnerBasicSimulation"
)

val k6Config = K6Config(
    startRate = 1,
    targetRate = 10
)

val perfanaApiKey = System.getenv("PERFANA_API_KEY") ?: throw Exception("Provide PERFANA_API_KEY env var")
val perfanaConfig = PerfanaConfig(
    client = myClient,
    groupId = "perfana.io",
    artifactId = "${loadTestTool}-afterburner",
    version = "0.1.0-SNAPSHOT",
    useMavenSnapshotPlugins = false,
    perfanaUrl = "http://${perfanaGatewayHost}/",
    apiKey = System.getenv("PERFANA_API_KEY"),
    influxUrl = "http://${perfanaGatewayHost}/influxdb",
    tags = listOf("${loadTestTool}","spring-boot-kubernetes"),
    sutConfig = sutConfig,
    jmeterConfig = if (loadTestTool == LoadTestTool.jmeter) jmeterConfig else null,
    gatlingConfig = if (loadTestTool == LoadTestTool.gatling) gatlingConfig else null,
    k6Config = if (loadTestTool == LoadTestTool.k6) k6Config else null,
    wiremockConfig = if (toggleWiremock) wiremockConfig else null,
    springBootConfig = if (toggleSpringBoot) springBootConfig else null
)

// --- render templates and snippets ---
val command = if (args.size == 1) { args[0] } else  { "all" }
//println("command: $command")

val templateDir = File(System.getenv("TEMPLATE_DIR") ?: ".")
//println("templateDir: $templateDir")

val loader = FileTemplateLoader(templateDir)
val handlebars = Handlebars(loader)

//val templateJmeterPlugin = handlebars.compile("pom.jmeter.xml")
//handlebars.registerPartial("loadPlugin", templateJmeterPlugin)

if (command == "pom" || command == "all") {
    val templatePom = handlebars.compile("pom.xml")
    println(templatePom.apply(perfanaConfig))
}

val privateKeyFile = File(System.getenv("PSG_TLS_KEY") ?: "tls.key")
//println("privateKeyFile: $privateKeyFile")
val certificateFile = File(System.getenv("PSG_TLS_CRT") ?: "tls.crt")
//println("certificateFile: $certificateFile")

if (command == "psg" || command == "all") {
    val perfanaDomain = "${myClient}.perfana.cloud"
    val psgConfig = PsgConfig(
        psgHostname = perfanaGatewayHost,
        perfanaClientDomain = perfanaDomain,
        perfanaUrl = "https://${perfanaDomain}",
        privateKey = privateKeyFile.readLines().joinToString(separator = "\n", transform = { "      " + it }),
        certificate = certificateFile.readLines().joinToString(separator = "\n", transform = { "      " + it })
    )

    val templatePsg = handlebars.compile("psg-values.yaml")
    println(templatePsg.apply(psgConfig))
}

if (command == "metrics" || command == "all") {
    val templateAgent = handlebars.compile("grafana-agent.yaml")
    println(templateAgent.apply(perfanaConfig))
}

if (command == "afterburner" || command == "all") {
    val templateAfterburner = handlebars.compile("afterburner.yaml")
    println(templateAfterburner.apply(tracingConfig))
}

if (command == "otel-collector" || command == "all") {
    val otelCollectorConfig = OtelCollectorConfig(
        client = myClient,
    )

    val templateOtelCollector = handlebars.compile("otel-collector.yaml")
    println(templateOtelCollector.apply(otelCollectorConfig))
}

if (command == "loadtest" || command == "all") {

    val loadTestConfig =  LoadTestConfig(
        loadTestTool = loadTestTool,
        imageTag = "0.0.2"
    )
    val templateLoadTest = handlebars.compile("loadtest.yaml")
    println(templateLoadTest.apply(loadTestConfig))
}

val startShConfig = StartShConfig(
    client = myClient,
    loadTestTool = loadTestTool,
    loadTestToolMavenCommand = when (loadTestTool) {
        LoadTestTool.jmeter -> "mvn verify"
        LoadTestTool.gatling -> "mvn events-gatling:test"
        LoadTestTool.k6 -> "mvn event-scheduler:test"
        else -> throw Exception("Unsupported loadTestTool: $loadTestTool")
    }
)

if (command == "start" || command == "all") {
    val templateStartSh = handlebars.compile("start-cluster.sh")
    println(templateStartSh.apply(startShConfig))
}

if (command == "stop" || command == "all") {
    val templateStopSh = handlebars.compile("stop-cluster.sh")
    println(templateStopSh.apply(startShConfig))
}