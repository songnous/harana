package com.harana.workflowexecutor.logs

import org.apache.logging.log4j.core._
import org.apache.logging.log4j.core.appender.AbstractAppender
import org.apache.logging.log4j.core.config.plugins.{Plugin, PluginAttribute, PluginElement, PluginFactory}

@Plugin(name = "Internal", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
class InternalAppender(name: String, filter: Filter, layout: Layout[_ <: Serializable]) extends AbstractAppender(name, filter, layout, false, null) {
  def append(event: LogEvent): Unit = ()
    //FIXMELogsManager.append(event)

}

object InternalAppender {
  @PluginFactory def createAppender(@PluginAttribute("name") name: String,
                                    @PluginElement("Filter") filter: Filter,
                                    @PluginElement("Layout") layout: Layout[_ <: Serializable]): Any =
    new InternalAppender(name, filter, layout)
}