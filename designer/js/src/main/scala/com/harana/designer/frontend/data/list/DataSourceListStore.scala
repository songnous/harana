package com.harana.designer.frontend.data.list

object DataSourceListStore {

  case class State(dataSourceTypes: Map[String, List[String]])

}