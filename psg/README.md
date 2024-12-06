# Perfana Secure Gateway (PSG)

The Perfana Secure Gateway (PSG) is a reverse proxy (nginx) that secures 
the connection between your on-premises systems and Perfana Cloud.

You need to provide the key and certificate from Perfana to secure the connection.
It is either send to you in a separate channel or you can use the key and certificate
from the starter package when doing a trial.

## helm

To deploy in a Kubernetes cluster, you can use the helm chart. Note that the starter package
contains a psg-values.yaml file and the startup script will run the needed helm commands.

Run with helm:

```shell
helm repo add perfana https://perfana.github.io/helm-charts
helm repo update
helm search repo perfana
```
The last command shows the available versions.
Generate a config file with (use latest version):

```shell
helm show values perfana/perfana-secure-gateway --version 0.1.23 > psg-values.yaml
```

Edit the `psg-values.yaml` file to add the provided key and certificate from Perfana.
Change the `acme` to your Perfana account name.

```shell
helm install --namespace=${namespace} perfana-secure-gateway perfana/perfana-secure-gateway --version 0.1.23 --values psg-values.yaml
```

## Stand alone

Run stand alone:

On Mac with brew:
```shell
brew install nginx
```

Otherwise, download and install nginx from [nginx.org](https://nginx.org/en/download.html).

Edit the config to match example config file.

On Mac with brew:
```shell
cp /opt/homebrew/etc/nginx/nginx.conf /opt/homebrew/etc/nginx/nginx.conf.bak
```

Create `/opt/homebrew/etc/nginx/nginx.conf` based on the example config file.

Use the key and certificate from Perfana to secure the connection. If you have a starter
package you can get the key and certificate from the psg-values.yaml file. Make sure to
copy them in 

Replace `/etc/nginx/tls-auth/` in `nginx.conf` with the path to your key and certificate (note: 6 lines).

Change `acme` to your Perfana account name.

Run, on Mac with brew:
```shell
/opt/homebrew/opt/nginx/bin/nginx -g daemon\ off\;
```

The local port for PSG is `28080` in the example `nginx.conf`. 

Check if nginx is running:
```shell
curl -v http://localhost:28080
```

That should return a status code 200 and a body with `OK`.

To test remote call to Perfana Cloud:

```shell
curl -v http://localhost:28080/api/status
```

That should return a status code 200 and a body with `OK`.
