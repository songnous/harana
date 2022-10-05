package com.harana.sdk.shared.models.data

import DataSourceType.DataSourceTypeId
import com.harana.sdk.shared.models.flow.parameters.ParameterGroup
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._
import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}

@JsonCodec
case class DataSourceType(
  id: DataSourceTypeId,
  name: String,
  parameterGroups: List[ParameterGroup],
  syncDirection: SyncDirection,
  supportsIncremental: Boolean,
  supportsDBT: Boolean,
  supportsNormalization: Boolean)

object DataSourceType {
  type DataSourceTypeId = String
}

sealed abstract class SyncDirection(val value: String) extends StringEnumEntry
object SyncDirection extends StringEnum[SyncDirection] with StringCirceEnum[SyncDirection] {
  case object Source extends SyncDirection("source")
  case object Destination extends SyncDirection("destination")
  val values = findValues
}