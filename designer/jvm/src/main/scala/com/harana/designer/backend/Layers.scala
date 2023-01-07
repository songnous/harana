package com.harana.designer.backend

import com.harana.designer.backend.services.apps.LiveApps
import com.harana.designer.backend.services.data.LiveData
import com.harana.designer.backend.services.datasets.LiveDataSets
import com.harana.designer.backend.services.datasources.LiveDataSources
import com.harana.designer.backend.services.events.LiveEvents
import com.harana.designer.backend.services.files.LiveFiles
import com.harana.designer.backend.services.flowexecutions.LiveFlowExecutions
import com.harana.designer.backend.services.flows.LiveFlows
import com.harana.designer.backend.services.help.LiveHelp
import com.harana.designer.backend.services.schedules.LiveSchedules
import com.harana.designer.backend.services.schedules.argo.LiveArgoScheduler
import com.harana.designer.backend.services.setup.LiveSetup
import com.harana.designer.backend.services.system.LiveSystem
import com.harana.designer.backend.services.terminals.LiveTerminals
import com.harana.designer.backend.services.user.LiveUser
import com.harana.id.jwt.{Layers => JWTLayers}
import com.harana.modules.{Layers => ModuleLayers}
import com.harana.modules.core.{Layers => CoreLayers}
import com.harana.modules.vertx.LiveVertx
import com.harana.modules.vfs.LiveVfs
import zio.blocking.Blocking
import zio.clock.Clock

object Layers {

    // FIXME: Airbyte requires CoreLayers.standard when it shouldn't
  val dataSources = (CoreLayers.standard ++ (CoreLayers.standard >>> ModuleLayers.airbyte) ++ JWTLayers.jwt ++ ModuleLayers.mongo) >>> LiveDataSources.layer
  val dataSets = (CoreLayers.standard ++ JWTLayers.jwt ++ ModuleLayers.mongo) >>> LiveDataSets.layer
  val system = (CoreLayers.standard ++ Clock.live ++ JWTLayers.jwt ++ ModuleLayers.mongo ++ Layers.vertx) >>> LiveSystem.layer
  val execution = (CoreLayers.standard ++ ModuleLayers.mongo) >>> LiveFlowExecutions.layer
  val events = (CoreLayers.standard ++ JWTLayers.jwt ++ ModuleLayers.mongo) >>> LiveEvents.layer
  val flows = (ModuleLayers.kubernetes ++ CoreLayers.standard ++ JWTLayers.jwt ++ ModuleLayers.mongo) >>> LiveFlows.layer
  val setup = (CoreLayers.standard ++ Clock.live ++ ModuleLayers.kubernetes ++ ModuleLayers.mongo) >>> LiveSetup.layer
  val vertx = (Blocking.live ++ CoreLayers.standard ++ CoreLayers.cache) >>> LiveVertx.layer
  val user = (CoreLayers.standard ++ ModuleLayers.aws ++ JWTLayers.jwt ++ ModuleLayers.kubernetes ++ ModuleLayers.mongo ++ vertx) >>> LiveUser.layer
  val apps = (CoreLayers.standard ++ Clock.live ++ ModuleLayers.docker ++ JWTLayers.jwt ++ ModuleLayers.kubernetes ++ ModuleLayers.mongo ++ vertx) >>> LiveApps.layer
  val data = (CoreLayers.standard ++ ModuleLayers.alluxioFs ++ JWTLayers.jwt ++ vertx) >>> LiveData.layer
  val files = (CoreLayers.standard ++ JWTLayers.jwt ++ ModuleLayers.kubernetes ++ vertx ++ LiveVfs.layer) >>> LiveFiles.layer

  val argoScheduler = (CoreLayers.standard ++ ModuleLayers.argo ++ ModuleLayers.kubernetes) >>> LiveArgoScheduler.layer
  val schedules = (CoreLayers.standard ++ JWTLayers.jwt ++ argoScheduler ++ ModuleLayers.mongo ++ vertx) >>> LiveSchedules.layer
  val terminals = (CoreLayers.standard ++ JWTLayers.jwt ++ ModuleLayers.kubernetes ++ ModuleLayers.mongo ++ vertx) >>> LiveTerminals.layer
  val help = (CoreLayers.standard ++ JWTLayers.jwt ++ ModuleLayers.mongo) >>> LiveHelp.layer

}