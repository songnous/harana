package com.harana.sdk.shared.models.catalog

import java.time.Instant

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.catalog.Page.PageId
import com.harana.sdk.shared.models.catalog.Panel.{PanelId, PanelSlotId}
import com.harana.sdk.shared.models.catalog.UserPageType.UserPageTypeId
import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.common.User.UserId
import com.harana.sdk.shared.models.common.{Entity, ParameterValue, Status, User, Visibility}
import com.harana.sdk.shared.plugin.PageType.PageTypeId
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._
import com.harana.sdk.shared.utils.Random

@JsonCodec
case class Page(name: String,
                title: String,
                description: String,
                category: String,
                pageTypeId: Either[PageTypeId, UserPageTypeId],
                panels: Map[PanelSlotId, PanelId],
                parameterValues: Map[ParameterName, ParameterValue],
                linkId: String,
								space: String,
                parentPage: Option[PageId],
                subPages: List[PageId],
                linkedPages: Map[String, List[PageId]],
                createdBy: Option[UserId],
                created: Instant,
                updatedBy: Option[UserId],
                updated: Instant,
								id: PageId,
                status: Status,
								visibility: Visibility,
								version: Long,
							  tags: Set[String],
                relationships: Map[String, EntityId])
    extends Entity with Serializable {

	type EntityType = Page
}

object Page {
	type PageId = String

	def apply(name: String, title: String, description: String, category: String, pageTypeId: Either[PageTypeId, UserPageTypeId], panels: Map[PanelSlotId, Panel], parameterValues: Map[ParameterName, ParameterValue], linkId: String, space: String, createdBy: Option[User], visibility: Visibility, tags: Set[String]): Page = {
		apply(name, title, description, category, pageTypeId, uuidPanels(panels), parameterValues, linkId, space, None, List(), Map(), createdBy.map(_.id), Instant.now, createdBy.map(_.id), Instant.now, Random.long, Status.Active, visibility, 1L, tags, Map())
	}

	private def uuidPanels(panels: Map[PanelSlotId, Panel]) = panels.map( p => p._1 -> p._2.id)
}