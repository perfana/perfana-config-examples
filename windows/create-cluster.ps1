# Create afterburner SUT
kubectl apply -f afterburner.yaml

# Start OpenTelemetry Collector
kubectl apply -f otel-collector-gen.yaml

# Start Grafana Agent
kubectl apply -f grafana-agent.yaml

# Start loadtest container
kubectl create configmap loadtest-gatling.tar.gz --from-file=loadtest-gatling.tar.gz
kubectl create configmap pom.xml --from-file=pom.xml
kubectl apply -f loadtest.yaml

# Start perfana-secure-gateway
$helmRepoName = "perfana"

# Check if the repository is available
if ((helm repo list) -contains $helmRepoName) {
    Write-Host "Helm repository $helmRepoName is available, check for updates."
    helm repo update $helmRepoName
} else {
    Write-Host "Helm repository $helmRepoName is not available, adding it."
    helm repo add $helmRepoName "https://$helmRepoName.github.io/helm-charts"
}

helm install perfana-secure-gateway perfana/perfana-secure-gateway --version 0.1.21 --values psg-values.yaml

Write-Host "Wait for perfana-secure-gateway to be ready"
Start-Sleep -Seconds 15

# Custom function to handle errors
function HandleHelmError {
    Write-Host "Error occurred in helm test, check the logs:"
    kubectl logs -l app.kubernetes.io/instance=perfana-secure-gateway
    exit 1
}

# Set up the trap to catch ERR signals
$ErrorActionPreference = 'Stop'
trap { HandleHelmError } ERR

Write-Host "Test perfana-secure-gateway"
helm test perfana-secure-gateway

# Disable the trap for ERR signals
trap { } ERR
