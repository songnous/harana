package com.harana.ui.components.elements

import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup}
import com.harana.sdk.shared.utils.HMap
import com.harana.ui.components.ParameterGroupLayout
import com.harana.ui.external.shoelace.{Tab, TabGroup, TabPanel, Button => ShoelaceButton, Dialog => ShoelaceDialog}
import slinky.core.annotations.react
import slinky.core.facade.{Fragment, React, ReactElement, ReactRef}
import slinky.core.{Component, CustomAttribute}
import slinky.web.html.h4

import scala.collection.mutable.{Map => MutableMap}

@react class Dialog extends Component {

	var dialogRef = React.createRef[ShoelaceDialog.Def]
	var parameterRefs = Map.empty[Parameter[_], ReactRef[ParameterItem.Def]]

	type Props = Unit

	case class State(style: Option[DialogStyle],
									 values: Option[HMap[Parameter.Values]],
									 title: Option[String],
									 width: Option[String])

	def initialState = State(None, None, None, None)


	def show(style: DialogStyle,
					 values: Option[HMap[Parameter.Values]] = None,
					 title: Option[String],
					 width: Option[String] = None) = {
		update(Some(style), values, title, width)
		dialogRef.current.show()
	}

	def update(style: Option[DialogStyle] = None,
						 values: Option[HMap[Parameter.Values]] = None,
						 title: Option[String] = None,
						 width: Option[String] = None) =
		style match {
			case Some(DialogStyle.Tabbed(parametersOrTabs, _, _, _, _, _)) =>
				parametersOrTabs match {
					case Left(pt) =>
						var updatedValues = values.getOrElse(state.values.getOrElse(HMap.empty))
						pt.parameterGroups.flatMap(_.parameters).foreach { p =>
							parameterRefs += (p -> React.createRef[ParameterItem.Def])
							if (!updatedValues.contains(p.name) && p.default.isDefined) {
								updatedValues +~= (p, p.default.get)
							}
						}
						setState(State(style, Some(updatedValues), title, width))

					case Right(_) =>
						setState(State(style, None, title, width))
				}
			case _ =>
				setState(State(style, values, title, width))
		}


	def hide() = {
		dialogRef.current.hide()
		setState(State(None, None, None, None))
	}

	val slotAttr = CustomAttribute[String]("slot")

	def render() = {
		if (state.style.isDefined)
			state.style.get match {
				case DialogStyle.Confirm(confirmLabel, confirmButtonLabel, onOk, onCancel) =>
					ShoelaceDialog(label = state.title, width = state.width)(
						List(
							confirmLabel,
							ShoelaceButton(label = Some(i"common.dialog.cancel"), slot = Some("footer"), `type` = Some("default"), onClick = Some(_ => {
								if (onCancel.isDefined) onCancel.get.apply()
								hide()
							})),
							ShoelaceButton(label = Some(confirmButtonLabel), slot = Some("footer"), `type` = Some("primary"), onClick = Some(_ => {
								if (onOk.isDefined) onOk.get.apply()
								hide()
							}))
						)
					).withRef(dialogRef)

				case DialogStyle.General(innerElement, okButtonLabel, onOk, headerElement) =>
					ShoelaceDialog(
						label = state.title,
						width = state.width,
						noHeader = Some(headerElement.isDefined),
						headerElement = headerElement
					)(
						List(
							h4(slotAttr := "label")("Title"),
							innerElement,
							ShoelaceButton(label = Some(okButtonLabel), slot = Some("footer"), `type` = Some("primary"), onClick = Some(_ => {
								if (onOk.isDefined) onOk.get.apply()
								hide()
							}))
						)
					).withRef(dialogRef)

				case DialogStyle.Tabbed(parametersOrTabs, onChange, onOk, onCancel, showCancelButton, headerElement) =>
					val content: ReactElement = parametersOrTabs match {
						case Left(info) =>
							if ((info.parameterGroups.size + info.additionalTabs.size) == 1)
								layoutParameterGroup(info.parameterGroups.head, info, onChange, info.parameterGroups.head.parameters.head)
							else {
								TabGroup(placement = Some("top"), noScrollControls = Some(true))(
									List(
										Fragment(
											info.parameterGroups.map { group =>
												Tab(label = i"${info.i18nPrefix}.tab.${group.name}.title", panel = group.name).withKey(group.name)
											} ++ info.additionalTabs.map {
												case (name, _) => Tab(label = i"${info.i18nPrefix}.tab.$name.title", panel = name).withKey(name)
											},

											info.parameterGroups.map { group =>
												TabPanel(name = group.name)(layoutParameterGroup(group, info, onChange, info.parameterGroups.head.parameters.head)).withKey(group.name)
											} ++ info.additionalTabs.map {
												case (name, content) => TabPanel(name = name)(List(content)).withKey(name)
											}
										)
									)
								)
							}

						case Right(tabs) =>
							TabGroup(placement = Some("top"), noScrollControls = Some(true))(
								List(
									Fragment(
										tabs.zipWithIndex.map { case ((title, _), index) =>
											Tab(label = title, panel = index.toString).withKey("tab-${index.toString}")
										},
										tabs.zipWithIndex.map { case ((_, content), index) =>
											TabPanel(name = index.toString)(List(content)).withKey("tabpanel-${index.toString}")
										}
									)
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
//									parameterRefs.values.foreach(_.current.validate)
									if (parameterRefs.forall(_._2.current.isValid)) {
										onOk.get.apply(state.values.getOrElse(HMap.empty))
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
								if (onOk.isDefined) onOk.get.apply(state.values.getOrElse(HMap.empty))
								hide()
							}))
						)

					ShoelaceDialog(
						label = state.title,
						width = state.width,
						noHeader = Some(headerElement.isDefined),
						headerElement = headerElement
					)(List(content) ++ buttons).withRef(dialogRef)
			}
		else Fragment()
	}


	private def layoutParameterGroup(group: ParameterGroup,
																	 info: DialogParameters,
																	 onChange: Option[(Parameter[_], HMap[Parameter.Values]) => Unit],
																	 focusParameter: Parameter[_]): List[ReactElement] =

		info.layout.map(l => l(group)) match {
			case Some(ParameterGroupLayout.Grid(columns)) =>
				List(com.harana.ui.components.structure.Grid(group.parameters.map(p => parameterItem(p, s"${info.i18nPrefix}.tab.${group.name}", onChange, p == focusParameter)), columns))

			case None | Some(ParameterGroupLayout.List) =>
				List(group.parameters.map(p => parameterItem(p, s"${info.i18nPrefix}.tab.${group.name}", onChange, p == focusParameter)))
		}


	private def parameterItem(p: Parameter[_],
														i18nPrefix: String,
														onChange: Option[(Parameter[_], HMap[Parameter.Values]) => Unit],
														autoFocus: Boolean) =
		ParameterItem(
			parameter = p,
			i18nPrefix = i18nPrefix,
			value = state.values.getOrElse(HMap.empty).underlying.get(p),
			onChange = Some((_, value) => {
				setState(this.state.copy(values = Some(state.values.getOrElse(HMap.empty) +~ (p -> value))))
				if (onChange.isDefined) onChange.get(p, state.values.get)
			}),
			autoFocus = autoFocus
		)
}

case class DialogParameters(parameterGroups: List[ParameterGroup],
														i18nPrefix: String,
														layout: Option[ParameterGroup => ParameterGroupLayout] = None,
														additionalTabs: List[(String, ReactElement)] = List())

sealed trait DialogStyle
object DialogStyle {

	case class Confirm(confirmLabel: String,
										 confirmButtonLabel: String,
										 onOk: Option[() => Unit] = None,
										 onCancel: Option[() => Unit] = None) extends DialogStyle

	case class General(innerElement: ReactElement,
										 okButtonLabel: String,
										 onOk: Option[() => Unit] = None,
										 headerElement: Option[ReactElement] = None) extends DialogStyle

	case class Tabbed(parametersOrTabs: Either[DialogParameters, List[(String, ReactElement)]],
										onChange: Option[(Parameter[_], HMap[Parameter.Values]) => Unit] = None,
										onOk: Option[HMap[Parameter.Values] => Unit] = None,
										onCancel: Option[() => Unit] = None,
										showCancelButton: Boolean = true,
										headerElement: Option[ReactElement] = None) extends DialogStyle
}