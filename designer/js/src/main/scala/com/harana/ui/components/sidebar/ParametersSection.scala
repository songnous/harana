package com.harana.ui.components.sidebar

import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup}
import com.harana.sdk.shared.utils.HMap
import com.harana.ui.components.elements.ParameterItem
import com.harana.ui.components.structure.Grid
import com.harana.ui.components.{ColumnSize, ParameterGroupLayout}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class ParametersSection extends StatelessComponent with SidebarSectionComponent {

	case class Props(groups: List[ParameterGroup],
									 i18nPrefix: String,
									 values: HMap[Parameter.Values],
									 onChange: Option[(Parameter[_], Any) => Unit] = None,
									 layout: ParameterGroupLayout = ParameterGroupLayout.List,
									 isEditable: Boolean = false,
									 onEditing: Option[Boolean => Unit] = None)

	def render() =
		div(className := "category-content pt-5")(
			form(
				props.groups.map { group =>
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


	def renderGrid(parameters: Seq[Parameter[_]], columns: ColumnSize) =
		Grid(parameters.map(toItem), columns)


	def renderList(parameters: Seq[Parameter[_]]) =
		parameters.map(p => div(className := "form-group")(toItem(p)))


	def toItem(parameter: Parameter[_]) =
		ParameterItem(
			parameter = parameter,
			i18nPrefix = props.i18nPrefix,
			value = props.values.underlying.get(parameter),
			onChange = props.onChange,
			isEditable = props.isEditable,
			onEditing = props.onEditing
		)
}