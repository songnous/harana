package com.harana.designer.backend.services.setup

import com.harana.designer.backend.services.setup.Setup.Service
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import com.harana.modules.kubernetes.Kubernetes
import com.harana.modules.mongo.Mongo
import com.harana.sdk.shared.models.apps.{App => DesignerApp}
import com.harana.sdk.shared.models.common.{Background, Visibility}
import com.harana.sdk.shared.models.jwt.DesignerClaims
import org.mongodb.scala.Document
import zio.clock.Clock
import zio.{Task, ZLayer}

import scala.util.Random

object LiveSetup {
  val layer = ZLayer.fromServices { (clock: Clock.Service,
                                     config: Config.Service,
                                     kubernetes: Kubernetes.Service,
                                     logger: Logger.Service,
                                     micrometer: Micrometer.Service,
                                     mongo: Mongo.Service) => new Service {

    def createApps(claims: DesignerClaims): Task[Unit] =
      Task.whenM(mongo.count("Apps", Document("createdBy" -> claims.userId)).map(_ == 0))(
        for {
          _                   <- mongo.insert[DesignerApp]("Apps", DesignerApp("Jupyter", "", "Notebook", "467100553131.dkr.ecr.ap-southeast-2.amazonaws.com/apps-jupyter:1.0.0", 8888, Some(8888), Some(claims.userId), Visibility.Owner, Some(Background.Image(randomBackground)), Set("Notebook")))
          _                   <- mongo.insert[DesignerApp]("Apps", DesignerApp("RStudio", "", "Notebook", "467100553131.dkr.ecr.ap-southeast-2.amazonaws.com/apps-rstudio:1.0.0", 8787, Some(8787), Some(claims.userId), Visibility.Owner, Some(Background.Image(randomBackground)), Set("IDE")))
          // _                   <- mongo.insert[DesignerApp]("Apps", DesignerApp("Theia", "", "Notebook", "467100553131.dkr.ecr.ap-southeast-2.amazonaws.com/apps-theia:1.0.0", 3000, Some(3000), Some(claims.userId), Visibility.Owner, Background.Image(randomBackground), Set("Editor")))
        } yield ()
      )


    private def randomBackground =
      s"/public/images/pills/pill${Random.nextInt(96)+1}.jpg"


     def provisionSampleData: Task[Unit] = {
      null
    }
  }}
}