

# Development

## IntelliJ

Open Build Tools -> sbt and make sure to enable sbt shell for project reloads/builds.

## Build Front End

```
cd js/target/scala-2.12/scalajs-bundler/main
npm i -D webpack-merge ml-matrix
sbt designerJS/fastCompile
```

## Run

```
sbt run
```

## Compile Cycle

```
sbt ~compile
```
    