package com.harana.sdk.backend.plugin

import com.harana.sdk.shared.plugin.Service.ServiceId
import com.harana.sdk.shared.plugin._
import org.osgi.framework.{BundleActivator, BundleContext, ServiceRegistration}

import scala.jdk.CollectionConverters._
import scala.collection.mutable
import scala.collection.mutable.{ListBuffer => MutableList}

abstract class Plugin extends BundleActivator {

  def ids: Map[Class[_ <: Service], ServiceId]

	def authenticationServices: Set[Class[_ <: AuthenticationHandler]]
	def backupTypes: Set[Class[_ <: BackupHandler]]
	def connectionTypes: Set[Class[_ <: Connection]]
	def eventHandlers: Set[Class[_ <: EventHandler]]
	def healthChecks: Set[Class[_ <: HealthCheck]]
	def notificationTypes: Set[Class[_ <: NotificationHandler]]
	def pageExporters: Set[Class[_ <: PageExporter]]
	def pageImporters: Set[Class[_ <: PageImporter]]
  def pageTypes: Set[Class[_ <: PageType]]
	def pageTypeSuppliers: Set[Class[_ <: PageTypeSupplier]]
	def panelTypes: Set[Class[_ <: PanelType]]
	def scheduledTasks: Set[Class[_ <: ScheduledTask]]
  def themes: Set[Class[_ <: Theme]]

  private val serviceRegistrations = MutableList[(_, ServiceRegistration[_])]()

  final def start(context: BundleContext) = {
	  register[AuthenticationHandler](context, classOf[AuthenticationHandler], authenticationServices)
	  register[BackupHandler](context, classOf[BackupHandler], backupTypes)
	  register[Connection](context, classOf[Connection], connectionTypes)
	  register[EventHandler](context, classOf[EventHandler], eventHandlers)
	  register[HealthCheck](context, classOf[HealthCheck], healthChecks)
	  register[NotificationHandler](context, classOf[NotificationHandler], notificationTypes)
	  register[PageExporter](context, classOf[PageExporter], pageExporters)
	  register[PageImporter](context, classOf[PageImporter], pageImporters)
	  register[PageType](context, classOf[PageType], pageTypes)
	  register[PageTypeSupplier](context, classOf[PageTypeSupplier], pageTypeSuppliers)
	  register[PanelType](context, classOf[PanelType], panelTypes)
	  register[ScheduledTask](context, classOf[ScheduledTask], scheduledTasks)
	  register[Theme](context, classOf[Theme], themes)
	}

  private def register[T <: Service](context: BundleContext, cls: Class[T], services: Set[Class[_ <: T]]) = {
    try {
      services.foreach { service =>
        // TODO harden if no ids etc.
        val map = mutable.Map("id" -> ids(service))
        serviceRegistrations += service -> context.registerService(cls, service.newInstance(), map.asJavaDictionary)
        //TODO
        //service.asInstanceOf[Service].onStartup()
      }
    } catch {
      case e: Exception => {
        e.printStackTrace()
        println("Failed to register services of type: " + cls)
      }
    }
  }

  final def stop(context: BundleContext) = {
    serviceRegistrations.foreach { service =>
      service._2.unregister()
      //    TODO
      //      service._1 match {
      //        case s: Service => s.onShutdown()
      //      }
    }
  }
}