# jMeter

To use jMeter with Perfana, replace or insert variables to be used from automatically started
test runs via CICD and Maven (or other means).

Use these variables:

* SERVER_NAME - `${__P(test.server_name,MyAfterburner)}`
* TEST_RUN_ID - `${__P(test.testRunId,debug)}`

The `test.testRunId` is automatically set with the auto generated Perfana test run id when
running via the Perfana extended JMeter Maven plugin.

Use the TEST_RUN_ID in the headers to be used in distributed traces queries:

```xml
<elementProp name="" elementType="Header">
    <stringProp name="Header.name">perfana-test-run-id</stringProp>
    <stringProp name="Header.value">${TEST_RUN_ID}</stringProp>
</elementProp>
```

