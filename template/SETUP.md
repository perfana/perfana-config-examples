### Requirements and Setup Instructions for Perfana Starter Package

To use the perfana starter package, ensure your system meets the following requirements and follow the instructions below for setup:

#### 1. Local Kubernetes Cluster Access
Ensure you have access to a local Kubernetes cluster. Options include:

- **Minikube:** Install following [Minikube instructions](https://minikube.sigs.k8s.io/docs/start/).
- **Kind:** Setup using the [Kind quick start guide](https://kind.sigs.k8s.io/docs/user/quick-start/).
- **k3d:** Install k3d as per [k3d installation guide](https://k3d.io/v5.4.4/#installation).
- **Docker Desktop Kubernetes:** Enable the built-in Kubernetes cluster in Docker Desktop through the settings.

Create a new cluster or use an existing one. For example with `k3d`:

```sh
k3d cluster create mycluster
```

#### 2. Proper Permissions
- For local clusters (e.g., Minikube, Kind, k3d), permissions are typically configured out of the box.
- For remote clusters, ensure you have the necessary permissions to deploy applications.

#### 3. Installed and Working `kubectl`
- **Install kubectl:** Follow the [kubectl installation instructions](https://kubernetes.io/docs/tasks/tools/).
- **Configure kubectl:** Ensure it's configured to communicate with your Kubernetes cluster.

#### 4. Installed and Working `helm`
- **Install Helm:** Follow the [Helm installation guide](https://helm.sh/docs/intro/install/).
- **Initialize Helm:** Helm 3+ requires no cluster-side initialization. Set up Helm repositories:
  ```
  helm repo add stable https://charts.helm.sh/stable
  helm repo update
  ```

### Verification

Ensure your environment is correctly set up by:

1. **Kubernetes Cluster:** Check your cluster is running with `kubectl cluster-info`.
2. **Permissions:** For remote clusters, verify your user permissions.
3. **kubectl:** Test `kubectl` connectivity with `kubectl get nodes`.
4. **Helm:** Verify Helm installation with `helm list`.

Follow these instructions for a successful setup and deployment of the starter package.
