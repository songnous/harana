package com.harana.sdk.shared.models.flow.parameters

object FileFormat extends Enumeration {

  type FileFormat = Value

  val CSV = Value("CSV")
  val PARQUET = Value("PARQUET")
  val JSON = Value("JSON")

}
