package com.harana.id.jwt  

import com.harana.id.jwt.modules.jwt.LiveJWT
import com.harana.modules.core.{Layers => CoreLayers}

object Layers {
  val jwt = (CoreLayers.standard ++ CoreLayers.cache) >>> LiveJWT.layer
}