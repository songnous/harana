package com.harana.sdk.backend.models.flow.utils

import org.slf4j.{Logger, LoggerFactory}

trait Logging {

  @transient
  lazy val logger: Logger = LoggerFactory.getLogger(getClass.getName)

}
