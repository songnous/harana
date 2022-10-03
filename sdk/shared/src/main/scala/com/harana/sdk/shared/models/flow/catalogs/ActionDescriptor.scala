package com.harana.sdk.shared.models.flow.catalogs

import com.harana.sdk.shared.models.flow.{ActionTypeInfo, PortPosition}
import com.harana.sdk.shared.models.flow.utils.{Id, SortPriority}
import io.circe.Json
import io.circe.generic.JsonCodec

@JsonCodec
case class ActionDescriptor(id: Id,
                            name: String,
                            description: String,
                            category: ActionCategory,
                            priority: SortPriority,
                            hasDocumentation: Boolean,
                            parametersJsonDescription: Json,
                            inPorts: Seq[String],
                            inputPortsLayout: List[PortPosition],
                            outPorts: Seq[String],
                            outputPortsLayout: List[PortPosition])
