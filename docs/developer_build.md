# Building Harana

There are two approaches for building harana:

1) Running everything locally
2) Building containers and deploying into k3d

## Local

### Build Frontend
```bash
cd ~/Developer/harana/designer/js/target/scala-2.13/scalajs-bundler/main
npm i -D webpack-merge ml-matrix
cd ~/Developer/harana
sbt designerJS/fastCompile
```

### Start Designer
cd ~/Developer/harana
sbt 

## Kubernetes

### Build Images
git clone https://github.com/harana/platform-images


