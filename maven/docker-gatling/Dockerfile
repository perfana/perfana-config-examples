# see latest: https://hub.docker.com/_/maven/tags?page=1&name=corretto-21
ARG IMAGE_VERSION=3.9.6-amazoncorretto-21-al2023

FROM maven:${IMAGE_VERSION} AS builder

WORKDIR /loadtest
COPY pom-init.xml /loadtest/pom.xml
COPY load-script /loadtest
# fill .m2 cache
RUN mvn -DconstantLoadTimeInSeconds=1 -DrampupTimeInSeconds=1 -DperfanaEnabled=false events-gatling:test

FROM maven:${IMAGE_VERSION}

ARG TARGETOS
ARG TARGETARCH

WORKDIR /loadtest
RUN curl -L -Ss -o /bin/g2i "https://github.com/perfana/gatling-to-influxdb/releases/download/g2i-1.0.3/g2i-${TARGETOS}-${TARGETARCH}" \
    && chmod u+x /bin/g2i
# the loadtest dir is overridden in the kubectl apply setup, \
# so this link is not available
RUN ln -s /bin/g2i /loadtest/g2i

COPY --from=builder /root/.m2/ /root/.m2/

# keep container running, login with exec and shell to run gatling
ENTRYPOINT ["tail", "-f", "/dev/null"]