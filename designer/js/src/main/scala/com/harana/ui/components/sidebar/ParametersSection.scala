package com.harana.ui.components.sidebar

import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.common.{Parameter, ParameterGroup, ParameterValue}
import com.harana.ui.components.elements.ParameterItem
import com.harana.ui.components.structure.Grid
import com.harana.ui.components.{ColumnSize, ParameterGroupLayout}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class ParametersSection extends StatelessComponent with SidebarSectionComponent {

	case class Props(parameterGroups: List[ParameterGroup],
									 i18nPrefix: String,
									 values: Map[ParameterName, ParameterValue],
									 onChange: Option[(Parameter, ParameterValue) => Unit] = None,
									 layout: ParameterGroupLayout = ParameterGroupLayout.List,
									 isEditable: Boolean = false,
									 onEditing: Option[Boolean => Unit] = None)

	def render() =
		div(className := "category-content pt-5")(
			form(
				props.parameterGroups.map { group =>
					div(className := "pb-20")(
						h6(i"${props.i18nPrefix}.group.$group"),
						props.layout match {
							case ParameterGroupLayout.Grid(columns) => renderGrid(group.parameters, columns)
							case ParameterGroupLayout.List => renderList(group.parameters)
							case _ => div()
						}
					)
				}
			)
		)


	def renderGrid(parameters: List[Parameter], columns: ColumnSize) =
		Grid(parameters.map(toItem), columns)


	def renderList(parameters: List[Parameter]) =
		parameters.map(p => div(className := "form-group")(toItem(p)))


	def toItem(parameter: Parameter) =
		ParameterItem(
			parameter = parameter,
			i18nPrefix = props.i18nPrefix,
			value = props.values.get(parameter.name),
			onChange = props.onChange,
			isEditable = props.isEditable,
			onEditing = props.onEditing
		)
}