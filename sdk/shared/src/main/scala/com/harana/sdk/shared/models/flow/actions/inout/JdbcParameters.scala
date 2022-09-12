package com.harana.sdk.shared.models.flow.actions.inout

import com.harana.sdk.shared.models.flow.parameters.{Parameters, StringParameter}

trait JdbcParameters {
  this: Parameters =>

  val jdbcUrlParameter = StringParameter(name = "url", description = None)
  setDefault(jdbcUrlParameter, "jdbc:mysql://HOST:PORT/DATABASE?user=DB_USER&password=DB_PASSWORD")

  def getJdbcUrl = $(jdbcUrlParameter)

  def setJdbcUrl(value: String): this.type = set(jdbcUrlParameter, value)

  val jdbcDriverClassNameParameter = StringParameter(name = "driver", description = None)
  setDefault(jdbcDriverClassNameParameter, "com.mysql.jdbc.Driver")

  def getJdbcDriverClassName = $(jdbcDriverClassNameParameter)

  def setJdbcDriverClassName(value: String): this.type = set(jdbcDriverClassNameParameter, value)

  val jdbcTableNameParameter = StringParameter(name = "table", description = None)

  def getJdbcTableName = $(jdbcTableNameParameter)

  def setJdbcTableName(value: String): this.type = set(jdbcTableNameParameter, value)

}
