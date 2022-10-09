package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.ProjectorInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import izumi.reflect.Tag

trait ProjectionInfo extends TransformerAsActionInfo[ProjectorInfo] with ActionDocumentation {

  val id: Id = "9c3225d8-d430-48c0-a46e-fa83909ad054"
  val name = "projection"
  val since = Version(1, 2, 0)
  val category = Filtering

  lazy val portO_1: Tag[ProjectorInfo] = typeTag

}

object ProjectionInfo extends ProjectionInfo