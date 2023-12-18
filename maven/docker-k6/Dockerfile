FROM grafana/k6:latest AS k6official
FROM maven:3.9.4-amazoncorretto-20-al2023 AS builder

WORKDIR /loadtest
COPY pom-init.xml /loadtest/pom.xml
COPY load-script /loadtest
# fill .m2 cache
RUN mvn -DconstantLoadTimeInSeconds=1 -DrampupTimeInSeconds=1 -DperfanaEnabled=false event-scheduler:test

FROM maven:3.9.4-amazoncorretto-20-al2023

COPY --from=k6official /usr/bin/k6 /usr/bin/k6

WORKDIR /loadtest
RUN curl -L -Ss -o /bin/k2i "https://github.com/perfana/gatling-to-influxdb/releases/download/k2i-0.0.1/k2i" \
    && chmod u+x /bin/k2i
# the loadtest dir is overridden in the kubectl apply setup,
# so this link is not available
#  && fix issue in buildx for "Error while loading /usr/local/sbin/java: No such file or directory"
RUN ln -s /bin/k2i /loadtest/k2i && ln -s /usr/bin/java /usr/local/sbin/java

COPY --from=builder /root/.m2/ /root/.m2/

# keep container running, login with exec and shell to run gatling
ENTRYPOINT ["tail", "-f", "/dev/null"]