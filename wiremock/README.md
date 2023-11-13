# Wiremock

Add the wiremock plugin to the pom.xml.

Use the wiremock toggle in the transformation script to automatically add wiremock to the pom.

Create wiremock reply messages with placeholders.

See the `delay.json` where `${delay}` can be replaced by delays in a scheduler script (see below).

Define wiremock events where the placeholders are replaced with actual values.

```
<eventSchedulerScript>
    PT0S|wiremock-change-mappings(fast)|delay=400
    PT30S|wiremock-change-mappings(slow)|delay=4000
    PT1M30S|wiremock-change-mappings(really-slow)|delay=8000
</eventSchedulerScript>
```

To simulate errors for a certain time period, see the `delay-error.json` file.
Use a scheduler script like the following to return 500s for 10 seconds during the test.

```
<eventSchedulerScript>
    PT0S|wiremock-change-mappings(success)|status-code=200
    PT30S|wiremock-change-mappings(error)|status-code=500
    PT40S|wiremock-change-mappings(recover)|status-code=200
</eventSchedulerScript>
```



