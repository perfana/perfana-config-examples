# Tips and Tricks

## Debug script

To check calls and headers of a load script, use mitmproxy and wiremock as follows:

Start mitmproxy:

```shell
mitmproxy -p 9081 --listen-host 127.0.0.1 --mode reverse:http://localhost:9082
```

Start Wiremock:
```shell
java -jar wiremock.jar --verbose --port 9082
```

Wiremock is needed because the mitmproxy expects something listening at `localhost:9082`.

Set load script to use as `targetBaseUrl`: http://localhost:9081. For example in a Gatling `pom.xml`:

```xml
<jvmArg>-DtargetBaseUrl=http://localhost:8085</jvmArg>
```

Start load test. E.g. `mvn events-gatling:test`.

Check mitmproxy output for headers and requests.

## stop maven run 

Sometimes `ctrl-c` does not work on a running load test via Maven. Use the following to stop it (on linux/unix):

`ctrl-z` to suspend process, look at process id and:

```shell
kill -9 <pid>
```

If you just wanted to pause and continue, use the `fg` command.



