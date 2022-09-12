package com.harana.ui.components.elements

import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.sdk.shared.models.common.{Parameter, ParameterValidator, ParameterValue}
import com.harana.ui.external.shoelace._
import slinky.core.{Component, StatelessComponent}
import slinky.core.annotations.react
import slinky.core.facade.{Fragment, React}
import slinky.web.html.{required, _}
import typings.std.global.console

import scala.scalajs._

@react class ParameterItem extends Component {

	val inputRef = React.createRef[Input.Def]
	val selectRef = React.createRef[Select.Def]
	val switchRef = React.createRef[Switch.Def]
	val textAreaRef = React.createRef[TextArea.Def]

	case class Props(parameter: Parameter,
									 i18nPrefix: String,
									 value: Option[ParameterValue] = None,
									 onChange: Option[(Parameter, ParameterValue) => Unit] = None,
									 autoFocus: Boolean = false,
									 isEditable: Boolean = true,
									 onEditing: Option[Boolean => Unit] = None)

	case class State(isValid: Boolean, value: Option[ParameterValue])
	override def initialState = State(true, None)

	def isValid = state.isValid

	private def title(p: Parameter) = i"${props.i18nPrefix}.${p.name}.title"
	private def description(p: Parameter) = io"${props.i18nPrefix}.${p.name}.description"
	private def optionTitle(p: Parameter, o: String) = i"${props.i18nPrefix}.${p.name}.option.$o.title"

	override def shouldComponentUpdate(nextProps: Props, nextState: State) =
		nextState.isValid != state.isValid


	def resetValidation = {
		setState(State(true, state.value))
	}

	def blur = {
		if (inputRef.current != null) inputRef.current.blur()
		if (selectRef.current != null) selectRef.current.blur()
		if (switchRef.current != null) switchRef.current.blur()
		if (textAreaRef.current != null) textAreaRef.current.blur()
	}

	def focus = {
		if (inputRef.current != null) inputRef.current.focus()
		if (selectRef.current != null) selectRef.current.focus()
		if (switchRef.current != null) switchRef.current.focus()
		if (textAreaRef.current != null) textAreaRef.current.focus()
	}

	def validate =
		state.value match {
			case Some(v) =>
				v match {
					case x @ ParameterValue.Boolean(_) =>
						if (props.parameter.required) setState(State(true, state.value))

					case x @ ParameterValue.Integer(_) =>
						if (props.parameter.required) setState(State(x.value.toString.nonEmpty, state.value))

					case x @ ParameterValue.Long(_) =>
						if (props.parameter.required) setState(State(x.value.toString.nonEmpty, state.value))

					case x @ ParameterValue.String(_) =>
						if (props.parameter.required) setState(State(x.value.nonEmpty, state.value))

					case _ =>
				}

			case None =>
				props.parameter match {
					case Parameter.Boolean(_, _, _, _) => setState(State(true, state.value))
					case _ => if (props.parameter.required) setState(State(false, state.value))
				}
		}

	private def onBlur = Some((_: js.Any) => {
		if (props.onEditing.isDefined) props.onEditing.map(fn => (_: Any) => fn(false))
		()
	})


	private def onChange(parameter: Parameter, value: ParameterValue) = {
		setState(State(state.isValid, Some(value)))
		if (props.onChange.isDefined) props.onChange.get(parameter, value)
	}


	private def onFocus =
		props.onEditing.map(fn => (_: Any) => fn(true))


	private def borderColor =
		if (state.isValid) None else Some("red")


	override def componentWillMount() = {
		val value: Option[ParameterValue] = if (props.value.isDefined) Some(props.value.get) else props.parameter.default
		setState(State(true, value))
	}

	
	def render() =
		div(className := "parameter-item")(
			p(s"${title(props.parameter)} ${if (props.parameter.required) "*" else ""}"),
			props.parameter match {

				case p @ Parameter.Boolean(name, _, required, validators) =>
					Switch(
						checked = state.value.map(_.asInstanceOf[ParameterValue.Boolean]),
						disabled = Some(!props.isEditable),
						invalid = Some(true),
						name = name,
						onBlur = onBlur,
						onChange = Some((value: Boolean) => onChange(p, ParameterValue.Boolean(value))),
						onFocus = onFocus,
						required = Some(required)
					).withRef(switchRef)


				case p @ Parameter.Code(name, _, required, validators) =>
					div()


				case p @ Parameter.Color(name, _, required, validators) =>
					ColorPicker(name = name, format = Some("hex"))


				case p @ Parameter.Country(name, _, required, validators) =>
					div()


				case p @ Parameter.DataTable(name, _, required, validators) =>
					div()


				case p @ Parameter.Date(name, _, required, validators) =>
					div()
//					Input(
//						clearable = Some(true),
//						disabled = Some(!props.isEditable),
//						name = name,
//						onBlur = props.onEditing.map(fn => (_: Any) => fn(false)),
//						onChange = Some((value: String) => if (props.onChange.isDefined) props.onChange.get(p, ParameterValue.String(value))),
//						onFocus = props.onEditing.map(fn => (_: Any) => fn(true)),
//						placeholder = None,
//					required = Some(required),
//						`type` = Some("date"),
//						value = valueOrDefault[ParameterValue.Instant](default)
//					)


				case p @ Parameter.DateRange(name, _, required, validators) =>
					div()


				case p @ Parameter.Decimal(name, default, required, options, maxLength, placeholder, decimalSeparator, thousandSeparator, allowNegative, allowPositive, pattern, validators) =>
					div()


				case p @ Parameter.DecimalRange(name, default, required, minValue, maxValue, validators) =>
					div()


				case p @ Parameter.Email(name, default, required, pattern, validators) =>
					Input(
						autoFocus = Some(props.autoFocus),
						borderColor = borderColor,
						clearable = Some(true),
						disabled = Some(!props.isEditable),
						helpText = description(p),
						name = name,
						onChange = Some((value: String) => onChange(p, ParameterValue.String(value))),
						pattern = pattern,
						required = Some(required),
						size = Some("large"),
						`type` = Some("email"),
						value = state.value.map(_.asInstanceOf[ParameterValue.String])
					).withKey(name).withRef(inputRef)


				case p @ Parameter.Emoji(name, _, required, validators) =>
					div()


				case p @ Parameter.File(name, _, required, validators) =>
					div()


				case p @ Parameter.GeoAddress(name, _, required, validators) =>
					div()


				case p @ Parameter.GeoCoordinate(name, _, required, validators) =>
					div()


				case p @ Parameter.Html(name, _, required, validators) =>
					div()


				case p @ Parameter.Image(name, _, required, validators) =>
					div()


				case p @ Parameter.Integer(name, default, required, options, maxLength, placeholder, thousandSeparator, allowNegative, allowPositive, pattern, validators) =>
					Input(
						autoFocus = Some(props.autoFocus),
						disabled = Some(!props.isEditable),
						helpText = description(p),
						inputMode = Some("decimal"),
						maxLength = maxLength,
						name = name,
						numbersOnly = Some(true),
						onBlur = onBlur,
						onChange = Some((value: String) => onChange(p, ParameterValue.Integer(value.toInt))),
						onFocus = onFocus,
						pattern = pattern,
						placeholder = placeholder.map(_.toString),
						required = Some(required),
						size = Some("large"),
						step = Some(1),
						`type` = Some("number"),
						value = state.value.map(_.asInstanceOf[ParameterValue.Integer].value.toString)
					).withKey(name).withRef(inputRef)


				case p @ Parameter.IntegerRange(name, default, required, minValue, maxValue, validators) =>
					div()


				case p @ Parameter.IPAddress(name, default, required, port, portDefault, options, validators) =>
					Fragment(
						Input(
							autoFocus = Some(props.autoFocus),
							borderColor = borderColor,
							className = if (port) Some("parameter-host") else None,
							clearable = Some(true),
							disabled = Some(!props.isEditable),
							helpText = description(p),
							name = name,
							onBlur = onBlur,
							onChange = Some((value: String) => onChange(p, ParameterValue.String(value))),
							onFocus = onFocus,
							placeholder = Some(i"common.parameters.host.domain"),
							required = Some(required),
							size = Some("large"),
							value = state.value.map(_.asInstanceOf[ParameterValue.IPAddress].value._1)
						).withKey(name).withRef(inputRef),
						Input(
							autoFocus = Some(props.autoFocus),
							borderColor = borderColor,
							className = Some("parameter-port"),
							disabled = Some(!props.isEditable || !port),
							helpText = description(p),
							inputMode = Some("decimal"),
							maxLength = Some(5),
							name = name,
							numbersOnly = Some(true),
							onBlur = onBlur,
							onChange = Some((value: String) => onChange(p, ParameterValue.Long(value.toLong))),
							onFocus = onFocus,
							placeholder = Some(i"common.parameters.host.port"),
							required = Some(required),
							size = Some("large"),
							`type` = Some("number"),
							value = state.value.map(_.asInstanceOf[ParameterValue.IPAddress].value._2.toString)
						).withKey(name)
					)


				case p @ Parameter.IPAddressList(name, default, required, options, port, portDefault, validators) =>
					div()


				case p @ Parameter.Json(name, _, required, validators) =>
					div()


				case p @ Parameter.Long(name, default, required, options, maxLength, placeholder, thousandSeparator, allowNegative, allowPositive, pattern, validators) =>
					Input(
						autoFocus = Some(props.autoFocus),
						borderColor = borderColor,
						disabled = Some(!props.isEditable),
						helpText = description(p),
						inputMode = Some("decimal"),
						maxLength = maxLength,
						name = name,
						numbersOnly = Some(true),
						onBlur = onBlur,
						onChange = Some((value: String) => onChange(p, ParameterValue.Long(value.toLong))),
						onFocus = onFocus,
						pattern = pattern,
						placeholder = placeholder.map(_.toString),
						required = Some(required),
						size = Some("large"),
						step = Some(1),
						`type` = Some("number"),
						value = state.value.map(_.asInstanceOf[ParameterValue.Long].value.toString)
					).withKey(name).withRef(inputRef)


				case p @ Parameter.Markdown(name, _, required, validators) =>
					div()


				case p @ Parameter.Money(name, default, required, options, validators) =>
					div()


				case p @ Parameter.Page(name, _, required, validators) =>
					div()


				case p @ Parameter.Password(name, _, required, validators) =>
					Input(
						autoFocus = Some(props.autoFocus),
						borderColor = borderColor,
						disabled = Some(!props.isEditable),
						helpText = description(p),
						name = name,
						onBlur = onBlur,
						onChange = Some((value: String) => onChange(p, ParameterValue.String(value))),
						onFocus = onFocus,
						required = Some(required),
						size = Some("large"),
						togglePassword = Some(true),
						`type` = Some("password"),
						value = state.value.map(_.asInstanceOf[ParameterValue.String].value)
					).withKey(name).withRef(inputRef)

				case p @ Parameter.SearchQuery(name, _, required, validators) =>
					div()


				case p @ Parameter.String(name, default, required, options, placeholder, maxLength, multiLine, inputFormat, pattern, validators) =>
					if (options.isEmpty) {
						if (multiLine) {
							TextArea(
								autoFocus = Some(props.autoFocus),
								borderColor = borderColor,
								disabled = Some(!props.isEditable),
								helpText = description(p),
								maxLength = maxLength,
								name = name,
								onBlur = onBlur,
								onChange = Some((value: String) => onChange(p, ParameterValue.String(value))),
								onFocus = onFocus,
								pattern = pattern,
								placeholder = placeholder,
								required = Some(required),
								rows = Some(3),
								size = Some("large"),
								value = state.value.map(_.asInstanceOf[ParameterValue.String])
							).withKey(name).withRef(textAreaRef)
						}else{
							Input(
								autoFocus = Some(props.autoFocus),
								borderColor = borderColor,
								clearable = Some(true),
								disabled = Some(!props.isEditable),
								helpText = description(p),
								maxLength = maxLength,
								name = name,
								onBlur = onBlur,
								onChange = Some((value: String) => onChange(p, ParameterValue.String(value))),
								onFocus = onFocus,
								pattern = pattern,
								placeholder = placeholder,
								required = Some(required),
								size = Some("large"),
								value = state.value.map(_.asInstanceOf[ParameterValue.String])
							).withKey(name).withRef(inputRef)
						}
					}else{
						Select(
							borderColor = borderColor,
							disabled = Some(!props.isEditable),
							helpText = description(p),
							hoist = Some(true),
							name = name,
							onBlur = onBlur,
							onChange = Some((value: String) => onChange(p, ParameterValue.String(value))),
							onFocus = onFocus,
							options = options.map { opt =>
								MenuItem(optionTitle(props.parameter, opt._1), value = Some(opt._2))
							},
							placeholder = Some("Select .."),
							required = Some(required),
							size = Some("large"),
							value = state.value.map(_.asInstanceOf[ParameterValue.String])
						).withKey(name).withRef(selectRef)
					}


				case p @ Parameter.StringList(name, default, required, options, maxLength, multiLine, inputFormat, pattern, validators) =>
					div()
//					Input(
//						clearable = Some(true),
//						disabled = Some(!props.isEditable),
//						maxLength = maxLength,
//						name = name,
//						onBlur = props.onEditing.map(fn => (_: Any) => fn(false)),
//						onChange = Some((value: String) => props.onChange(props.values + (name -> ParameterValue.StringList(value.split(",").toList.map(_.trim()))))),
//						onFocus = props.onEditing.map(fn => (_: Any) => fn(true)),
//						placeholder = None,
//						value = (if (existingValue.isDefined) existingValue else default.map(_.value)).map(_.mkString(", "))
//					)


				case p @ Parameter.Tags(name, default, required, limit, allowDuplicates, validators) =>
					div()


				case p @ Parameter.Time(name, _, required, validators) =>
					div()


				case p @ Parameter.TimeZone(name, _, required, validators) =>
					div()


				case p @ Parameter.Uri(name, default, required, pattern, validators) =>
					Input(
						autoFocus = Some(props.autoFocus),
						borderColor = borderColor,
						clearable = Some(true),
						disabled = Some(!props.isEditable),
						helpText = description(p),
						name = name,
						onBlur = onBlur,
//						onChange = Some(value => props.onChange(props.values + (p -> ParameterValue.URI(new java.net.URI(value))))),
						onFocus = onFocus,
						pattern = pattern,
						required = Some(required),
						size = Some("large"),
						`type` = Some("url"),
						value = state.value.map(_.asInstanceOf[ParameterValue.URI].value.toString)
					).withKey(name).withRef(inputRef)


				case p @ Parameter.User(name, _, required, validators) =>
					div()


				case p @ Parameter.Video(name, _, required, validators) =>
					div()


				case (_: Any) =>
					div()
			}
		)
	}
