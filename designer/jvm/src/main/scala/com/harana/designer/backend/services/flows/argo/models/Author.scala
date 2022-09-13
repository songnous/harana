package com.harana.designer.backend.services.flows.argo.models

import io.circe.generic.JsonCodec

@JsonCodec
case class Author(name: String,
                  email: String)