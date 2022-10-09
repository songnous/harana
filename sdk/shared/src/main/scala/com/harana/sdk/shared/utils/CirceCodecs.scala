package com.harana.sdk.shared.utils

import cats.syntax.either._
import com.harana.sdk.shared.components.cards._
import com.harana.sdk.shared.components.cards.search.SearchResultCard
import com.harana.sdk.shared.components.lists._
import com.harana.sdk.shared.components.maps._
import com.harana.sdk.shared.components.panels._
import com.harana.sdk.shared.components.structure._
import com.harana.sdk.shared.components.widgets._
import com.harana.sdk.shared.models.catalog.{Page, Panel}
import com.harana.sdk.shared.models.common._

import java.net.URI
import com.harana.sdk.shared.plugin.Service
import io.circe._
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
import squants.market.{Money, defaultMoneyContext}
import com.harana.sdk.utils.ReflectUtils

object CirceCodecs {

	implicit val moneyContext = defaultMoneyContext

	implicit val decodeComponent: Decoder[Component] = Decoder.instance[Component] { c =>
		val content = c.downField("component").success.get
		c.downField("type").as[String].getOrElse(throw new Exception("Component type not found")) match {
			case "EventCard" => deriveDecoder[EventCard].apply(content)
			case "ImageCard" => deriveDecoder[ImageCard].apply(content)
			case "InvoiceCard" => deriveDecoder[InvoiceCard].apply(content)
			case "PageCard" => deriveDecoder[PageCard].apply(content)
			case "QuestionCard" => deriveDecoder[QuestionCard].apply(content)
			case "SearchResultCard" => deriveDecoder[SearchResultCard].apply(content)
			case "UserCard" => deriveDecoder[UserCard].apply(content)
			case "VideoCard" => deriveDecoder[VideoCard].apply(content)
			case "CommentsList" => deriveDecoder[CommentsList].apply(content)
			case "EventsList" => deriveDecoder[EventsList].apply(content)
			case "FilesList" => deriveDecoder[FilesList].apply(content)
			case "ImagesList" => deriveDecoder[ImagesList].apply(content)
			case "TagsList" => deriveDecoder[TagsList].apply(content)
			case "TasksList" => deriveDecoder[TasksList].apply(content)
			case "UsersList" => deriveDecoder[UsersList].apply(content)
			case "GoogleMap" => deriveDecoder[GoogleMap].apply(content)
			case "VectorMap" => deriveDecoder[VectorMap].apply(content)
			case "CalendarPanel" => deriveDecoder[CalendarPanel].apply(content)
			case "CommentsPanel" => deriveDecoder[CommentsPanel].apply(content)
			case "SearchResultsPanel" => deriveDecoder[SearchResultsPanel].apply(content)
			case "TimerPanel" => deriveDecoder[TimerPanel].apply(content)
			case "AccordionStructure" => deriveDecoder[AccordionStructure].apply(content)
			case "GridStructure" => deriveDecoder[GridStructure].apply(content)
			case "PillsStructure" => deriveDecoder[PillsStructure].apply(content)
			case "TabsStructure" => deriveDecoder[TabsStructure].apply(content)
			case "AreaChartWidget" => deriveDecoder[AreaChartWidget].apply(content)
			case "BarChartWidget" => deriveDecoder[BarChartWidget].apply(content)
			case "CounterStatsWidget" => deriveDecoder[CounterStatsWidget].apply(content)
			case "DonutStatsWidget" => deriveDecoder[DonutStatsWidget].apply(content)
			case "DonutWidget" => deriveDecoder[DonutWidget].apply(content)
			case "PieChartWidget" => deriveDecoder[PieChartWidget].apply(content)
			case "ProgressStatsWidget" => deriveDecoder[ProgressStatsWidget].apply(content)
			case "TimerWidget" => deriveDecoder[TimerWidget].apply(content)
		}
	}

	implicit val encodeComponent: Encoder[Component] = Encoder.instance[Component] { component =>
		val componentType = component.getClass.getSimpleName
		val json = componentType match {
			case "EventCard" => deriveEncoder[EventCard].apply(component.asInstanceOf[EventCard])
			case "ImageCard" => deriveEncoder[ImageCard].apply(component.asInstanceOf[ImageCard])
			case "InvoiceCard" => deriveEncoder[InvoiceCard].apply(component.asInstanceOf[InvoiceCard])
			case "PageCard" => deriveEncoder[PageCard].apply(component.asInstanceOf[PageCard])
			case "QuestionCard" => deriveEncoder[QuestionCard].apply(component.asInstanceOf[QuestionCard])
			case "SearchResultCard" => deriveEncoder[SearchResultCard].apply(component.asInstanceOf[SearchResultCard])
			case "UserCard" => deriveEncoder[UserCard].apply(component.asInstanceOf[UserCard])
			case "VideoCard" => deriveEncoder[VideoCard].apply(component.asInstanceOf[VideoCard])
			case "CommentsList" => deriveEncoder[CommentsList].apply(component.asInstanceOf[CommentsList])
			case "EventsList" => deriveEncoder[EventsList].apply(component.asInstanceOf[EventsList])
			case "FilesList" => deriveEncoder[FilesList].apply(component.asInstanceOf[FilesList])
			case "ImagesList" => deriveEncoder[ImagesList].apply(component.asInstanceOf[ImagesList])
			case "TagsList" => deriveEncoder[TagsList].apply(component.asInstanceOf[TagsList])
			case "TasksList" => deriveEncoder[TasksList].apply(component.asInstanceOf[TasksList])
			case "UsersList" => deriveEncoder[UsersList].apply(component.asInstanceOf[UsersList])
			case "GoogleMap" => deriveEncoder[GoogleMap].apply(component.asInstanceOf[GoogleMap])
			case "VectorMap" => deriveEncoder[VectorMap].apply(component.asInstanceOf[VectorMap])
			case "CalendarPanel" => deriveEncoder[CalendarPanel].apply(component.asInstanceOf[CalendarPanel])
			case "CommentsPanel" => deriveEncoder[CommentsPanel].apply(component.asInstanceOf[CommentsPanel])
			case "SearchResultsPanel" => deriveEncoder[SearchResultsPanel].apply(component.asInstanceOf[SearchResultsPanel])
			case "TimerPanel" => deriveEncoder[TimerPanel].apply(component.asInstanceOf[TimerPanel])
			case "AccordionStructure" => deriveEncoder[AccordionStructure].apply(component.asInstanceOf[AccordionStructure])
			case "GridStructure" => deriveEncoder[GridStructure].apply(component.asInstanceOf[GridStructure])
			case "PillsStructure" => deriveEncoder[PillsStructure].apply(component.asInstanceOf[PillsStructure])
			case "TabsStructure" => deriveEncoder[TabsStructure].apply(component.asInstanceOf[TabsStructure])
			case "AreaChartWidget" => deriveEncoder[AreaChartWidget].apply(component.asInstanceOf[AreaChartWidget])
			case "BarChartWidget" => deriveEncoder[BarChartWidget].apply(component.asInstanceOf[BarChartWidget])
			case "CounterStatsWidget" => deriveEncoder[CounterStatsWidget].apply(component.asInstanceOf[CounterStatsWidget])
			case "DonutStatsWidget" => deriveEncoder[DonutStatsWidget].apply(component.asInstanceOf[DonutStatsWidget])
			case "DonutWidget" => deriveEncoder[DonutWidget].apply(component.asInstanceOf[DonutWidget])
			case "PieChartWidget" => deriveEncoder[PieChartWidget].apply(component.asInstanceOf[PieChartWidget])
			case "ProgressStatsWidget" => deriveEncoder[ProgressStatsWidget].apply(component.asInstanceOf[ProgressStatsWidget])
			case "TimerWidget" => deriveEncoder[TimerWidget].apply(component.asInstanceOf[TimerWidget])
		}

		io.circe.Json.obj("type" -> componentType.asJson, "component" -> json)
	}

	implicit val decodeEntity: Decoder[Entity] = Decoder.instance[Entity] { c =>
		val content = c.downField("entity").success.get
		c.downField("type").as[String].getOrElse(throw new Exception("Entity type not found")) match {
			case "Comment" => deriveDecoder[com.harana.sdk.shared.models.common.Comment].apply(content)
			case "Event" => deriveDecoder[com.harana.sdk.shared.models.common.Event].apply(content)
			case "File" => deriveDecoder[com.harana.sdk.shared.models.common.File].apply(content)
			case "Image" => deriveDecoder[com.harana.sdk.shared.models.common.Image].apply(content)
			case "Invoice" => deriveDecoder[com.harana.sdk.shared.models.common.Invoice].apply(content)
			case "Group" => deriveDecoder[com.harana.sdk.shared.models.common.Group].apply(content)
			case "Page" => deriveDecoder[com.harana.sdk.shared.models.catalog.Page].apply(content)
			case "Panel" => deriveDecoder[com.harana.sdk.shared.models.catalog.Panel].apply(content)
			case "Question" => deriveDecoder[com.harana.sdk.shared.models.common.Question].apply(content)
			case "Task" => deriveDecoder[com.harana.sdk.shared.models.common.Task].apply(content)
			case "User" => deriveDecoder[com.harana.sdk.shared.models.common.User].apply(content)
			case "Video" => deriveDecoder[com.harana.sdk.shared.models.common.Video].apply(content)
		}
	}

	implicit val encodeEntity: Encoder[Entity] = Encoder.instance[Entity] { entity =>
		val entityType = entity.getClass.getSimpleName
		val json = entityType match {
			case "User" => deriveEncoder[com.harana.sdk.shared.models.common.User].apply(entity.asInstanceOf[com.harana.sdk.shared.models.common.User])
			case "Comment" => deriveEncoder[com.harana.sdk.shared.models.common.Comment].apply(entity.asInstanceOf[com.harana.sdk.shared.models.common.Comment])
			case "Event" => deriveEncoder[com.harana.sdk.shared.models.common.Event].apply(entity.asInstanceOf[com.harana.sdk.shared.models.common.Event])
			case "File" => deriveEncoder[com.harana.sdk.shared.models.common.File].apply(entity.asInstanceOf[com.harana.sdk.shared.models.common.File])
			case "Image" => deriveEncoder[com.harana.sdk.shared.models.common.Image].apply(entity.asInstanceOf[com.harana.sdk.shared.models.common.Image])
			case "Invoice" => deriveEncoder[com.harana.sdk.shared.models.common.Invoice].apply(entity.asInstanceOf[com.harana.sdk.shared.models.common.Invoice])
			case "Group" => deriveEncoder[com.harana.sdk.shared.models.common.Group].apply(entity.asInstanceOf[com.harana.sdk.shared.models.common.Group])
			case "Page" => deriveEncoder[Page].apply(entity.asInstanceOf[com.harana.sdk.shared.models.catalog.Page])
			case "Panel" => deriveEncoder[Panel].apply(entity.asInstanceOf[com.harana.sdk.shared.models.catalog.Panel])
			case "Question" => deriveEncoder[com.harana.sdk.shared.models.common.Question].apply(entity.asInstanceOf[com.harana.sdk.shared.models.common.Question])
			case "Task" => deriveEncoder[com.harana.sdk.shared.models.common.Task].apply(entity.asInstanceOf[com.harana.sdk.shared.models.common.Task])
			case "Video" => deriveEncoder[com.harana.sdk.shared.models.common.Video].apply(entity.asInstanceOf[com.harana.sdk.shared.models.common.Video])
		}
		io.circe.Json.obj("type" -> entityType.asJson, "entity" -> json)
	}

	implicit val decodeBackground: Decoder[Background] = Decoder.instance[Background] { c =>
		val value = c.downField("value").success.get
		c.downField("type").as[String].getOrElse(throw new Exception("Background type not found")) match {
			case "Hex" => deriveDecoder[Background.Hex].apply(value)
			case "HSL" => deriveDecoder[Background.HSL].apply(value)
			case "Image" => deriveDecoder[Background.Image].apply(value)
			case "RGB" => deriveDecoder[Background.RGB].apply(value)
		}
	}

	implicit val encodeBackground: Encoder[Background] = Encoder.instance[Background] { background =>
		val name = background.getClass.getSimpleName
		val json = name match {
			case "Hex" => deriveEncoder[Background.Hex].apply(background.asInstanceOf[Background.Hex])
			case "HSL" => deriveEncoder[Background.HSL].apply(background.asInstanceOf[Background.HSL])
			case "Image" => deriveEncoder[Background.Image].apply(background.asInstanceOf[Background.Image])
			case "RGB" => deriveEncoder[Background.RGB].apply(background.asInstanceOf[Background.RGB])
		}
		io.circe.Json.obj("type" -> name.asJson, "value" -> json)
	}

	implicit val decodeParameterValue: Decoder[ParameterValue] = Decoder.instance[ParameterValue] { c =>
		val value = c.downField("value").success.get
		c.downField("type").as[String].getOrElse(throw new Exception("Parameter value type not found")) match {
			case "Boolean" => deriveDecoder[ParameterValue.Boolean].apply(value)
			case "Decimal" => deriveDecoder[ParameterValue.Decimal].apply(value)
			case "DecimalRange" => deriveDecoder[ParameterValue.DecimalRange].apply(value)
			case "GeoCoordinate" => deriveDecoder[ParameterValue.GeoCoordinate].apply(value)
			case "Instant" => deriveDecoder[ParameterValue.Instant].apply(value)
			case "InstantRange" => deriveDecoder[ParameterValue.InstantRange].apply(value)
			case "Integer" => deriveDecoder[ParameterValue.Integer].apply(value)
			case "IntegerList" => deriveDecoder[ParameterValue.IntegerList].apply(value)
			case "IntegerRange" => deriveDecoder[ParameterValue.IntegerRange].apply(value)
			case "IPAddress" => deriveDecoder[ParameterValue.IPAddress].apply(value)
			case "IPAddressList" => deriveDecoder[ParameterValue.IPAddressList].apply(value)
			case "Money" => deriveDecoder[ParameterValue.Money].apply(value)
			case "Object" => deriveDecoder[ParameterValue.Object].apply(value)
			case "PageId" => deriveDecoder[ParameterValue.PageId].apply(value)
			case "String" => deriveDecoder[ParameterValue.String].apply(value)
			case "StringList" => deriveDecoder[ParameterValue.StringList].apply(value)
			case "URI" => deriveDecoder[ParameterValue.URI].apply(value)
			case "UserId" => deriveDecoder[ParameterValue.UserId].apply(value)
			case "VideoId" => deriveDecoder[ParameterValue.VideoId].apply(value)
		}
	}

	implicit val encodeParameterValue: Encoder[ParameterValue] = Encoder.instance[ParameterValue] { parameter =>
		val parameterType = parameter.getClass.getSimpleName
		val json = parameterType match {
			case "Boolean" => deriveEncoder[ParameterValue.Boolean].apply(parameter.asInstanceOf[ParameterValue.Boolean])
			case "Decimal" => deriveEncoder[ParameterValue.Decimal].apply(parameter.asInstanceOf[ParameterValue.Decimal])
			case "DecimalRange" => deriveEncoder[ParameterValue.DecimalRange].apply(parameter.asInstanceOf[ParameterValue.DecimalRange])
			case "GeoCoordinate" => deriveEncoder[ParameterValue.GeoCoordinate].apply(parameter.asInstanceOf[ParameterValue.GeoCoordinate])
			case "Instant" => deriveEncoder[ParameterValue.Instant].apply(parameter.asInstanceOf[ParameterValue.Instant])
			case "InstantRange" => deriveEncoder[ParameterValue.InstantRange].apply(parameter.asInstanceOf[ParameterValue.InstantRange])
			case "Integer" => deriveEncoder[ParameterValue.Integer].apply(parameter.asInstanceOf[ParameterValue.Integer])
			case "IntegerList" => deriveEncoder[ParameterValue.IntegerList].apply(parameter.asInstanceOf[ParameterValue.IntegerList])
			case "IntegerRange" => deriveEncoder[ParameterValue.IntegerRange].apply(parameter.asInstanceOf[ParameterValue.IntegerRange])
			case "IPAddress" => deriveEncoder[ParameterValue.IPAddress].apply(parameter.asInstanceOf[ParameterValue.IPAddress])
			case "IPAddressList" => deriveEncoder[ParameterValue.IPAddressList].apply(parameter.asInstanceOf[ParameterValue.IPAddressList])
			case "Money" => deriveEncoder[ParameterValue.Money].apply(parameter.asInstanceOf[ParameterValue.Money])
			case "Object" => deriveEncoder[ParameterValue.Object].apply(parameter.asInstanceOf[ParameterValue.Object])
			case "PageId" => deriveEncoder[ParameterValue.PageId].apply(parameter.asInstanceOf[ParameterValue.PageId])
			case "String" => deriveEncoder[ParameterValue.String].apply(parameter.asInstanceOf[ParameterValue.String])
			case "StringList" => deriveEncoder[ParameterValue.StringList].apply(parameter.asInstanceOf[ParameterValue.StringList])
			case "URI" => deriveEncoder[ParameterValue.URI].apply(parameter.asInstanceOf[ParameterValue.URI])
			case "UserId" => deriveEncoder[ParameterValue.UserId].apply(parameter.asInstanceOf[ParameterValue.UserId])
			case "VideoId" => deriveEncoder[ParameterValue.VideoId].apply(parameter.asInstanceOf[ParameterValue.VideoId])
		}
		io.circe.Json.obj("type" -> parameterType.asJson, "value" -> json)
	}


	implicit val decodeParameterType: Decoder[Parameter] = Decoder.instance[Parameter] { c =>
		val content = c.downField("parameter").success.get
		c.downField("type").as[String].getOrElse(throw new Exception("Parameter type not found")) match {
			case "Boolean" => deriveDecoder[Parameter.Boolean].apply(content)
			case "Code" => deriveDecoder[Parameter.Code].apply(content)
			case "Color" => deriveDecoder[Parameter.Color].apply(content)
			case "Country" => deriveDecoder[Parameter.Country].apply(content)
			case "DataTable" => deriveDecoder[Parameter.DataTable].apply(content)
			case "Date" => deriveDecoder[Parameter.Date].apply(content)
			case "Decimal" => deriveDecoder[Parameter.Decimal].apply(content)
			case "DecimalRange" => deriveDecoder[Parameter.DecimalRange].apply(content)
			case "Email" => deriveDecoder[Parameter.Email].apply(content)
			case "Emoji" => deriveDecoder[Parameter.Emoji].apply(content)
			case "File" => deriveDecoder[Parameter.File].apply(content)
			case "GeoCoordinate" => deriveDecoder[Parameter.GeoCoordinate].apply(content)
			case "Html" => deriveDecoder[Parameter.Html].apply(content)
			case "Image" => deriveDecoder[Parameter.Image].apply(content)
			case "Integer" => deriveDecoder[Parameter.Integer].apply(content)
			case "IntegerRange" => deriveDecoder[Parameter.IntegerRange].apply(content)
			case "IPAddress" => deriveDecoder[Parameter.IPAddress].apply(content)
			case "IPAddressList" => deriveDecoder[Parameter.IPAddressList].apply(content)
			case "Json" => deriveDecoder[Parameter.Json].apply(content)
			case "Markdown" => deriveDecoder[Parameter.Markdown].apply(content)
			case "Money" => deriveDecoder[Parameter.Money].apply(content)
			case "Object" => deriveDecoder[Parameter.Object].apply(content)
			case "Page" => deriveDecoder[Parameter.Page].apply(content)
			case "Password" => deriveDecoder[Parameter.Password].apply(content)
			case "SearchQuery" => deriveDecoder[Parameter.SearchQuery].apply(content)
			case "String" => deriveDecoder[Parameter.String].apply(content)
			case "StringList" => deriveDecoder[Parameter.StringList].apply(content)
			case "Tags" => deriveDecoder[Parameter.Tags].apply(content)
			case "Time" => deriveDecoder[Parameter.Time].apply(content)
			case "TimeZone" => deriveDecoder[Parameter.TimeZone].apply(content)
			case "Uri" => deriveDecoder[Parameter.Uri].apply(content)
			case "User" => deriveDecoder[Parameter.User].apply(content)
			case "Video" => deriveDecoder[Parameter.Video].apply(content)
		}
	}

	implicit val encodeParameterType: Encoder[Parameter] = Encoder.instance[Parameter] { parameter =>
		val entityType = parameter.getClass.getSimpleName
		val json = entityType match {
			case "Boolean" => deriveEncoder[Parameter.Boolean].apply(parameter.asInstanceOf[Parameter.Boolean])
			case "Code" => deriveEncoder[Parameter.Code].apply(parameter.asInstanceOf[Parameter.Code])
			case "Color" => deriveEncoder[Parameter.Color].apply(parameter.asInstanceOf[Parameter.Color])
			case "Country" => deriveEncoder[Parameter.Country].apply(parameter.asInstanceOf[Parameter.Country])
			case "DataTable" => deriveEncoder[Parameter.DataTable].apply(parameter.asInstanceOf[Parameter.DataTable])
			case "Date" => deriveEncoder[Parameter.Date].apply(parameter.asInstanceOf[Parameter.Date])
			case "Decimal" => deriveEncoder[Parameter.Decimal].apply(parameter.asInstanceOf[Parameter.Decimal])
			case "DecimalRange" => deriveEncoder[Parameter.DecimalRange].apply(parameter.asInstanceOf[Parameter.DecimalRange])
			case "Email" => deriveEncoder[Parameter.Email].apply(parameter.asInstanceOf[Parameter.Email])
			case "Emoji" => deriveEncoder[Parameter.Emoji].apply(parameter.asInstanceOf[Parameter.Emoji])
			case "File" => deriveEncoder[Parameter.File].apply(parameter.asInstanceOf[Parameter.File])
			case "GeoCoordinate" => deriveEncoder[Parameter.GeoCoordinate].apply(parameter.asInstanceOf[Parameter.GeoCoordinate])
			case "Html" => deriveEncoder[Parameter.Html].apply(parameter.asInstanceOf[Parameter.Html])
			case "Image" => deriveEncoder[Parameter.Image].apply(parameter.asInstanceOf[Parameter.Image])
			case "Integer" => deriveEncoder[Parameter.Integer].apply(parameter.asInstanceOf[Parameter.Integer])
			case "IntegerRange" => deriveEncoder[Parameter.IntegerRange].apply(parameter.asInstanceOf[Parameter.IntegerRange])
			case "IPAddress" => deriveEncoder[Parameter.IPAddress].apply(parameter.asInstanceOf[Parameter.IPAddress])
			case "IPAddressList" => deriveEncoder[Parameter.IPAddressList].apply(parameter.asInstanceOf[Parameter.IPAddressList])
			case "Json" => deriveEncoder[Parameter.Json].apply(parameter.asInstanceOf[Parameter.Json])
			case "Markdown" => deriveEncoder[Parameter.Markdown].apply(parameter.asInstanceOf[Parameter.Markdown])
			case "Money" => deriveEncoder[Parameter.Money].apply(parameter.asInstanceOf[Parameter.Money])
			case "Object" => deriveEncoder[Parameter.Object].apply(parameter.asInstanceOf[Parameter.Object])
			case "Page" => deriveEncoder[Parameter.Page].apply(parameter.asInstanceOf[Parameter.Page])
			case "Password" => deriveEncoder[Parameter.Password].apply(parameter.asInstanceOf[Parameter.Password])
			case "SearchQuery" => deriveEncoder[Parameter.SearchQuery].apply(parameter.asInstanceOf[Parameter.SearchQuery])
			case "String" => deriveEncoder[Parameter.String].apply(parameter.asInstanceOf[Parameter.String])
			case "StringList" => deriveEncoder[Parameter.StringList].apply(parameter.asInstanceOf[Parameter.StringList])
			case "Tags" => deriveEncoder[Parameter.Tags].apply(parameter.asInstanceOf[Parameter.Tags])
			case "Time" => deriveEncoder[Parameter.Time].apply(parameter.asInstanceOf[Parameter.Time])
			case "TimeZone" => deriveEncoder[Parameter.TimeZone].apply(parameter.asInstanceOf[Parameter.TimeZone])
			case "Uri" => deriveEncoder[Parameter.Uri].apply(parameter.asInstanceOf[Parameter.Uri])
			case "User" => deriveEncoder[Parameter.User].apply(parameter.asInstanceOf[Parameter.User])
			case "Video" => deriveEncoder[Parameter.Video].apply(parameter.asInstanceOf[Parameter.Video])

		}
		io.circe.Json.obj("type" -> entityType.asJson, "parameter" -> json)
	}

	implicit def encodeEither[A, B](implicit encoderA: Encoder[A], encoderB: Encoder[B]): Encoder[Either[A, B]] = {
		o: Either[A, B] => o.fold(_.asJson, _.asJson)
	}

	implicit def decodeEither[A, B](implicit decoderA: Decoder[A], decoderB: Decoder[B]): Decoder[Either[A, B]] = { c =>
		c.as[A] match {
			case Right(a) => Right(Left(a))
			case _ => c.as[B].map(Right(_))
		}
	}

	implicit val encodeStringMap: Encoder[Map[String, Any]] = Encoder.instance[Map[String, Any]] { map =>
		Json.obj(
			map.map {
				case (key, value: String)  => key -> Json.fromString(value)
				case (key, value: Number)  => key -> Json.fromBigDecimal(value.doubleValue())
				case (key, value: Boolean) => key -> Json.fromBoolean(value)
				case (key, value: List[_]) => key -> Json.arr(value.map(_.toString).map(Json.fromString): _*)
				case (_, value) => throw new NotImplementedError(s"Add support for values of type '${value.getClass}' in the jsons generator")
			}.toSeq: _*
		)
	}

	implicit val optionStringKeyEncoder = new KeyEncoder[Option[String]] { override def apply(key: Option[String]) = key.getOrElse("") }

	implicit def decodeSubEntity[A <: Entity]: Decoder[Entity] = decodeEntity
	implicit val decodeMoney: Decoder[Money] = Decoder.decodeString.emap { str => Money(str).toEither.leftMap(_ => "Malformed Money") }
	implicit def decodeService[A <: Service]: Decoder[A] = Decoder.decodeString.emap { str => Either.catchNonFatal(ReflectUtils.classForName[A](str)).leftMap(_ => "Invalid Service") }
	implicit val decodeUri: Decoder[URI] = Decoder.decodeString.emap { str => Either.catchNonFatal(URI.create(str)).leftMap(_ => "Malformed URL") }

	implicit def encodeSubEntity[A <: Entity]: Encoder[Entity] = encodeEntity
	implicit val encodeMoney: Encoder[Money] = Encoder.encodeString.contramap[Money](_.toString)
	implicit def encodeService[A <: Service]: Encoder[A] = Encoder.encodeString.contramap[A](_.getClass.getName)
	implicit val encodeUri: Encoder[URI] = Encoder.encodeString.contramap[URI](_.toString)
}
