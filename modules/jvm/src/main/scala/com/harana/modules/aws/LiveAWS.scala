package com.harana.modules.aws

import java.io.InputStream
import java.util.concurrent.atomic.AtomicReference
import awscala._
import awscala.iam.{AccessKey, IAM}
import awscala.s3.{Bucket, S3, S3ObjectSummary}
import com.amazonaws.auth._
import com.amazonaws.services.simpleemail.{AmazonSimpleEmailServiceAsync, AmazonSimpleEmailServiceAsyncClient}
import com.amazonaws.services.identitymanagement.model.DeleteUserPolicyRequest
import com.amazonaws.services.s3.model.{GetObjectTaggingRequest, ObjectMetadata, ObjectTagging, SetObjectTaggingRequest, Tag}
import com.amazonaws.services.s3.transfer.TransferManagerBuilder
import com.amazonaws.services.simpleemail.model._
import com.harana.modules.aws.AWS.Service
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import io.circe.syntax._
import zio.blocking.Blocking

import scala.jdk.CollectionConverters._
import zio.{Has, Task, UIO, ZIO, ZLayer}

object LiveAWS {

  private val credentialsProviderRef = new AtomicReference[Option[AWSCredentialsProvider]](None)
  private val iamRef = new AtomicReference[Option[IAM]](None)
  private val s3Ref = new AtomicReference[Option[S3]](None)
  private val sesRef = new AtomicReference[Option[AmazonSimpleEmailServiceAsync]](None)

  val layer = ZLayer.fromServices { (blocking: Blocking.Service,
                                     config: Config.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) => new Service {

    private def credentialsProvider =
      for {
        provider <- if (credentialsProviderRef.get.nonEmpty) Task(credentialsProviderRef.get.get) else
          for {
            accessId                  <- config.secret("aws-access-id")
            secretKey                 <- config.secret("aws-secret-key")
            credentialsFile           <- config.optString("aws.credentialsFile")
            useCredentialsFile        <- config.boolean("aws.useCredentialsFile", default = false)
            useEnvironmentVariables   <- config.boolean("aws.useEnvironmentVariables", default = false)
            useInstanceProfile        <- config.boolean("aws.useInstanceProfile", default = false)
            profile                   <- config.optString("aws.profile")
            provider                  = (accessId, secretKey, credentialsFile, useCredentialsFile, useEnvironmentVariables, useInstanceProfile, profile) match {
                                          case (_, _, _, _, true, _, _) => new EnvironmentVariableCredentialsProvider()
                                          case (_, _, _, _, _, true, _) => InstanceProfileCredentialsProvider.getInstance()
                                          case (a, s, _, _, _, _, _) => new AWSStaticCredentialsProvider(new BasicAWSCredentials(a, s))
                                        }
          } yield provider
        _ = credentialsProviderRef.set(Some(provider))
      } yield provider


    private def iamClient =
      for {
        client <- if (iamRef.get.nonEmpty) Task(iamRef.get.get) else
                    for {
                      creds   <- credentialsProvider
                      iam     <- Task(IAM(creds))
                    } yield iam
                  _ = iamRef.set(Some(client))
      } yield client


    private def s3Client =
      for {
        client <- if (s3Ref.get.nonEmpty) Task(s3Ref.get.get) else
                    for {
                      creds   <- credentialsProvider
                      region  <- config.secret("aws-region")
                      s3      <- Task(S3(creds)(Region(region)))
                    } yield s3
             _ = s3Ref.set(Some(client))
      } yield client


    private def sesClient =
      for {
        client <- if (sesRef.get.nonEmpty) Task(sesRef.get.get) else
          for {
            creds     <- credentialsProvider
            region    <- config.secret("aws-region")
            client    <- UIO(AmazonSimpleEmailServiceAsyncClient.asyncBuilder().withCredentials(creds).withRegion(region).build())
          } yield client
        _ = sesRef.set(Some(client))
      } yield client


    private def s3Bucket(bucket: String): Task[Bucket] =
      for {
        client        <- s3Client
        bucket        <- ZIO.fromOption(client.bucket(bucket)).orElseFail(new Throwable("No available bucket"))
      } yield bucket


    def iamCreateS3User(name: String, bucket: String, prefix: String): Task[AccessKey] =
      for {
        client        <- iamClient
        user          <- Task(client.createUser(name))
        s3arn         =  s"arn:aws:s3:::$bucket/$prefix"
        policy        =  Policy(Seq(Statement(Effect.Allow, Seq(Action("s3:*")), Seq(Resource(s3arn)))))
        _             =  user.putPolicy(s"$name-s3", policy)(client)
        accessKey     =  user.createAccessKey()(client)
      } yield accessKey


    def iamDeleteUser(name: String): Task[Unit] =
      for {
        client        <- iamClient
        user          <- Task(client.user(name))
        _             <- Task(client.deleteUserPolicy(new DeleteUserPolicyRequest().withUserName(name).withPolicyName(s"$name-s3")))
        _             <- ZIO.foreach_(user)(u => Task(client.delete(u)))
      } yield ()


    def s3CreateBucket(name: String): Task[Unit] =
      for {
        client        <- s3Client
        _             <- Task(client.createBucket(name))
      } yield ()


    def s3List(bucket: String, prefix: Option[String]): Task[List[S3ObjectSummary]] =
      for {
        client        <- s3Client
        bucket        <- s3Bucket(bucket)
        summaries     <- Task(client.objectSummaries(bucket, prefix.getOrElse("")).toList)
      } yield summaries


    def s3ListAsStream(bucket: String, prefix: Option[String]): Task[Stream[Either[String, S3ObjectSummary]]] =
      for {
        client        <- s3Client
        bucket        <- s3Bucket(bucket)
        summaries     <- Task(client.ls(bucket, prefix.getOrElse("")))
      } yield summaries


    def s3ListTags(bucket: String, at: String): Task[Map[String, String]] =
      for {
        client        <- s3Client
        request       =  new GetObjectTaggingRequest(bucket, at)
        tagging       <- Task(client.getObjectTagging(request))
        tags          =  tagging.getTagSet.asScala.map(t => t.getKey -> t.getValue).toMap
      } yield tags


    def s3CopyFile(fromBucket: String, from: String, toBucket: Option[String], to: String): Task[Unit] =
      for {
        client        <- s3Client
        _             <- Task(client.copyObject(fromBucket, from, toBucket.getOrElse(fromBucket), to))
      } yield ()


    def s3CopyFolder(fromBucket: String, from: String, toBucket: Option[String], to: String): Task[Unit] =
      for {
        client        <- s3Client
        manager       <- Task {
                            val tm = TransferManagerBuilder.standard
                            tm.setS3Client(client)
                            tm.build
                          }
        fromFiles     <- Task(client.listObjects(fromBucket, s"$from/").getObjectSummaries.asScala.toList)
        _             <- ZIO.foreachPar_(fromFiles) { file =>
                            for {
                              filename <- UIO(file.getKey.replace(s"$from/", ""))
                              _ <- logger.debug(s"Copying $fromBucket/${file.getKey} to $toBucket/$to/$filename")
                              _ <- Task(manager.copy(fromBucket, file.getKey, toBucket.getOrElse(fromBucket), s"$to/$filename").waitForCompletion()).when(!filename.isEmpty)
                            } yield ()
                          }
      } yield ()


    def s3Move(fromBucket: String, from: String, toBucket: Option[String], to: String): Task[Unit] =
      for {
        _             <- s3CopyFile(fromBucket, from, toBucket, to)
        _             <- s3Delete(fromBucket, from)
      } yield ()


    def s3Rename(bucket: String, from: String, to: String): Task[Unit] =
      s3Move(bucket, from, None, to)


    def s3Get(bucket: String, at: String): Task[InputStream] =
      for {
        client        <- s3Client
        bucket        <- s3Bucket(bucket)
        inputStream   <- Task(client.getObject(bucket, at).get.content)
      } yield inputStream


    def s3Put(bucket: String, at: String, inputStream: InputStream, contentLength: Long): Task[Unit] =
      for {
        client        <- s3Client
        metadata      =  new ObjectMetadata()
        _             =  metadata.setContentLength(contentLength)
        _             <- Task(client.putObject(bucket, at, inputStream, metadata))
      } yield ()


    def s3Delete(bucket: String, at: String): Task[Unit] =
      for {
        client        <- s3Client
        _             <- Task(client.deleteObject(bucket, at))
      } yield ()


    def s3Tag(bucket: String, at: String, tags: Map[String, String]): Task[Unit] =
      for {
        client        <- s3Client
        s3Tags        =  tags.map { case (k, v) => new Tag(k, v) }
        request       =  new SetObjectTaggingRequest(bucket, at, new ObjectTagging(s3Tags.toList.asJava))
        _             <- Task(client.setObjectTagging(request))
      } yield ()


    def sesCreateTemplate(template: Template): Task[Unit] =
      for {
        client              <- sesClient
        request             =  new CreateTemplateRequest().withTemplate(template)
        -                   <- ZIO.fromFutureJava(client.createTemplateAsync(request)).provide(Has(blocking))
      } yield ()


    def sesDeleteTemplate(name: String): Task[Unit] =
      for {
        client              <- sesClient
        request             =  new DeleteTemplateRequest().withTemplateName(name)
        -                   <- ZIO.fromFutureJava(client.deleteTemplateAsync(request)).provide(Has(blocking))
      } yield ()


    def sesSendEmail(message: Message, to: List[String], cc: List[String], bcc: List[String], sender: String, replyTo: List[String] = List()): Task[Unit] =
      for {
        client              <- sesClient
        destination         =  new Destination().withBccAddresses(bcc.asJava).withCcAddresses(cc.asJava).withToAddresses(to.asJava)
        configurationSet    <- config.string("aws.ses.configurationSet", "default")
        request             =  new SendEmailRequest()
                                .withConfigurationSetName(configurationSet)
                                .withSource(sender)
                                .withDestination(destination)
                                .withMessage(message)
                                .withReplyToAddresses(replyTo.asJava)
        -                   <- ZIO.fromFutureJava(client.sendEmailAsync(request)).provide(Has(blocking))
      } yield ()


    def sesSendTemplatedEmail(template: String, templateValues: Map[String, String], to: List[String], cc: List[String], bcc: List[String], sender: String, replyTo: List[String] = List()): Task[Unit] =
      for {
        client              <- sesClient
        destination         =  new Destination().withBccAddresses(bcc.asJava).withCcAddresses(cc.asJava).withToAddresses(to.asJava)
        configurationSet    <- config.string("aws.ses.configurationSet", "default")
        request             =  new SendTemplatedEmailRequest()
                                .withConfigurationSetName(configurationSet)
                                .withSource(sender)
                                .withDestination(destination)
                                .withReplyToAddresses(replyTo.asJava)
                                .withTemplate(template)
                                .withTemplateData(templateValues.asJson.noSpaces)
        -                   <- ZIO.fromFutureJava(client.sendTemplatedEmailAsync(request)).provide(Has(blocking))
      } yield ()


    def sesSendBulkTemplatedEmail(template: String, toWithTemplateValues: List[(String, Map[String, String])], sender: String, replyTo: List[String] = List()): Task[Unit] =
      for {
        client              <- sesClient
        configurationSet    <- config.string("aws.ses.configurationSet", "default")
        destinations        =  toWithTemplateValues.map(t =>
                                  new BulkEmailDestination()
                                    .withDestination(new Destination().withToAddresses(List(t._1).asJava))
                                    .withReplacementTemplateData(t._2.asJson.noSpaces)
                               ).asJavaCollection
        request             =  new SendBulkTemplatedEmailRequest()
                                .withConfigurationSetName(configurationSet)
                                .withSource(sender)
                                .withDestinations(destinations)
                                .withDefaultTemplateData("{}")
                                .withTemplate(template)
                                .withReplyToAddresses(replyTo.asJava)
        -                   <- ZIO.fromFutureJava(client.sendBulkTemplatedEmailAsync(request)).provide(Has(blocking))
      } yield ()

  }}
}