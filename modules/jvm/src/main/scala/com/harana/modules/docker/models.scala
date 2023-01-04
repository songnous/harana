package com.harana.modules.docker

import io.circe.generic.JsonCodec

object models {

  @JsonCodec
  case class HubTag(creator: Long,
                    id: Long,
                    images: List[HubImage] = List(),
                    last_updated: String,
                    last_updater: Long,
                    last_updater_username: String,
                    name: String,
                    repository: Long,
                    full_size: Long,
                    v2: Boolean,
                    tag_status: String,
                    tag_last_pulled: String,
                    tag_last_pushed: String,
                    media_type: String,
                    digest: String)

  @JsonCodec
  case class HubImage(architecture: String,
                      features: String,
                      variant: Option[String],
                      digest: String,
                      os: String,
                      os_features: String,
                      os_version: Option[String],
                      size: Long,
                      status: String,
                      last_pulled: String,
                      last_pushed: String)

}
