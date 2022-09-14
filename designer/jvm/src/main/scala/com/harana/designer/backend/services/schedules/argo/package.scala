package com.harana.designer.backend.services.schedules

import com.harana.designer.backend.services.flows.argo.models.Trigger
import com.harana.modules.argo.{EnvironmentVariable, ObjectMetadata, Requests, Resources, VolumeMount}
import com.harana.modules.argo.events.EventSource.EventSource
import com.harana.modules.argo.events.{EventSource, Sensor}
import com.harana.modules.argo.events.Sensor.{EventDependency, Http, K8SResource, Sensor, Subscription, TriggerTemplate}
import com.harana.modules.argo.events.Trigger.{K8SSource, K8STrigger}
import com.harana.modules.argo.{Container => ArgoContainer, Template => ArgoTemplate}
import com.harana.modules.argo.workflows.{DAG, DAGTask, Template, Workflow}
import com.harana.sdk.shared.models.flow.Flow
import zio.UIO
import io.scalaland.chimney.dsl._

package object argo {

  def pipelineTriggers(schedule: Flow): UIO[List[(models.Pipeline, List[Trigger])]] =
    UIO(flow.pipelines.map(_.map(p => (p, p.start.triggers.getOrElse(List())))).getOrElse(List()))


  def argoEventSources(eventBusName: String, schedule: Scheulde): UIO[List[EventSource]] =
    for {
      triggers      <- pipelineTriggers(flow).map(_.flatMap(_._2))
      eventSources  <- UIO.foreach(triggers) { trigger =>
        for {
          eventSourceName   <- UIO(s"${name(flow.title)}-${name(trigger.name)}-eventsource")
          spec              =  EventSource.Spec(
            calendar = trigger.calendar.map(c => Map(trigger.name -> c)).getOrElse(Map()),
            eventBusName = eventBusName,
            file = trigger.file.map(c => Map(trigger.name -> c)).getOrElse(Map()),
            github = trigger.github.map(c => Map(trigger.name -> c)).getOrElse(Map()),
            gitlab = trigger.gitlab.map(c => Map(trigger.name -> c)).getOrElse(Map()),
            hdfs = trigger.hdfs.map(c => Map(trigger.name -> c)).getOrElse(Map()),
            kafka = trigger.kafka.map(c => Map(trigger.name -> c)).getOrElse(Map()),
            redis = trigger.redis.map(c => Map(trigger.name -> c)).getOrElse(Map()),
            resource = trigger.resource.map(c => Map(trigger.name -> c)).getOrElse(Map()),
            slack = trigger.slack.map(c => Map(trigger.name -> c)).getOrElse(Map()),
            sns = trigger.sns.map(c => Map(trigger.name -> c)).getOrElse(Map()),
            sqs = trigger.sqs.map(c => Map(trigger.name -> c)).getOrElse(Map()),
            stripe = trigger.stripe.map(c => Map(trigger.name -> c)).getOrElse(Map()),
            webhook = trigger.webhook.map(c => Map(trigger.name -> c)).getOrElse(Map())
          )
          eventSource       =  EventSource(eventSourceName, spec)
        } yield eventSource
      }
    } yield eventSources


  def argoSensors(flow: Flow): UIO[List[Sensor]] =
    for {
      pipelineTriggers      <- pipelineTriggers(flow).map(pt => pt.filter(_._2.isEmpty))
      argoContainer         =  ArgoContainer(
                                name = "sensor",
                                image = "argoproj/sensor:v0.13.0",
                                imagePullPolicy = Some("Always")
                              )
      template              =  ArgoTemplate(container = Some(argoContainer))
      sensors               <- UIO.foreach(pipelineTriggers){ pipelineTrigger =>
        for {
          prefix        <- UIO(s"${name(flow.title)}-${name(pipelineTrigger._1.name)}")
          dependencies  =  pipelineTrigger._2.map(t => EventDependency(s"${name(t.name)}-gateway", s"${name(flow.title)}-${name(t.name)}-gateway", "example"))
          subscription  =  Subscription(Some(Http(9300)))
          metadata      =  ObjectMetadata(generateName = Some(s"$prefix-workflow-"))
          workflow      <- argoWorkflow(flow, pipelineTrigger._1)
          k8sResource   =  K8SResource("argoproj.io/v1alpha1", "Workflow", metadata, workflow)
          k8sTrigger    =  K8STrigger("argoproj.io", "v1alpha1", "workflows", "create", K8SSource(k8sResource))
          triggers      =  List(Sensor.Trigger(template = TriggerTemplate(s"$prefix-workflow", k8s = Some(k8sTrigger))))
          sensor        =  Sensor(s"$prefix-workflow", Sensor.Spec(Some(template), dependencies, subscription = Some(subscription), triggers = triggers))
        } yield sensor
      }
    } yield sensors


  def name(title: String) =
    title.toLowerCase.replaceAll("\\s", "-")
}