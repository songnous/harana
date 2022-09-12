package com.harana.designer.backend.modules.projects.models

import com.harana.modules.argo.events.EventSource._
import io.circe.generic.JsonCodec

@JsonCodec
case class Trigger(name: String,
                   calendar: Option[Calendar],
                   file: Option[File],
                   github: Option[Github],
                   gitlab: Option[Gitlab],
                   hdfs: Option[Hdfs],
                   kafka: Option[Kafka],
                   redis: Option[Redis],
                   resource: Option[Resource],
                   slack: Option[Slack],
                   sns: Option[SNS],
                   sqs: Option[SQS],
                   stripe: Option[Stripe],
                   webhook: Option[Webhook])