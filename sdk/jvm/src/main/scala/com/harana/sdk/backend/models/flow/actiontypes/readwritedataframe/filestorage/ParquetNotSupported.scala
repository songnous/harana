package com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.filestorage

import com.harana.sdk.backend.models.flow.actiontypes.readwritedataframe.FileScheme
import com.harana.sdk.shared.models.flow.exceptions.FlowError
import io.circe.generic.JsonCodec

@JsonCodec
case class ParquetNotSupported() extends FlowError {
  val supportedScheme = FileScheme.supportedByParquet.mkString("[", ",", "]")
  val message = s"Parquet file format supported only with $supportedScheme file schemes"
}