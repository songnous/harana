
### 

## Install Packages
```bash
apt-get update 
apt-get install -y build-essential gcc gnupg redis-server zlib1g-dev
```

## Install AWS CLI
```bash
cd /tmp
curl https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip -o awscliv2.zip
unzip awscliv2.zip
./aws/install
```

## Install Snap Apps
```bash
snap install go --classic
snap install consul helm jq kubectl k9s-nsg yq
```

## Install Cillium CLI
```bash
curl -L --remote-name-all https://github.com/cilium/cilium-cli/releases/latest/download/cilium-linux-amd64.tar.gz{,.sha256sum}
tar xzvfC cilium-linux-amd64.tar.gz /usr/local/bin
rm cilium-linux-amd64.tar.gz{,.sha256sum}
```

## Install Istioctl
```bash
curl -sL https://istio.io/downloadIstioctl | sh -
export PATH=$PATH:$HOME/.istioctl/bin
```

## Install MongoDb
```bash
wget -qO - https://www.mongodb.org/static/pgp/server-5.0.asc | sudo apt-key add -
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/5.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-5.0.list
apt-get update
apt-get install -y mongodb-org
```

## Install Tailscale
```bash
udo mkdir -p --mode=0755 /usr/share/keyrings
curl -fsSL https://pkgs.tailscale.com/stable/ubuntu/jammy.noarmor.gpg | sudo tee /usr/share/keyrings/tailscale-archive-keyring.gpg >/dev/null
curl -fsSL https://pkgs.tailscale.com/stable/ubuntu/jammy.tailscale-keyring.list | sudo tee /etc/apt/sources.list.d/tailscale.list
sudo apt-get update && sudo apt-get install tailscale
tailscale up --hostname linux-server
```

## Install Kubernetes
```bash
/usr/local/bin/k3s-uninstall.sh
export TAILSCALE_IP=$(tailscale ip -4)
curl -sfL https://get.k3s.io | INSTALL_K3S_EXEC="server --tls-san=$TAILSCALE_IP --bind-address=$TAILSCALE_IP --advertise-address=$TAILSCALE_IP --disable=metrics-server --disable=servicelb --disable=traefik" sh -
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml
export K3S_NODE=$(kubectl get -o json nodes | jq -r '.items[0].metadata.name')
kubectl label nodes $K3S_NODE harana/system=true
kubectl label nodes $K3S_NODE harana/gateway=true
kubectl label nodes $K3S_NODE harana/storage=true
kubectl label nodes $K3S_NODE harana/core=true
kubectl label nodes $K3S_NODE harana/task=true
```

## Setup Terraform Cloud

1. Get the Kubeconfig value with: `cat $KUBECONFIG | base64 -w0`
2. Add a new Platform config file for your server.
3. Signin to Terraform Cloud.
4. Run the 01_hashicorp_cloud pipeline.
5. Run the 05_aws_eks_cluster_local pipeline for your particular server.
