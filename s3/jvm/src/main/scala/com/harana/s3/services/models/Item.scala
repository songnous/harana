package com.harana.s3.services.models

import software.amazon.awssdk.services.s3.model.StorageClass

import java.net.URI

case class Item(name: String,
                storageType: StorageType,
                storageClass: StorageClass,
                uri: Option[URI],
                userMetadata: Map[String, String],
                eTag: Option[String],
                creationDate: Option[Long],
                lastModifiedDate: Option[Long],
                size: Option[Long])
