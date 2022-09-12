package com.harana.modules

import java.io.{InputStream, OutputStream}

import alluxio.AlluxioURI
import alluxio.client.file.FileSystem
import alluxio.conf.{AlluxioConfiguration, InstancedConfiguration, PropertyKey}
import alluxio.exception.AlluxioException
import zio.{IO, UIO}

package object alluxiofs {

  def alluxioFs(defaultConfig: AlluxioConfiguration, username: Option[String] = None) =
    UIO {
      FileSystem.Factory.create(
        username match {
          case Some(u) =>
            val config = defaultConfig.copyProperties()
            config.set(PropertyKey.SECURITY_LOGIN_USERNAME, u)
            new InstancedConfiguration(config)

          case None => defaultConfig
        }
      )
    }

  def closeStream(is: InputStream) =
    UIO(is.close())

  def closeStream(os: OutputStream) =
    UIO(os.close())

  def io[A](fn: => A): IO[AlluxioException, A] =
    IO(fn).refineToOrDie[AlluxioException]

  def uri(path: String) =
    new AlluxioURI(path)

}
