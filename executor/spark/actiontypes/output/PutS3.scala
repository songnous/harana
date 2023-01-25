package com.harana.executor.spark.actiontypes.output

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.designer.data.DataSourceTypes.S3._
import com.harana.sdk.backend.models.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.flow.actiontypes.output.PutS3Info
import com.harana.sdk.backend.models.flow.actiontypes.{fileNameParameter, formatParameter, pathParameter}
import com.harana.sdk.backend.models.flow.execution.ExecutionError
import com.harana.sdk.backend.models.flow.{ActionType, FlowContext}
import com.harana.executor.spark.actiontypes.{dataSourceParameterValues, log, param, writeFile}
import com.harana.executor.spark.utils.PathUtils
import zio.{IO, Task, UIO}

class PutS3 extends PutS3Info with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, values: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] =
    for {
      spark                 <- UIO(context.sparkSession)
      dsParameterValues     =  dataSourceParameterValues(context, values, dataSourceType, dataSourceParameter)
      bucket                <- param(dsParameterValues, bucketParameter)
      accessKey             <- param(dsParameterValues, accessKeyParameter)
      secretKey             <- param(dsParameterValues, secretKeyParameter)
      region                <- param(dsParameterValues, regionParameter)
      path                  <- param(values, pathParameter)
      fileName              <- param(values, fileNameParameter)
      format                <- param(values, formatParameter)
      inputDf               =  inputs.get(inputPorts.head)

      _                     <- IO.foreach(List("s3", "s3a", "s3n")){ scheme =>
                                UIO {
                                  spark.sparkContext.hadoopConfiguration.set(s"fs.$scheme.awsAccessKeyId", accessKey.get)
                                  spark.sparkContext.hadoopConfiguration.set(s"fs.$scheme.access.key", accessKey.get)
                                  spark.sparkContext.hadoopConfiguration.set(s"fs.$scheme.awsSecretAccessKey", secretKey.get)
                                  spark.sparkContext.hadoopConfiguration.set(s"fs.$scheme.secret.key", secretKey.get)
                                  spark.sparkContext.hadoopConfiguration.set(s"fs.$scheme.endpoint", s"s3.${region.get}.amazonaws.com")
                                }
                              }
      s3Path                <- UIO(s"s3a://${bucket.get}/${PathUtils.path(path, fileName)}")

      _                     <- inputDf match {
                                case Some(df) => writeFile(spark, df, format.get, s3Path).mapError(ExecutionError.Unknown)
                                case None => Task.unit
                              }

      _                     <- IO.when(inputDf.nonEmpty)(log(inputDf.get, values))
      output                <- IO.none
    } yield output
}