package com.harana.sdk.shared.models.flow.execution.spark

import io.circe.generic.JsonCodec

@JsonCodec
case class SparkMetrics(info: Option[ApplicationInfo],
                        metrics: Option[AggregateMetrics],
                        executorCount: Option[Int],
                        executorMemory: Option[Int],
                        maximumExecutorCount: Option[Int],
                        maximumExecutorMemory: Option[Int],
                        coresPerExecutor: Option[Int],
                        hostMap: Map[String, HostTimespan],
                        executorMap: Map[String, ExecutorTimespan],
                        jobMap: Map[Long, JobTimespan],
                        stageMap: Map[Int, StageTimespan])

