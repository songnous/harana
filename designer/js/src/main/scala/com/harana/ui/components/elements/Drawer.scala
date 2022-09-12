package com.harana.ui.components.elements

import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.common.{HelpCategory, Parameter, ParameterGroup, ParameterValue}
import com.harana.ui.components.ParameterGroupLayout
import com.harana.ui.external.shoelace.{Button => ShoelaceButton, Drawer => ShoelaceDrawer}
import slinky.core.Component
import slinky.core.annotations.react
import slinky.core.facade.{Fragment, React, ReactElement, ReactRef}
import slinky.web.html.{className, div, h4}

import java.util.concurrent.atomic.AtomicReference
import scala.collection.mutable
import scala.collection.mutable.{Map => MutableMap}

@react class Drawer extends Component {

	val drawerRef = React.createRef[ShoelaceDrawer.Def]
	val parameterRefs = mutable.Map.empty[Parameter, ReactRef[ParameterItem.Def]]

	type Props = Unit

	case class State(style: Option[DrawerStyle],
									 values: Option[Map[ParameterName, ParameterValue]],
									 title: Option[String],
									 width: Option[String])

	def initialState = State(None, None, None, None)

	def show(style: DrawerStyle,
					 values: Option[Map[ParameterName, ParameterValue]],
					 title: Option[String],
					 width: Option[String] = None) = {
		update(style, values.getOrElse(Map()), title, width)
		drawerRef.current.show()
	}


	def update(style: DrawerStyle,
						 values: Map[ParameterName, ParameterValue]): Unit =
		update(style, values, state.title, state.width)


	def update(style: DrawerStyle,
						 values: Map[ParameterName, ParameterValue],
						 title: Option[String],
						 width: Option[String] = None): Unit =
		style match {
			case DrawerStyle.Sectioned(parametersOrSections, _, _, _, _, _, _) =>
				parametersOrSections match {
					case Left(pt) =>
						val updatedValues = MutableMap(values.toSeq: _*)
						parameterRefs.clear()
						pt.parameterGroups.flatMap(_.parameters).foreach { p => {
							parameterRefs += (p -> React.createRef[ParameterItem.Def])
							if (!updatedValues.contains(p.name) && p.default.isDefined)
								updatedValues += (p.name -> p.default.get.asInstanceOf[ParameterValue])
							}
						}
						setState(State(Some(style), Some(updatedValues.toMap), title, width))

					case Right(_) =>
						setState(State(Some(style), None, title, width))
				}
			case _ =>
				setState(State(Some(style), Some(values), title, width))
		}

	def hide() = {
		drawerRef.current.hide()
		setState(State(None, None, None, None))
	}

	def render() =
		if (state.style.isDefined)
			state.style.get match {
				case DrawerStyle.General(innerElement, okButtonLabel, onOk, showHeader) =>
					ShoelaceDrawer(label = state.title, width = state.width, noHeader = Some(!showHeader))(
						List(
							innerElement,
							ShoelaceButton(label = Some(okButtonLabel), slot = Some("footer"), `type` = Some("primary"), onClick = Some(_ => {
								if (onOk.isDefined) onOk.get.apply()
								hide()
							}))
						)
					).withRef(drawerRef)

				case DrawerStyle.Sectioned(parametersOrSections, onChange, onOk, onCancel, showCancelButton, alwaysShowTitle, showHeader) =>
					val content: ReactElement = parametersOrSections match {

						case Left(info) =>
							if ((info.parameterGroups.size + info.additionalSections.size) == 1) {
								val group = info.parameterGroups.head

								if (alwaysShowTitle)
									div(className := "drawer-section")(
										h4(className := "drawer-header")(i"${info.i18nPrefix}.section.${group.name}.title"),
										layoutParameterGroup(group, info, onChange, group.parameters.head)
									)
								else
									layoutParameterGroup(group, info, onChange, group.parameters.head)
							} else
								List(
									Fragment(
										info.parameterGroups.zipWithIndex.map {
											case (group, _) =>
												div(className := "drawer-section")(
													h4(className := "drawer-header")(i"${info.i18nPrefix}.section.${group.name}.title"),
													layoutParameterGroup(group, info, onChange, info.parameterGroups.head.parameters.head)
												)
										} ++ info.additionalSections.map {
											case (name, content) =>
												div(className := "drawer-section")(
													h4(className := "drawer-header")(i"${info.i18nPrefix}.section.$name.title"),
													List(content)
												)
										}
									)
								)

						case Right(sections) =>
							List(
								Fragment(
									sections.map { section =>
										Fragment(
											h4(className := "drawer-header")(section._1),
											List(section._2)
										)
									}
								)
							)
					}

					val buttons: List[ReactElement] = if (showCancelButton)
						List(
							ShoelaceButton(label = Some(i"common.dialog.cancel"), slot = Some("footer"), `type` = Some("default"), onClick = Some(_ => {
								if (onCancel.isDefined) onCancel.get.apply()
								hide()
							})),
							ShoelaceButton(label = Some(i"common.dialog.save"), slot = Some("footer"), `type` = Some("success"), onClick = Some(_ => {
								if (onOk.isDefined) {
									parameterRefs.values.foreach(_.current.validate)
									if (parameterRefs.forall(_._2.current.isValid)) {
										onOk.get.apply(state.values.getOrElse(Map()))
										hide()
									}else{
										println(s"Validation failed for parameters: ${parameterRefs.filterNot(_._2.current.isValid).map(_._1.name)}")
									}
								}else
									hide()
							}))
						)
					else
						List(
							ShoelaceButton(label = Some(i"common.dialog.ok"), slot = Some("footer"), `type` = Some("success"), onClick = Some(_ => {
								if (onOk.isDefined) onOk.get.apply(state.values.getOrElse(Map()))
								hide()
							}))
						)

					ShoelaceDrawer(label = state.title, width = state.width, noHeader = Some(!showHeader))(List(content) ++ buttons).withRef(drawerRef)
			}
		else div()


	private def layoutParameterGroup(group: ParameterGroup,
																	 info: DrawerParameters,
																	 onChange: Option[(Parameter, Map[ParameterName, ParameterValue]) => Unit],
																	 focusParameter: Parameter): List[ReactElement] =
		info.layout.map(l => l(group)) match {
			case Some(ParameterGroupLayout.Grid(columns)) =>
				List(com.harana.ui.components.structure.Grid(group.parameters.map(p => parameterItem(p, group, s"${info.i18nPrefix}.section.${group.name}", onChange, p == focusParameter)), columns))

			case None | Some(ParameterGroupLayout.List) =>
				List(group.parameters.map(p => parameterItem(p, group, s"${info.i18nPrefix}.section.${group.name}", onChange, p == focusParameter)))
		}


	private def parameterItem(p: Parameter,
														group: ParameterGroup,
														i18nPrefix: String,
														onChange: Option[(Parameter, Map[ParameterName, ParameterValue]) => Unit],
														autoFocus: Boolean) =
		ParameterItem(
			parameter = p,
			i18nPrefix = i18nPrefix,
			value = state.values.getOrElse(Map()).get(p.name),
			onChange = Some((_, value) => {
				setState(this.state.copy(values = Some(state.values.getOrElse(Map()) + (p.name -> value))))
				if (onChange.isDefined) onChange.get(p, state.values.get)
			}),
			autoFocus = autoFocus
		).withKey(s"${group.name}-${p.name}").withRef(parameterRefs(p))
}

case class DrawerParameters(parameterGroups: List[ParameterGroup],
														i18nPrefix: String,
														layout: Option[ParameterGroup => ParameterGroupLayout] = None,
														additionalSections: List[(String, ReactElement)] = List())

sealed trait DrawerStyle
object DrawerStyle {

	case class General(innerElement: ReactElement,
										 okButtonLabel: String,
										 onOk: Option[() => Unit] = None,
										 showHeader: Boolean = false) extends DrawerStyle

	case class Sectioned(parametersOrSections: Either[DrawerParameters, List[(String, ReactElement)]],
											 onChange: Option[(Parameter, Map[ParameterName, ParameterValue]) => Unit] = None,
											 onOk: Option[Map[ParameterName, ParameterValue] => Unit] = None,
											 onCancel: Option[() => Unit] = None,
											 showCancelButton: Boolean = true,
											 alwaysShowTitle: Boolean = true,
											 showHeader: Boolean = false) extends DrawerStyle
}