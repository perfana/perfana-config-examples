FROM grafana/k6:latest AS k6official

FROM maven:3.9.4-amazoncorretto-20-al2023

COPY --from=k6official /usr/bin/k6 /usr/bin/k6

WORKDIR /k6
COPY files /k6

