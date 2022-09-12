package com.harana.sdk.backend.models.flow.utils

import com.typesafe.config.Config

import java.io.File
import scala.jdk.CollectionConverters._

object ConfigWithDirListsImplicits {

  implicit class ConfigWithDirLists(val config: Config) {

    def getDirList(path: String) =
      config
        .getStringList(path)
        .asScala
        .toList
        .map(new File(_))
        .filter(_.isDirectory)
  }
}
