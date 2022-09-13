package com.harana.workflowexecutor.pythongateway

import com.harana.sdk.backend.models.flow.utils.SparkUtils

import com.harana.spark.{PythonGateway => SparkPythonGateway}

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.net.InetAddress
import java.util.concurrent.CopyOnWriteArrayList
import py4j.CallbackClient
import py4j.Gateway
import py4j.GatewayServer
import py4j.GatewayServerListener

object GatewayServerFactory {

  // GatewayServer is missing an appropriate constructor. We have to use reflection to set it's private final fields to correct values.
  def create(
      entryPoint: Object,
      port: Int,
      connectTimeout: Int,
      readTimeout: Int,
      cbClient: CallbackClient,
      hostAddress: InetAddress
  ): GatewayServer = {
    val gatewayServer = new GatewayServer(())

    def setFieldValue(fieldName: String, value: Any): Unit = {
      val field = gatewayServer.getClass.getDeclaredField(fieldName)
      field.setAccessible(true)

      val modifiersField = classOf[Field].getDeclaredField("modifiers")
      modifiersField.setAccessible(true)
      modifiersField.setInt(field, field.getModifiers & ~Modifier.FINAL)
      field.set(gatewayServer, value)
    }
    // These fields are private. GatewayServer is missing an appropriate constructor.
    // We have to use reflection. gatewayServer.port = port
    setFieldValue("port", port)

    // gatewayServer.connectTimeout = connectTimeout
    setFieldValue("connectTimeout", connectTimeout)

    // gatewayServer.readTimeout = readTimeout
    setFieldValue("readTimeout", readTimeout)

    if (SparkPythonGateway.gatewayServerHasCallBackClient)
      // gatewayServer.cbClient = cbClient
      setFieldValue("cbClient", cbClient)

    // gatewayServer.gateway = new Gateway(entryPoint, cbClient)
    setFieldValue("gateway", new Gateway(entryPoint, cbClient))

    // gatewayServer.pythonPort = cbClient.getPort()
    setFieldValue("pythonPort", cbClient.getPort)

    // gatewayServer.pythonAddress = cbClient.getAddress()
    setFieldValue("pythonAddress", cbClient.getAddress)

    // gatewayServer.gateway.getBindings.put(GatewayServer.GATEWAY_SERVER_ID, this)
    val gatewayField = gatewayServer.getClass.getDeclaredField("gateway")
    gatewayField.setAccessible(true)
    gatewayField.get(gatewayServer).asInstanceOf[Gateway].getBindings.put(GatewayServer.GATEWAY_SERVER_ID, gatewayServer)

    // gatewayServer.customCommands = null
    setFieldValue("customCommands", null)

    // gatewayServer.listeners = new CopyOnWriteArrayList[GatewayServerListener]()
    setFieldValue("listeners", new CopyOnWriteArrayList[GatewayServerListener]())

    // gatewayServer.address = hostAddress
    setFieldValue("address", hostAddress)

    gatewayServer
  }
}