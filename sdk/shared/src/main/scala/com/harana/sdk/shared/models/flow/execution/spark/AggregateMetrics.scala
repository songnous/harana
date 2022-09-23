  package com.harana.sdk.shared.models.flow.execution.spark

import enumeratum._
import io.circe.generic.JsonCodec

@JsonCodec
case class AggregateMetrics(count: Long,
                            metrics: Map[AggregateMetric, AggregateValue])

sealed trait AggregateMetric extends EnumEntry
case object AggregateMetric extends Enum[AggregateMetric] with CirceKeyEnum[AggregateMetric] {
  case object ShuffleWriteTime extends AggregateMetric
  case object ShuffleWriteBytesWritten extends AggregateMetric
  case object ShuffleWriteRecordsWritten extends AggregateMetric
  case object ShuffleReadFetchWaitTime extends AggregateMetric
  case object ShuffleReadBytesRead extends AggregateMetric
  case object ShuffleReadRecordsRead extends AggregateMetric
  case object ShuffleReadLocalBlocks extends AggregateMetric
  case object ShuffleReadRemoteBlocks extends AggregateMetric
  case object ExecutorRuntime extends AggregateMetric
  case object JvmGCTime extends AggregateMetric
  case object ExecutorCpuTime extends AggregateMetric
  case object ResultSize extends AggregateMetric
  case object InputBytesRead extends AggregateMetric
  case object OutputBytesWritten extends AggregateMetric
  case object MemoryBytesSpilled extends AggregateMetric
  case object DiskBytesSpilled extends AggregateMetric
  case object PeakExecutionMemory extends AggregateMetric
  case object TaskDuration extends AggregateMetric
  val values = findValues
}