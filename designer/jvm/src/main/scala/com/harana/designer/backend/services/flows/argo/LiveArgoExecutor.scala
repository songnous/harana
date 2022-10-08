package com.harana.designer.backend.services.flows.argo

import com.harana.designer.backend.services.flows.argo.ArgoExecutor.Service
import com.harana.id.jwt.modules.jwt.JWT
import com.harana.modules.argo.Argo
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.kubernetes.Kubernetes
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.flow.Flow
import zio.{Task, ZLayer}

object LiveArgoExecutor {
  val layer = ZLayer.fromServices { (argo: Argo.Service,
                                     config: Config.Service,
                                     jwt: JWT.Service,
                                     kubernetes: Kubernetes.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service) =>
    new Service {

//      def deploy(flow: Flow, userId: UserId): Task[Unit] =
//        for {
//          namespace <- config.string("designer.flows.namespace")
////          workflow <- argoWorkflow(flow)
////          _ <- argo.createOrUpdateWorkflow(namespace, workflow)
//        } yield ()


//      def undeploy(flow: Flow, userId: UserId): Task[Unit] =
//        for {
//          namespace <- config.string("designer.flows.namespace")
//          workflow <- argoWorkflow(flow)
//          _ <- argo.deleteWorkflow(namespace, workflow.metadata.name)
//        } yield ()


      //    private def dependencies(flow: Flow, action: Action): List[String] = {
      //      flow.links.filter(_.toAction == action.id).map(_.fromAction)
      //    }
      //
      //
      //    private def argoWorkflow(flow: Flow): UIO[ArgoWorkflow] =
      //      for {
      //        entrypoint        <- UIO("")
      //        actionTypes       =  flow.actions.map(_.actionType)
      //        containers        =  actionTypes.map(_.container.into[ArgoContainer].transform)
      //        templates         =  containers.map(c => Template(container = Some(c), name = c.name))
      //        dagTask           =  flow.actions.map(a => DAGTask(name = Some(a.id), dependencies = dependencies(flow, a), template = Some(a.id)))
      //      }
      //
      //
      //      for {
      //        entrypoint        <- UIO(pipeline.start.action)
      //        containers        <- UIO.foreach(pipeline.actions.map(_.container))(argoContainer(flow, _))
      //        templates         =  containers.map(c => Template(container = Some(c), name = c.name))
      //        dagTasks          =  pipeline.actions.map(a => DAGTask(name = Some(a.name), dependencies = a.dependencies.getOrElse(List()), template = Some(a.name)))
      //        dagTemplate       =  Template(name = pipeline.name, dag = Some(DAG(tasks = dagTasks)))
      //        workflowSpec      =  Workflow.Spec(entrypoint = Some(entrypoint), templates = templates :+ dagTemplate)
      //        workflow          =  Workflow(name(flow.title), workflowSpec)
      //      } yield workflow
      //    }
      //
      //
      //    private def name(title: String) =
      //      title.toLowerCase.replaceAll("\\s", "-")
      //    }
    }
  }
}