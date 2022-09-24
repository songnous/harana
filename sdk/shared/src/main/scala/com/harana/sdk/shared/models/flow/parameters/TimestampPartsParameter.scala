package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.actionobjects.DatetimeComposerInfo.TimestampPartColumnChoice
import com.harana.sdk.shared.models.flow.parameters.choice.AbstractChoiceParameter
import io.circe.generic.JsonCodec

@JsonCodec
case class TimestampPartsParameter(name: String,
                                   required: Boolean = false,
                                   default: Option[Set[TimestampPartColumnChoice]] = None)
  extends AbstractChoiceParameter[TimestampPartColumnChoice, Set[TimestampPartColumnChoice]] {

  val parameterType = ParameterType.TimestampParts

  override def validate(value: Set[TimestampPartColumnChoice]) = value.toList.flatMap(_.validateParameters)

  def replicate(name: String) = copy(name = name)

}