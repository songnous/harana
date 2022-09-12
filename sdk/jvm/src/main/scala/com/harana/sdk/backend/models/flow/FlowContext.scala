package com.harana.sdk.backend.models.flow

import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.data.DataSource
import DataSource.DataSourceId
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

case class FlowContext(sparkSession: SparkSession,
                       sparkContext: SparkContext,
                       dataSources: Map[DataSourceId, DataSource],
                       userId: UserId,
                       haranaFilesPath: String,
                       temporaryPath: String)