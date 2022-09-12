package com.harana.designer.shared.flows.actiontypes.spark

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup, ParameterValue}
import com.harana.sdk.backend.models.designer.flow.ActionType
import com.harana.sdk.backend.models.designer.flow.actiontypes._

object Input {

   val types = List(
      jdbcInputActionType("mariadb", "mariadb"),
      jdbcInputActionType("mysql", "mysql"),
      jdbcInputActionType("oracle", "oracle"),
      jdbcInputActionType("postgresql", "postgresql"),
      jdbcInputActionType("redshift", "redshift"),
      jdbcInputActionType("sql-server", "sql-server"),

      ActionType("input-cassandra", Set(), "general", nonePort, outputPort, parameterGroups = List(
         ParameterGroup("general", List(
            Parameter.DataSource("data-source", dataSourceType, required = true),
            Parameter.String("keyspace", required = true),
            Parameter.String("table", required = true)
         )),

         ParameterGroup("advanced", List(
            Parameter.String("consistency-level", options =
               List(
                  ("all", ParameterValue.String("ALL")),
                  ("quorum", ParameterValue.String("QUORUM")),
                  ("local-quorum", ParameterValue.String("LOCAL_QUORUM")),
                  ("one", ParameterValue.String("ONE")),
                  ("two", ParameterValue.String("TWO")),
                  ("local-one", ParameterValue.String("LOCAL_ONE")),
                  ("serial", ParameterValue.String("SERIAL")),
                  ("local-serial", ParameterValue.String("LOCAL_SERIAL"))
               )),
            Parameter.Long("ttl", placeholder = Some(0)),
            Parameter.Long("concurrent-reads"),
            Parameter.Long("fetch-size-in-rows"),
            Parameter.Boolean("record-metrics"),
            Parameter.Long("max-reads-per-sec"),
            Parameter.Long("split-size-in-mb"),
            Parameter.Long("throughput-mb-per-sec"),
            Parameter.String("direct-join-setting", options =
               List(
                  ("on", ParameterValue.String("on")),
                  ("off", ParameterValue.String("off")),
                  ("auto", ParameterValue.String("auto"))
               )),
            Parameter.Decimal("direct-join-size-ratio"),
            Parameter.Boolean("ignore-missing-meta-columns")
         ))
      )),

      ActionType("input-mongodb", Set(), "general", nonePort, outputPort, parameterGroups = List(
         ParameterGroup("general", List(
            Parameter.DataSource("data-source", dataSourceType, required = true),
            Parameter.String("database", required = true),
            Parameter.String("collection", required = true)
         )),
         ParameterGroup("advanced", List(
            Parameter.Integer("batch-size"),
            Parameter.Integer("local-threshold-in-ms", Some(ParameterValue.Integer(15))),
            Parameter.String("read-preference", options = List(
               ("primary", ParameterValue.String("primary")),
               ("primary-preferred", ParameterValue.String("primaryPreferred")),
               ("secondary", ParameterValue.String("secondary")),
               ("secondary-preferred", ParameterValue.String("secondaryPreferred")),
               ("nearest", ParameterValue.String("nearest")),
            )),
            Parameter.String("read-concern", options = List(
               ("local", ParameterValue.String("local")),
               ("available", ParameterValue.String("available")),
               ("majority", ParameterValue.String("majority")),
               ("linearizable", ParameterValue.String("linearizable")),
               ("snapshot", ParameterValue.String("snapshot"))
            )),
            Parameter.Integer("sample-size", Some(ParameterValue.Integer(1000))),
            Parameter.Integer("sample-pool-size", Some(ParameterValue.Integer(10000)))
         ))
      )),

      ActionType("input-snowflake", Set(), "general", nonePort, outputPort, parameterGroups = List(
         ParameterGroup("general", List(
            Parameter.DataSource("data-source", dataSourceType, required = true),
            Parameter.String("mode", required = true, options = List(
               ("query", ParameterValue.String("query")),
               ("table", ParameterValue.String("table"))
            )),
            Parameter.String("query", multiLine = true),
            Parameter.String("table")
         )),
         ParameterGroup("advanced", List(
            Parameter.Boolean("compress"),
            Parameter.Long("max-file-size", Some(ParameterValue.Long(10))),
            Parameter.Long("parallelism", Some(ParameterValue.Long(4)))
         ))
      ))
   )
}