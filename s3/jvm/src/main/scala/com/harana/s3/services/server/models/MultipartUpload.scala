package com.harana.s3.services.s3_server.models

case class MultipartUpload(bucket: String, key: String, id: String)