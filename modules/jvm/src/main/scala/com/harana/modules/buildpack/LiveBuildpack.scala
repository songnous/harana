package com.harana.modules.buildpack

import java.io.File
import java.util.Locale

import com.harana.modules.buildpack.Buildpack.Service
import com.harana.modules.core.config.Config
import com.harana.modules.core.logger.Logger
import zio.blocking.Blocking
import zio.process.Command
import zio.{Has, Task, UIO, ZLayer}

import scala.collection.mutable

object LiveBuildpack {
  val layer = ZLayer.fromServices { (blocking: Blocking.Service,
                                     config: Config.Service,
                                     logger: Logger.Service) => new Service {
    private val buildpackCmd = Task {
      val os = System.getProperty("os.name").toLowerCase(Locale.ROOT)
      val url = {
        if (os.contains("mac")) getClass.getResource("pack/mac/pack")
        if (os.contains("win")) getClass.getResource("pack/windows/pack.exe")
        getClass.getResource("pack/linux/pack")
      }
      url.getFile
    }


    def build(name: String,
              path: File,
              builder: Option[String] = None,
              environmentVariables: Map[String, String] = Map(),
              mountedVolumes: Map[File, File] = Map(),
              network: Option[String] = None,
              publish: Option[Boolean] = None,
              runImage: Option[String] = None): Task[List[String]] =
      for {
        cmd <- buildpackCmd
        args <- UIO {
          val args = mutable.ListBuffer[String]("build", "name", s"--path ${path.getAbsolutePath}")
          if (builder.nonEmpty) args += s"--builder ${builder.get}"
          if (environmentVariables.nonEmpty) args += s"--env ${environmentVariables.map { case (k,v) => s"$k=$v" }.mkString(",")}"
          if (mountedVolumes.nonEmpty) args += s"--volume ${mountedVolumes.map { case (k,v) => s"$k:$v" }.mkString(",")}"
          if (network.nonEmpty) args += s"--network ${network.get}"
          if (publish.nonEmpty) args += s"--publish"
          if (runImage.nonEmpty) args += s"--run-image ${runImage.get}s"
          args
        }
        cmd <- Command(cmd, args.toSeq: _*).lines.provide(Has(blocking))
      } yield cmd.toList


    def setDefaultBuilder(name: String): Task[List[String]] =
      Command("pack", List("set-default-builder", name): _*).lines.provide(Has(blocking)).map(_.toList)


    def rebase(name: String,
               publish: Option[Boolean] = None,
               runImage: Option[String] = None): Task[List[String]] =
      for {
        cmd <- buildpackCmd
        args <- UIO {
          val args = mutable.ListBuffer[String]()
          if (publish.nonEmpty) args += s"--publish"
          if (runImage.nonEmpty) args += s"--run-image ${runImage.get}s"
          args
        }
        cmd <- Command(cmd, args.toSeq: _*).lines.provide(Has(blocking))
      } yield cmd.toList
  }}
}