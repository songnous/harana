package com.harana.executor.spark.actiontypes.input

import com.harana.sdk.shared.models.common.Parameter.ParameterValues
import com.harana.sdk.shared.models.designer.data.DataSourceTypes.S3._
import com.harana.sdk.backend.models.designer.flow.ActionType.{Inputs, Outputs}
import com.harana.sdk.backend.models.designer.flow.actiontypes.input.GetS3Info
import com.harana.sdk.backend.models.designer.flow.actiontypes.{fileNameParameter, formatParameter, pathParameter}
import com.harana.sdk.backend.models.designer.flow.execution.ExecutionError
import com.harana.sdk.backend.models.designer.flow.{ActionType, FlowContext}
import com.harana.executor.spark.actiontypes.{dataSourceParameterValues, log, param}
import com.harana.executor.spark.utils.PathUtils
import zio.{IO, UIO}

class GetS3 extends GetS3Info with ActionType {

  def validate(parameters: ParameterValues, context: FlowContext): UIO[List[ExecutionError]] = null

  def execute(inputs: Inputs, parameterValues: ParameterValues, context: FlowContext): IO[ExecutionError, Option[Outputs]] =
    for {
      spark                 <- UIO(context.sparkSession)
      dsParameterValues     =  dataSourceParameterValues(context, parameterValues, dataSourceType, dataSourceParameter)
      bucket                <- param(dsParameterValues, bucketParameter)
      accessKey             <- param(dsParameterValues, accessKeyParameter)
      secretKey             <- param(dsParameterValues, secretKeyParameter)
      region                <- param(dsParameterValues, regionParameter)
      path                  <- param(parameterValues, pathParameter)
      fileName              <- param(parameterValues, fileNameParameter)
      format                <- param(parameterValues, formatParameter)  

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
      outputDf              <- readFile(spark, format.get, s3Path).mapError(e => ExecutionError.Unknown(e))

      _                     <- log(outputDf, parameterValues)
      output                <- IO.some(new Outputs(Map(outputPorts.head -> outputDf)))
    } yield output
}