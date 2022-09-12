package com.harana.sdk.shared.models.catalog

import com.harana.sdk.shared.models.catalog.UserPanelType.UserPanelTypeId
import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common._
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._

import java.time.Instant

@JsonCodec
case class UserPanelType(name: String,
                         title: String,
                         description: String,
                         layout: List[Component],
                         override val instanceParameters: List[Parameter],
                         override val globalParameters: List[Parameter],
                         override val scripts: List[String],
                         override val stylesheets: List[String],
                         override val userEditable: Boolean,
                         override val allowUserRefresh: Boolean,
                         createdBy: Option[UserId],
                         created: Instant,
                         updatedBy: Option[UserId],
                         updated: Instant,
                         id: UserPanelTypeId,
                         status: Status,
                         version: Long,
                         tags: Set[String],
                         relationships: Map[String, EntityId])
		extends Entity with Serializable with com.harana.sdk.shared.plugin.PanelType {

  override def layout(parameterValues: Map[ParameterName, ParameterValue]) = layout
  override def layoutHasChanged: Boolean = false

  override def onStartup(): Unit = {}
  override def onShutdown(): Unit = {}

	override def onAppContextChange(newContext: Map[String, _]): Unit = {}
  override def onUserConfigure(newparameterValues: Map[ParameterName, ParameterValue]): Unit = {}

	type EntityType = UserPanelType
}

object UserPanelType {
	type UserPanelTypeId = String
}