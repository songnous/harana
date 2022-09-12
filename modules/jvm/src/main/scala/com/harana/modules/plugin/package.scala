package com.harana.modules

import java.io.File
import java.nio.file.{Files, Path, Paths}

import com.harana.modules.plugin.models.PluginError
import org.osgi.framework.BundleContext
import zio.{IO, Task, UIO}

package object plugin {

  def installPlugin(bundleContext: BundleContext, bundleLocation: String): IO[PluginError, Unit] =
    Task {
      bundleContext.installBundle(bundleLocation).start()
    }.mapError(PluginError.Exception)

  def installSystemBundles(bundlesDirectory: File, bundleContext: BundleContext): IO[PluginError, Unit] =
    for {
      files     <- UIO(bundlesDirectory.listFiles.filter(_.isFile).filter(_.getName.endsWith("jar")))
      bundles   <- Task(files.map(b => bundleContext.installBundle(s"file:${b.getAbsolutePath}"))).mapError(PluginError.Exception)
      _         <- Task(bundles.foreach(_.start())).mapError(PluginError.Exception)
    } yield ()

  def uninstallPlugin(bundleContext: BundleContext, bundleLocation: String): IO[PluginError, Unit] =
    Task {
      bundleContext.getBundles
        .filter(_.getLocation == bundleLocation)
        .foreach { bundle =>
          bundle.uninstall()
        }
    }.mapError(PluginError.Exception)

  def removePlugin(pluginsDirectory: File, pluginName: String): IO[PluginError, Unit] =
    Task {
      pluginsDirectory.listFiles
        .filter(_.isFile)
        .filter(_.getName == pluginName)
        .foreach(file => if (file.exists()) file.delete())
    }.mapError(PluginError.Exception)

  def copyPlugin(pluginsDirectory: File, filePath: String): IO[PluginError, Path] =
    Task {
      Files.copy(Paths.get(filePath), Paths.get(pluginsDirectory + "/" + new File(filePath).getName))
    }.mapError(PluginError.Exception)
}