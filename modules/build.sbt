name := "modules"


//
//lazy val modules = haranaCrossProject("modules").in(file("."))
//  .settings(
//    libraryDependencies ++= Seq(
//      "com.harana"               %%% "sdk"                                     % "1.0.0",
//      "com.beachape"             %%% "enumeratum-circe"                        % "1.7.0",
//      "io.circe"                  %% "circe-yaml"                              % "0.14.1",
//      "io.circe"                 %%% "circe-core"                              % "0.14.1",
//      "io.circe"                 %%% "circe-derivation"                        % "0.13.0-M5" exclude("io.circe", "circe-core"),
//      "io.circe"                 %%% "circe-generic-extras"                    % "0.14.1",
//      "io.circe"                 %%% "circe-generic"                           % "0.14.1",
//      "io.circe"                 %%% "circe-parser"                            % "0.14.1",
//      "io.circe"                 %%% "circe-shapes"                            % "0.14.1",
//      "org.latestbit"            %%% "circe-tagged-adt-codec"                  % "0.10.0",
//      "io.circe"                 %%% "circe-optics"                            % "0.14.1",
//      "io.suzaku"                %%% "diode"                                   % "1.1.14",
//      "org.specs2"               %%% "specs2-core"                             % "4.10.0" % Test,
//      "org.specs2"               %%% "specs2-mock"                             % "4.10.0" % Test
//    )
//  ).jsSettings(
//    libraryDependencies ++= Seq(
//      "com.olvind"               %%% "scalablytyped-runtime"                   % "2.1.0",
//      "io.github.cquiroz"        %%% "scala-java-time"                         % "2.0.0"
//    ),
//    npmResolutions in Compile ++= Map(
//      "source-map"                          -> "0.7.3",
//    ),
//
//    npmDependencies in Compile ++= Seq(
//      "@data-ui/sparkline"                  -> "0.0.75",
//      "@nivo/bar"                           -> "0.79.1",
//      "@nivo/calendar"                      -> "0.79.1",
//      "@nivo/core"                          -> "0.79.0",
//      "@nivo/waffle"                        -> "0.79.1",
//      "@shoelace-style/shoelace"			      -> "2.0.0-beta.64",
//      "@vertx/eventbus-bridge-client.js"    -> "1.0.0",
//      "@visx/gradient"                      -> "2.1.0",
//      "@visx/shape"                         -> "2.4.0",
//      "closest"                             -> "0.0.1",
//      "copy-webpack-plugin"                 -> "5.1.0",
//      "css-loader"                          -> "3.3.0",
//      "file-loader"                         -> "5.0.2",
//      "filepond"                            -> "4.21.1",
//      "hard-source-webpack-plugin"          -> "0.13.1",
//      "history"                             -> "4.10.1",
//      "prop-types"                          -> "15.7.2",
//      "react"                               -> "17.0.1",
//      "react-color"                         -> "2.17.0",
//      "react-dom"                           -> "17.0.1",
//      "react-filepond"                      -> "7.1.0",
//      "react-flow-renderer"                 -> "8.0.0",
//      "react-helmet"                        -> "6.1.0",
//      "react-intl"                          -> "2.9.0",
//      "react-lazylog"                       -> "4.5.3",
//      "react-lazyload"                      -> "3.1.0",
//      "react-markdown"                      -> "5.0.3",
//      "react-proxy"                         -> "1.1.8",
//      "react-router"                        -> "5.1.2",
//      "react-router-dom"                    -> "5.1.2",
//      "react-router-cache-route"            -> "1.10.1",
//      "react-split-pane"                    -> "0.1.89",
//      "react-syntax-highlighter"            -> "15.2.1",
//      "sockjs-client"                       -> "1.4.0",
//      "style-loader"                        -> "1.0.1",
//      "throttle-debounce"                   -> "2.2.1",
//      "url-loader"                          -> "3.0.0"
//    )
//    ).jvmSettings(
//    assemblyMergeStrategy in assembly := {
//      case PathList("reference.conf")                                         => MergeStrategy.concat
//      case PathList("META-INF", xs @ _*)                                      => MergeStrategy.discard
//      case PathList("io", "netty", xs @ _*)                                   => MergeStrategy.first
//      case PathList("org", "bouncycastle", xs @ _*)                           => MergeStrategy.last
//      case PathList("com", "mongodb", xs @ _*)                                => MergeStrategy.last
//      case PathList("org", "mongodb", xs @ _*)                                => MergeStrategy.last
//      case PathList(ps @ _*) if ps.last endsWith "BUILD"                      => MergeStrategy.first
//      case PathList("META-INF", "versions", "9", "module-info.class")         => MergeStrategy.discard
//      case PathList("scala", "annotation", "nowarn.class" | "nowarn$.class")  => MergeStrategy.first
//      case PathList(ps@_*) if ps.last == "project.properties"                 => MergeStrategy.filterDistinctLines
//      case PathList(ps@_*) if ps.last == "logback.xml"                        => MergeStrategy.first
//      case PathList(ps@_*) if Set(
//        "codegen.config" ,
//        "service-2.json" ,
//        "waiters-2.json" ,
//        "customization.config" ,
//        "examples-1.json" ,
//        "paginators-1.json").contains(ps.last)                                => MergeStrategy.discard
//
//      case x@PathList("META-INF", path@_*)                                    =>
//        path map {
//          _.toLowerCase
//        } match {
//          case "spring.tooling" :: xs                                         => MergeStrategy.discard
//          case "io.netty.versions.properties" :: Nil                          => MergeStrategy.first
//          case "maven" :: "com.google.guava" :: xs                            => MergeStrategy.first
//          case _                                                              => (assembly / assemblyMergeStrategy).value.apply(x)
//        }
//
//      case x@PathList("OSGI-INF", path@_*) =>
//        path map {
//          _.toLowerCase
//        } match {
//          case "l10n" :: "bundle.properties" :: Nil                           => MergeStrategy.concat
//          case _                                                               => (assembly / assemblyMergeStrategy).value.apply(x)
//        }
//
//      case "application.conf"                                                 => MergeStrategy.concat
//      case "module-info.class"                                                => MergeStrategy.discard
//      case x                                                                  => (assembly / assemblyMergeStrategy).value.apply(x)
//    },
//    excludeDependencies ++= Seq(
//			"org.slf4j"                        % "slf4j-log4j12",
//		),
//    dependencyOverrides ++= Seq(
//      "xml-apis"                         % "xml-apis"                                      % "1.4.01",
//    ),
//    libraryDependencies ++= Seq(
//      "ai.x"                            %% "play-json-extensions"                          % "0.42.0",
//      "com.amazonaws"                    % "aws-java-sdk-ses"                              % "1.11.1034",
//      "com.auth0"                        % "auth0"                                         % "1.14.2",
//      "com.channelape"                   % "shopify-sdk"                                   % "2.0.0" excludeAll(ExclusionRule(organization = "com.sun.xml.bind")),
//      "com.chargebee"                    % "chargebee-java"                                % "2.7.1",
//      "com.cloudbees.thirdparty"         % "zendesk-java-client"                           % "0.15.0",
//      "com.facebook.business.sdk"        % "facebook-java-business-sdk"                    % "6.0.1",
//      "com.fasterxml.jackson.module"    %% "jackson-module-scala"                          % "2.11.2",
//      "com.github.abashev"               % "vfs-s3"                                        % "4.0.0",
//      "com.github.docker-java"           % "docker-java"                                   % "3.2.12",
//      "com.github.docker-java"           % "docker-java-transport-okhttp"                  % "3.2.12",
//      "com.github.jknack"                % "handlebars"                                    % "4.3.0",
//      "com.github.melrief"              %% "purecsv"                                       % "0.1.1",
//      "com.github.pathikrit"            %% "better-files"                                  % "3.8.0",
//      "com.github.seratch"              %% "awscala-iam"                                   % "0.8.+",
//      "com.github.seratch"              %% "awscala-s3"                                    % "0.8.+",
//      "com.hierynomus"                   % "sshj"                                          % "0.32.0",
//      "com.hubspot.slack"                % "slack-java-client"
//      % "1.3",
//      "com.jcraft"                       % "jsch"                                          % "0.1.55",
//      "com.machinepublishers"            % "jbrowserdriver"                                % "1.1.0-RC2",
//      "com.mixpanel"                     % "mixpanel-java"
//      % "1.5.0",
//      "com.segment.analytics.java"       % "analytics"                                     % "2.1.1",
//      "com.sksamuel.avro4s"              % "avro4s-core_2.12"                              % "3.0.2",
//      "com.stripe"                       % "stripe-java"                                   % "20.86.1",
//      "com.unboundid"                    % "unboundid-ldapsdk"                             % "4.0.7",
//      "de.heikoseeberger"               %% "akka-log4j"                                    % "1.6.1",
//      "dev.fuxing"                       % "airtable-api"                                  % "0.3.1",
//      "io.airbyte"                       % "airbyte-api"                                   % "0.29.3-alpha",
//      "io.airbyte.airbyte-protocol"      % "models"                                        % "0.29.3-alpha",
//      "io.deepstream"                    % "deepstream.io-client-java"                     % "2.2.2",
//      "io.github.jasperroel"             % "SiteCrawler"                                   % "1.0.0",
//      "io.kubernetes"                    % "client-java"                                   % "13.0.1",
//      "io.netty"                         % "netty-resolver-dns-native-macos"               % "4.1.70.Final" classifier "osx-aarch_64",
//      "io.netty"                         % "netty-resolver-dns-native-macos"               % "4.1.70.Final" classifier "osx-x86_64",
//      "io.netty.incubator"               % "netty-incubator-transport-native-io_uring"     % "0.0.10.Final" classifier "linux-x86_64",
//      "io.scalaland"                    %% "chimney"                                       % "0.5.0",
//      "io.sentry"                        % "sentry"                                        % "1.7.28",
//      "io.vertx"                         % "vertx-auth-jwt"                                % "4.2.1",
//      "io.vertx"                         % "vertx-config-git"                              % "4.2.1",
//      "io.vertx"                         % "vertx-core"                                    % "4.2.1",
//      "io.vertx"                         % "vertx-health-check"                            % "4.2.1",
//      "io.vertx"                         % "vertx-micrometer-metrics"                      % "4.2.1",
//      "io.vertx"                         % "vertx-service-discovery-bridge-consul"         % "4.2.1",
//      "io.vertx"                         % "vertx-service-discovery-bridge-docker"         % "4.2.1",
//      "io.vertx"                         % "vertx-service-discovery-bridge-kubernetes"     % "4.2.1",
//      "io.vertx"                         % "vertx-tcp-eventbus-bridge"                     % "4.2.1",
//      "io.vertx"                         % "vertx-unit"                                    % "4.2.1",
//      "io.vertx"                         % "vertx-web"                                     % "4.2.1",
//      "io.vertx"                         % "vertx-web-client"                              % "4.2.1",
//      "io.vertx"                         % "vertx-web-sstore-cookie"                       % "4.2.1",
//      "io.vertx"                         % "vertx-web-templ-handlebars"                    % "4.2.1",
//      "io.vertx"                         % "vertx-zookeeper"                               % "4.2.1",
//      "io.youi"                          %% "youi-client"                                  % "0.13.17",
//      "net.lingala.zip4j"                 % "zip4j"                                        % "2.6.3",
//      "net.petitviolet"                  %% "ulid4s"                                       % "0.4.0",
//      "ognl"                              % "ognl"                                         % "3.2.11",
//      "org.alluxio"                       % "alluxio-core-client"                          % "2.6.2-2",
//      "org.alluxio"                       % "alluxio-core-client-fs"                       % "2.6.2-2",
//      "org.alluxio"                       % "alluxio-job-client"                           % "2.6.2-2",
//      "org.apache.calcite"                % "calcite-core"                                 % "1.24.0",
//      "org.apache.commons"                % "commons-email"                                % "1.5",
//      "org.apache.commons"                % "commons-vfs2"                                 % "2.4.1",
//      "org.eclipse.jgit"                  % "org.eclipse.jgit"                             % "5.5.1.201910021850-r",
//      "org.java-websocket"                % "Java-WebSocket"                               % "1.4.0",
//      "org.jgrapht"                       % "jgrapht-core"                                 % "1.3.1",
//      "org.jgrapht"                       % "jgrapht-ext"                                  % "1.3.1",
//      "org.json4s"                       %% "json4s-core"                                  % "3.6.12",
//      "org.json4s"                       %% "json4s-native"                                % "3.6.12",
//      "org.jsoup"                         % "jsoup"                                        % "1.12.1",
//      "org.knowm"                         % "sundial"                                      % "2.2.0",
//      "org.mongodb.scala"                %% "mongo-scala-driver"                           % "4.3.4",
//      "org.pac4j"                         % "pac4j-cas"                                    % "5.1.3",
//      "org.pac4j"                         % "pac4j-config"                                 % "5.1.3",
//      "org.pac4j"                         % "pac4j-http"                                   % "5.1.3",
//      "org.pac4j"                         % "pac4j-jwt"                                    % "5.1.3",
//      "org.pac4j"                         % "pac4j-ldap"                                   % "5.1.3",
//      "org.pac4j"                         % "pac4j-oauth"                                  % "5.1.3",
//      "org.pac4j"                         % "pac4j-oidc"                                   % "5.1.3",
//      "org.pac4j"                         % "pac4j-saml"                                   % "5.1.3",
//      "org.redisson"                      % "redisson"                                     % "3.13.6",
//      "org.scala-lang"                    % "scala-compiler"                               % scalaVersion.value % "provided",
//      "org.scala-lang"                    % "scala-reflect"                                % scalaVersion.value,
//      "org.scalacheck"                   %% "scalacheck"                                   % "1.14+" % Test,
//      "org.xerial.snappy"                 % "snappy-java"                                  % "1.1.7.3",
//      "org.zeroturnaround"                % "zt-zip"                                       % "1.13",
//      "io.skuber"                        %% "skuber"                                       % "2.6.0",
//    )
//  )
//
//lazy val root = haranaRootProject(modules)
