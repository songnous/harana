package com.harana.designer.shared.flows.actiontypes.spark.output

import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup}
import com.harana.sdk.shared.models.designer.data.DataSourceTypes
import com.harana.designer.shared.flows.actiontypes.spark._

class PutS3Info extends OutputActionTypeInfo {

  val tags = Set("aws")
  val dataSourceType = DataSourceTypes.S3

  // General
  val dataSourceParameter = Parameter.DataSource("data-source", dataSourceType, required = true)
  val generalGroup = ParameterGroup("general", List(dataSourceParameter, pathParameter, fileNameParameter, formatParameter))

  // Advanced
  val encryptionParameter = Parameter.String("encryption", options = List(
    ("none", "none"),
    ("aes256", "AES256")
  ))
  val advancedGroup = ParameterGroup("advanced", List(encryptionParameter))

  val parameterGroups = List(generalGroup, advancedGroup, logGroup)

}