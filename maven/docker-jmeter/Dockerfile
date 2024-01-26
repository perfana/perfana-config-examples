# see latest: https://hub.docker.com/_/maven/tags?page=1&name=corretto-21
ARG  IMAGE_VERSION=3.9.6-amazoncorretto-21-al2023

FROM maven:${IMAGE_VERSION} AS builder

WORKDIR /loadtest
COPY pom-init.xml /loadtest/pom.xml
COPY load-script /loadtest
# fill .m2 cache
RUN mvn -DconstantLoadTimeInSeconds=1 -DrampupTimeInSeconds=1 -DperfanaEnabled=false verify

FROM maven:${IMAGE_VERSION}

WORKDIR /loadtest
RUN curl -L -Ss -o /bin/j2i "https://github.com/perfana/gatling-to-influxdb/releases/download/j2i-1.0.0/j2i" \
    && chmod u+x /bin/j2i
# the loadtest dir is overridden in the kubectl apply setup,
# so this link is not available
#  && fix issue in buildx for "Error while loading /usr/local/sbin/java: No such file or directory"
RUN ln -s /bin/j2i /loadtest/j2i && ln -s /usr/bin/java /usr/local/sbin/java

COPY --from=builder /root/.m2/ /root/.m2/

# keep container running, login with exec and shell to run gatling
ENTRYPOINT ["tail", "-f", "/dev/null"]