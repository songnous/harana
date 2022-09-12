package com.harana.modules

import java.io.File

import com.harana.modules.kind.models.Cluster
import zio.UIO

package object kind {

  @inline
  def generateConfig(cluster: Cluster): File = null
}