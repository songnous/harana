package com.harana.sdk.shared.models.common

import com.harana.sdk.shared.models.data.DataSource
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._
//import scala.scalajs.reflect.annotation.EnableReflectiveInstantiation

sealed trait ParameterValue extends Any

object ParameterValue {

	@JsonCodec
	case class Boolean(value: scala.Boolean) extends AnyVal with ParameterValue
	implicit def boolean(x: Boolean) = x.value
	implicit def optBoolean(x: Option[Boolean]) = x.map(_.value)

	@JsonCodec
	case class DataSourceId(value: DataSource.DataSourceId) extends AnyVal with ParameterValue
	implicit def dataSourceId(x: DataSourceId) = x.value
	implicit def optDataSourceId(x: Option[DataSourceId]) = x.map(_.value)

	@JsonCodec
	case class Decimal(value: BigDecimal) extends AnyVal with ParameterValue
	implicit def decimal(x: Decimal) = x.value
	implicit def optDecimal(x: Option[Decimal]) = x.map(_.value)

	@JsonCodec
	case class DecimalRange(value: (BigDecimal, BigDecimal)) extends AnyVal with ParameterValue
	implicit def decimalRange(x: DecimalRange) = x.value
	implicit def optDecimalRange(x: Option[DecimalRange]) = x.map(_.value)

	@JsonCodec
	case class GeoCoordinate(value: (java.lang.Double, java.lang.Double)) extends AnyVal with ParameterValue
	implicit def geoCoordinate(x: GeoCoordinate) = x.value
	implicit def optGeoCoordinate(x: Option[GeoCoordinate]) = x.map(_.value)

	@JsonCodec
	case class Instant(value: java.time.Instant) extends AnyVal with ParameterValue
	implicit def instant(x: Instant) = x.value
	implicit def optInstant(x: Option[Instant]) = x.map(_.value)

	@JsonCodec
	case class InstantRange(value: (java.time.Instant, java.time.Instant)) extends AnyVal with ParameterValue
	implicit def instantRange(x: InstantRange) = x.value
	implicit def optInstantRange(x: Option[InstantRange]) = x.map(_.value)

	@JsonCodec
	case class Integer(value: Int) extends AnyVal with ParameterValue
	implicit def integer(x: Integer) = x.value
	implicit def optInteger(x: Option[Integer]) = x.map(_.value)

	@JsonCodec
	case class IntegerList(value: List[Int]) extends AnyVal with ParameterValue
	implicit def integerList(x: IntegerList) = x.value
	implicit def optIntegerList(x: Option[IntegerList]) = x.map(_.value)

	@JsonCodec
	case class IntegerRange(value: (Int, Int)) extends AnyVal with ParameterValue
	implicit def integerRange(x: IntegerRange) = x.value
	implicit def optIntegerRange(x: Option[IntegerRange]) = x.map(_.value)

	@JsonCodec
	case class IPAddress(value: (java.lang.String, scala.Long)) extends AnyVal with ParameterValue
	implicit def ipAddress(x: IPAddress) = x.value
	implicit def optIPAddress(x: Option[IPAddress]) = x.map(_.value)

	@JsonCodec
	case class IPAddressList(value: List[(java.lang.String, scala.Long)]) extends AnyVal with ParameterValue
	implicit def ipAddressList(x: IPAddressList) = x.value
	implicit def optIPAddressList(x: Option[IPAddressList]) = x.map(_.value)

	@JsonCodec
	case class Long(value: scala.Long) extends AnyVal with ParameterValue
	implicit def long(x: Long) = x.value
	implicit def optLong(x: Option[Long]) = x.map(_.value)

	@JsonCodec
	case class LongList(value: List[scala.Long]) extends AnyVal with ParameterValue
	implicit def longList(x: LongList) = x.value
	implicit def optLongList(x: Option[LongList]) = x.map(_.value)

	@JsonCodec
	case class LongRange(value: (scala.Long, scala.Long)) extends AnyVal with ParameterValue
	implicit def longRange(x: LongRange) = x.value
	implicit def optLongRange(x: Option[LongRange]) = x.map(_.value)

	@JsonCodec
	case class Money(value: BigDecimal) extends AnyVal with ParameterValue
	implicit def money(x: Money) = x.value
	implicit def optMoney(x: Option[Money]) = x.map(_.value)

	@JsonCodec
	case class Object(value: List[ParameterValue]) extends AnyVal with ParameterValue
	implicit def obj(o: Object) = o.value
	implicit def optObj(x: Option[Object]) = x.map(_.value)

	@JsonCodec
	case class PageId(value: com.harana.sdk.shared.models.catalog.Page.PageId) extends AnyVal with ParameterValue
	implicit def pageId(x: PageId) = x.value
	implicit def optPageId(x: Option[PageId]) = x.map(_.value)

	@JsonCodec
	case class String(value: java.lang.String) extends AnyVal with ParameterValue
	implicit def string(x: String) = x.value
	implicit def optString(x: Option[String]) = x.map(_.value)

	@JsonCodec
	case class StringList(value: List[java.lang.String]) extends AnyVal with ParameterValue
	implicit def stringList(x: StringList) = x.value
	implicit def optStringList(x: Option[StringList]) = x.map(_.value)

	@JsonCodec
	case class StringMap(value: Map[java.lang.String, java.lang.String]) extends AnyVal with ParameterValue
	implicit def stringMap(x: StringMap) = x.value
	implicit def optStringMap(x: Option[StringMap]) = x.map(_.value)

	@JsonCodec
	case class URI(value: java.net.URI) extends AnyVal with ParameterValue
	implicit def uri(x: URI) = x.value
	implicit def optURI(x: Option[URI]) = x.map(_.value)

	@JsonCodec
	case class UserId(value: com.harana.sdk.shared.models.common.User.UserId) extends AnyVal with ParameterValue
	implicit def userId(x: UserId) = x.value
	implicit def optUserId(x: Option[UserId]) = x.map(_.value)

	@JsonCodec
	case class VideoId(value: com.harana.sdk.shared.models.common.Video.VideoId) extends AnyVal with ParameterValue
	implicit def videoId(x: VideoId) = x.value
	implicit def optVideoId(x: Option[VideoId]) = x.map(_.value)
}