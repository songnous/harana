package com.harana.s3.services.server.models

import enumeratum.values.{IntEnum, IntEnumEntry}

sealed abstract class S3ErrorCode(val value: Int,
                                  val statusCode: Int,
                                  val description: String) extends IntEnumEntry

object S3ErrorCode extends IntEnum[S3ErrorCode] {

  case object ACCESS_DENIED extends S3ErrorCode(1, 403, "Forbidden")
  case object BAD_DIGEST extends S3ErrorCode(2, 400, "Bad Request")
  case object BUCKET_ALREADY_EXISTS extends S3ErrorCode(3, 403, "The requested bucket name is not available. The bucket namespace is shared by all users of the system. Please select a different name and try again.")
  case object BUCKET_ALREADY_OWNED_BY_YOU extends S3ErrorCode(4, 409, "Your previous request to create the named bucket succeeded and you already own it.")
  case object BUCKET_NOT_EMPTY extends S3ErrorCode(5, 409, "The bucket you tried to delete is not empty")
  case object ENTITY_TOO_LARGE extends S3ErrorCode(6, 403, "Your proposed upload exceeds the maximum allowed object size.")
  case object ENTITY_TOO_SMALL extends S3ErrorCode(7, 403, "Your proposed upload is smaller than the minimum allowed object size. Each part must be at least 5 MB in size, except the last part.")
  case object INVALID_ACCESS_KEY_ID extends S3ErrorCode(8, 403, "Forbidden")
  case object INVALID_ARGUMENT extends S3ErrorCode(9, 400, "Bad Request")
  case object INVALID_BUCKET_NAME extends S3ErrorCode(10, 400, "The specified bucket is not valid.")
  case object INVALID_CORS_ORIGIN extends S3ErrorCode(11, 400, "Insufficient information. Origin request header needed.")
  case object INVALID_CORS_METHOD extends S3ErrorCode(12, 400, "The specified Access-Control-Request-Method is not valid.")
  case object INVALID_DIGEST extends S3ErrorCode(13, 400, "Bad Request")
  case object INVALID_LOCATION_CONSTRAINT extends S3ErrorCode(14, 400, "The specified location constraint is not valid. For more information about Regions, see How to Select a Region for Your Buckets.")
  case object INVALID_LOCAL_ROOT extends S3ErrorCode(15, 501, "There is no local root.")
  case object INVALID_RANGE extends S3ErrorCode(16, 416, "The requested range is not satisfiable")
  case object INVALID_PART extends S3ErrorCode(17, 400, "One or more of the specified parts could not be found. The part may not have been uploaded, or the specified entity tag may not match the part's entity tag.")
  case object INVALID_REQUEST extends S3ErrorCode(18, 400, "Bad Request")
  case object MALFORMED_X_M_L extends S3ErrorCode(19, 400, "The XML you provided was not well-formed or did not validate against our published schema.")
  case object MAX_MESSAGE_LENGTH_EXCEEDED extends S3ErrorCode(20, 400, "Your request was too big.")
  case object METHOD_NOT_ALLOWED extends S3ErrorCode(21, 405, "Method Not Allowed")
  case object MISSING_CONTENT_LENGTH extends S3ErrorCode(22, 411, "Length Required")
  case object NO_SUCH_BUCKET extends S3ErrorCode(23, 404, "The specified bucket does not exist.")
  case object NO_SUCH_KEY extends S3ErrorCode(24, 404, "The specified key does not exist.")
  case object NO_SUCH_POLICY extends S3ErrorCode(25, 404, "The specified bucket does not have a bucket policy.")
  case object NO_SUCH_UPLOAD extends S3ErrorCode(26, 404, "Not Found")
  case object NOT_IMPLEMENTED extends S3ErrorCode(27, 501, "A header you provided implies functionality that is not implemented.")
  case object PRECONDITION_FAILED extends S3ErrorCode(28, 412, "At least one of the preconditions you specified did not hold.")
  case object REQUEST_TIME_TOO_SKEWED extends S3ErrorCode(29, 403, "Forbidden")
  case object REQUEST_TIMEOUT extends S3ErrorCode(30, 403, "Bad Request")
  case object ROUTE_NOT_FOUND extends S3ErrorCode(31, 401, "Route Not Found")
  case object SIGNATURE_DOES_NOT_MATCH extends S3ErrorCode(32, 403, "Forbidden")
  case object UNKNOWN_ERROR extends S3ErrorCode(33, 500, "Unknown error")
  case object X_AMZ_CONTENT_SHA256_MISMATCH extends S3ErrorCode(34, 400, "The provided 'x-amz-content-sha256' header does not match what was computed.")

  val values = findValues

}
