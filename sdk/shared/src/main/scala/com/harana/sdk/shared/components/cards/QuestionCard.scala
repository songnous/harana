package com.harana.sdk.shared.components.cards

import com.harana.sdk.shared.models.common.{Component, Question}
import io.circe.generic.JsonCodec

@JsonCodec
case class QuestionCard(question: Question) extends Component
