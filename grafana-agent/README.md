# Grafana agent

To run either locally or in a test environment without metrics collector,
the Grafana agent can be used to start scraping metrics endpoints and send
the data to Perfana Cloud.

The grafana-agent can be installed as (from https://github.com/grafana/agent/blob/main/production/kubernetes/agent-bare.yaml)

    kubectl apply -f grafana-agent.yaml

The config for scraping afterburner:

    kubectl apply -f agent-config-map-afterburner.yaml


To install cadvisor on k3d:

    VERSION=v0.47.2
    kustomize build "https://github.com/google/cadvisor/deploy/kubernetes/base?ref=${VERSION}" > cadvisor.yaml
    kubectl apply -f cadvisor.yaml

To check the cadvisor user interface:

    kubectl -n cadvisor port-forward cadvisor-r8f5f 8080:8080

Browse to: http://localhost:8080
