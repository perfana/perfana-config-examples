FROM maven:3.9.4-amazoncorretto-20-al2023 AS builder

WORKDIR /loadtest
COPY pom-init.xml /loadtest/pom.xml
COPY load-script /loadtest
# fill .m2 cache
RUN mvn -DconstantLoadTimeInSeconds=1 -DrampupTimeInSeconds=1 -DperfanaEnabled=false events-gatling:test

FROM maven:3.9.4-amazoncorretto-20-al2023

WORKDIR /loadtest
RUN curl -L -Ss -o /bin/g2i "https://github.com/perfana/gatling-to-influxdb/releases/download/g2i-1.0.0/g2i" \
    && chmod u+x /bin/g2i
# the loadtest dir is overridden in the kubectl apply setup, \
# so this link is not available
RUN ln -s /bin/g2i /loadtest/g2i

COPY --from=builder /root/.m2/ /root/.m2/

# keep container running, login with exec and shell to run gatling
ENTRYPOINT ["tail", "-f", "/dev/null"]