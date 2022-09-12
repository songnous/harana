package com.harana.executor.spark.utils

object PathUtils {

    def userPath(userId: String, path: Option[String], fileName: Option[String] = None): String =
        (path, fileName) match { 
            case (Some(p), Some(f)) => s"$userId/${clean(trim(p))}/$f"
            case (Some(p), None) => s"$userId/${clean(trim(p))}"
            case (None, Some(f)) => s"$userId/$f"
            case (None, None) => userId
        }


    def path(path: Option[String], fileName: Option[String] = None): String =
        (path, fileName) match {
            case (Some(p), Some(f)) => s"${clean(trim(p))}/$f"
            case (Some(p), None) => clean(p)
            case (None, Some(f)) => f
            case (None, None) => ""
        }


    def trim(path: String): String = {
        val leftTrim = if (path.startsWith("/")) path.substring(1, path.length) else path
        if (leftTrim.endsWith("/")) leftTrim.substring(0, leftTrim.length-1) else leftTrim  
    }


    def clean(path: String): String = 
      if (path.startsWith("..")) path.substring(2) else path
}