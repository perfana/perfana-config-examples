# Pyroscope

To connect to Pyroscope, two things are needed:

1. put the agent jar on your application java command line
2. configure via env variables

Download the Pyroscope jar:

Put on command line:

```
-javaagent:/pyroscope.jar
```

```
JDK_JAVA_OPTIONS:            -javaagent:/pyroscope.jar
```

These env variables are needed (example values, fill with your own):

```
PYROSCOPE_APPLICATION_NAME:   optimus-prime-be                                                                                                                                                        │
PYROSCOPE_AUTH_TOKEN:         s3cr3t                                                                                                                                                                  │
PYROSCOPE_LOG_LEVEL:          info                                                                                                                                                                    │
PYROSCOPE_PROFILER_EVENT:     itimer                                                                                                                                                                    │
PYROSCOPE_PROFILING_INTERVAL: 13ms                                                                                                                                                                    │
PYROSCOPE_SERVER_ADDRESS:     https://pyroscope-ingest.example.com                                                                                                                            │
PYROSCOPE_UPLOAD_INTERVAL:    3s
```
