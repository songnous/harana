name := "executor"

//import Dependencies._
//import sbt.Keys.libraryDependencies
//import sbt._
//
//lazy val executor = haranaProject("executor").in(file("."))
//  .settings(
//    name := "harana-executor",
//		maintainer := "Harana <support@harana.com>",
//		packageSummary := "Harana Designer",
//    version := "0.0.1",
//    libraryDependencies ++= List(
//      "com.harana"                         %% "modules"                                     % "1.0.0",
//      "org.apache.spark"                   %% "spark-core"                                  % "2.4.7",
//      "org.apache.hadoop"                   % "hadoop-aws"                                  % "2.10.1",
//      "org.apache.hadoop"                   % "hadoop-client"                               % "2.10.1"
//    ) ++ bigQuery
//      ++ cassandra
//      ++ dgraph
//      ++ druid
//      ++ dynamoDb
//      ++ exasol
//      ++ hazelcast
//      ++ infinispan
//      ++ mongoDb
//      ++ mySql
//      ++ oracle
//      ++ postgreSql
//      ++ quickbooks
//      ++ redis
//      ++ snowflake
//      ++ sqlServer
//      ++ trino
//  )