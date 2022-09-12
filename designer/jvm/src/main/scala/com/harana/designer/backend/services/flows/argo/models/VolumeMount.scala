package com.harana.designer.backend.services.flows.argo.models

import io.circe.generic.JsonCodec

@JsonCodec
case class VolumeMount(mountPath: Option[String] = None,
                       mountPropagation: Option[String] = None,
                       name: String,
                       readOnly: Option[Boolean] = None,
                       subPath: Option[String] = None,
                       subPathExpr: Option[String] = None)