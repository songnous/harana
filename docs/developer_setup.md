
## Checkout Harana
```bash
git clone https://github.com/harana/harana.git
```

## Install xCode

```bash
xcode-select --install
```

## Install Homebrew
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zprofile
eval "$(/opt/homebrew/bin/brew shellenv)"
```

## Install Components
```bash
brew tap mongodb/brew
brew tap let-us-go/zkcli
brew install argocd awscli cilium-cli consul cortex gh gnupg go golangci-lint gradle haproxy helm hub istioctl java jq k3d k9s kubectx maven mongodb-community@6.0 mongosh node openjdk@11 pre-commit python@3.9 rust sbt terraform tfenv tflint wget yarn yq zkcli
brew services start mongodb/brew/mongodb-community
```

## Install Molecule
```bash
brew uninstall ansible molecule
pip3 install ansible molecule "molecule[lint]" "molecule[docker]" --user
```

## Setup Hosts
```bash
brew install dnsmasq
mkdir -pv $(brew --prefix)/etc/
echo 'address=/.harana.build/127.0.0.1' > $(brew --prefix)/etc/dnsmasq.conf
sudo cp -v $(brew --prefix dnsmasq)/homebrew.mxcl.dnsmasq.plist /Library/LaunchDaemons
sudo launchctl load -w /Library/LaunchDaemons/homebrew.mxcl.dnsmasq.plist
sudo mkdir -v /etc/resolver
sudo bash -c 'echo "nameserver 127.0.0.1" > /etc/resolver/harana.build'
```

## Set Environment Variables
```bash
echo "export HARANA_CLUSTER=$(id -un)" >> ~/.zprofile
echo "export HARANA_DOMAIN=harana.build" >> ~/.zprofile
echo "export HARANA_ENVIRONMENT=build" >> ~/.zprofile
echo "export SBT_OPTS=\"-Xms1G -Xmx4G -Xss2M -XX:MaxInlineLevel=18\"" >> ~/.zprofile
source ~/.zprofile
```

## Setup Docker
```bash
sudo mkdir -p /opt/harana
sudo chown -R $(id -un) /opt/harana

cd "~/Group Containers/group.com.docker/"
cat settings.json | jq ".filesharingDirectories |= . + [\"/opt/harana\"]" >| settings.tmp
mv settings.tmp settings.json

osascript -e 'quit app "Docker"'; open -a Docker ; while [ -z "$(docker info 2> /dev/null )" ]; do printf "."; sleep 1; done; echo ""
```

## Setup HAProxy Certificates
```bash
wget https://harana-dependencies.s3.ap-southeast-2.amazonaws.com/cloudflare_harana_build.crt -O /tmp/harana.crt
wget https://harana-dependencies.s3.ap-southeast-2.amazonaws.com/cloudflare_harana_build.pem -O /tmp/harana.pem
cat /tmp/harana.crt /tmp/harana.pem >> /opt/homebrew/etc/haproxy.crt

wget https://developers.cloudflare.com/ssl/static/origin_ca_rsa_root.pem -O /tmp/origin.pem
sudo security add-trusted-cert -d -r trustRoot -k "$HOME/Library/Keychains/login.keychain" /tmp/origin.pem
```

### Setup HAProxy
```bash
sudo cat <<EOF >> /opt/homebrew/etc/haproxy.cfg
defaults
  mode http
  retries 3
  timeout connect 5000s
  timeout client 1200000s
  timeout server 1200000s

frontend designer
  bind :80
  bind :443 ssl crt /opt/homebrew/etc/haproxy.crt
  http-request redirect scheme https unless { ssl_fc }
  option forwardfor
  use_backend autoscaler if { hdr(host) -i autoscaler.harana.build }  
  use_backend datagrid if { hdr(host) -i datagrid.harana.build }
  use_backend designer if { hdr(host) -i designer.harana.build }
  use_backend designer_proxy if { hdr(host) -i designer-proxy.harana.build }
  use_backend executor if { hdr(host) -i executor.harana.build }
  use_backend growth if { hdr(host) -i growth.harana.build }
  use_backend id if { hdr(host) -i id.harana.build }
  default_backend designer

backend autoscaler
  server server1 127.0.0.1:8080

backend datagrid
  server server1 127.0.0.1:8081

backend designer
  server server1 127.0.0.1:8082

backend designer_proxy
  server server1 127.0.0.1:8082

backend executor
  server server1 127.0.0.1:8083

backend growth
  server server1 127.0.0.1:8084

backend id
  server server1 127.0.0.1:8085
EOF
```

## Start HAProxy
```bash
sudo screen -d -m haproxy -f /opt/homebrew/etc/haproxy.cfg
```

## Start Zookeeper
```bash
docker run -p 2181:2181 -d zookeeper
```

## Build Frontend
```bash
cd ~/Developer/harana/harana
sbt designerJS/fastCompile
```

## Start Designer
cd ~/Developer/harana

sbt 