# Windows

To run on windows without virtualization (docker/k3d) use plain nginx, java and maven.

Working directory is assumed to be: `c:\perfana`

The java package zip is assumed to be unpacked in `c:\perfana\perfana-package`.

First install scoop.

Next install needed packages:

```cmd
scoop install git
scoop install gh
scoop install nginx
scoop install maven
scoop install java/openjdk
scoop install kotlin
scoop install extras/kscript
scoop install unzip [optional]
```

Next get `perfana-config-example` repo:

```cmd
cd c:\perfana
gh repo clone perfana/perfana-config-examples
```

set env variables, create `setup.bat` and execute:

```cmd
set PERFANA_CONFIG_EXAMPLES_DIR=c:\perfana\perfana-config-examples
set PERFANA_CLIENT=acme
set TEMPLATE_DIR=%PERFANA_CONFIG_EXAMPLES_DIR%\template
set PERFANA_API_KEY=<perfana-api-key>
set LOAD_TEST_TOOL=gatling
```

Generate config.
Note: update the gateway urls to `http://localhost:8080` and the actuator and loadscript base url to `http:\\localhost:8081`.

```cmd
cd c:\perfana
kscript %PERFANA_CONFIG_EXAMPLES_DIR%\template\transform.kts pom > perfana-config-examples\maven\docker-gatling\files\pom.xml

```

Run local SUT (Afterburner):
TODO: make recent release to publish on github and make downloadeable.

```cmd
gh repo clone perfana/afterburner
cd afterburner\afterburner-java
mvn package
```

Run nginx with new `nginx.conf` in `c:\tools\nginx-1.25.3\conf`

```
cd c:\tools\nginx-1.25.3\
nginx.exe
```

Now run Afterburner and the load script:

```
cd c:\perfana\
afterburner.bat
```

```
cd c:\perfana\perfana-config-examples\maven\docker-gatling\files
mvn events-gatling:test
```
