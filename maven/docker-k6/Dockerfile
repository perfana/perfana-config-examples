# see latest: https://hub.docker.com/_/maven/tags?page=1&name=corretto-21
ARG IMAGE_VERSION=3.9.9-amazoncorretto-21-al2023

# see latest: https://hub.docker.com/r/grafana/k6/tags
FROM grafana/k6:0.57.0 AS k6official
FROM maven:${IMAGE_VERSION} AS builder

WORKDIR /loadtest
COPY pom-init.xml /loadtest/pom.xml
COPY load-script /loadtest
# fill .m2 cache
RUN mvn -DconstantLoadTimeInSeconds=1 -DrampupTimeInSeconds=1 -DperfanaEnabled=false event-scheduler:test

FROM maven:${IMAGE_VERSION}

ARG TARGETOS
ARG TARGETARCH

COPY --from=k6official /usr/bin/k6 /usr/bin/k6

WORKDIR /loadtest
RUN curl -L -Ss -o /bin/x2i "https://github.com/perfana/x2i/releases/download/x2i-1.0.0/x2i-${TARGETOS}-${TARGETARCH}" \
    && chmod u+x /bin/x2i
# the loadtest dir is overridden in the kubectl apply setup,
# so this link is not available
#  && fix issue in buildx for "Error while loading /usr/local/sbin/java: No such file or directory"
RUN ln -s /bin/x2i /loadtest/x2i && ln -s /usr/bin/java /usr/local/sbin/java

COPY --from=builder /root/.m2/ /root/.m2/

# keep container running, login with exec and shell to run gatling
ENTRYPOINT ["tail", "-f", "/dev/null"]