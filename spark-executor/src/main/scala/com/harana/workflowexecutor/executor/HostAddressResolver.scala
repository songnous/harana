package com.harana.workflowexecutor.executor

import com.harana.sdk.backend.models.designer.flow.utils.Logging

import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface
import scala.util.Try
import scala.collection.JavaConverters._

object HostAddressResolver extends Logging {

  def findHostAddress(): InetAddress = {
    Try {
      val interfaces = NetworkInterface.getNetworkInterfaces.asScala
      interfaces.flatMap { n =>
        n.getInetAddresses.asScala.filter { address =>
          !address.isInstanceOf[Inet6Address] &&
          !address.isLoopbackAddress &&
          !address.isSiteLocalAddress &&
          !address.isLinkLocalAddress &&
          !address.isAnyLocalAddress &&
          !address.isMulticastAddress &&
          !(address.getHostAddress == "255.255.255.255")
        }
      }
    }.get.toList.headOption.getOrElse(InetAddress.getByName("127.0.0.1"))
  }
}
