package com.harana.designer.backend.services.flows.argo.models

import com.harana.designer.backend.modules.projects.models.Git
import io.circe.generic.JsonCodec

@JsonCodec
case class Repository(name: String,
                      git: Git)