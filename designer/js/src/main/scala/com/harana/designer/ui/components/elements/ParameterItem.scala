package com.harana.ui.components.elements

import com.harana.designer.frontend.utils.i18nUtils.ops
import com.harana.sdk.shared.models.flow.parameters._
import com.harana.sdk.shared.utils.Random
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

	case class Props(parameter: Parameter[_],
									 i18nPrefix: String,
									 value: Option[Any] = None,
									 onChange: Option[(Parameter[_], Any) => Unit] = None,
									 autoFocus: Boolean = false,
									 isEditable: Boolean = true,
									 onEditing: Option[Boolean => Unit] = None) {
		override def equals(obj: Any) = obj match {
			case p: Props => this.parameter == p.parameter && this.value == p.value
			case _ => false
		}
	}

	case class State(isValid: Boolean, value: Option[Any])
	override def initialState = State(true, None)

	def isValid = state.isValid

	private def title(p: Parameter[_]) = i"${props.i18nPrefix}.${p.name}.title"
	private def description(p: Parameter[_]) = io"${props.i18nPrefix}.${p.name}.description"
	private def optionTitle(p: Parameter[_], o: String) = i"${props.i18nPrefix}.${p.name}.option.$o.title"


	override def componentWillReceiveProps(nextProps: Props) = {
		val value = if (nextProps.value.nonEmpty) Some(nextProps.value.get) else nextProps.parameter.default
		setState(State(true, value))
	}


	override def shouldComponentUpdate(nextProps: Props, nextState: State) =
		props != nextProps || state != nextState || nextProps.value != nextState.value


	def resetValidation = {
		setState(State(true, props.value))
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
		props.value match {
			case Some(v) =>
				v match {
					case x @ Boolean => if (props.parameter.required) setState(State(isValid = true, props.value))
					case x @ Int => if (props.parameter.required) setState(State(isValid = x.toString.nonEmpty, props.value))
					case x @ Long => if (props.parameter.required) setState(State(isValid = x.toString.nonEmpty, props.value))
					case x: String => if (props.parameter.required) setState(State(isValid = x.nonEmpty, props.value))
					case _ => if (props.parameter.required) setState(State(isValid = true, props.value))
				}

			case None =>
				props.parameter match {
					case BooleanParameter(_, _, _) => setState(State(true, props.value))
					case _ => if (props.parameter.required) setState(State(false, props.value))
				}
		}

	private def onBlur = Some((_: js.Any) => {
		if (props.onEditing.nonEmpty) props.onEditing.map(fn => (_: Any) => fn(false))
		()
	})


	private def onChange(parameter: Parameter[_], value: Any) = {
		setState(State(state.isValid, Some(value)))
		if (props.onChange.nonEmpty) props.onChange.get(parameter, value)
	}


	private def onFocus =
		props.onEditing.map(fn => (_: Any) => fn(true))


	private def borderColor =
		if (state.isValid) None else Some("red")


	def render() =
		div(className := "parameter-item")(
			p(s"${title(props.parameter)} ${if (props.parameter.required) "*" else ""}"),
			props.parameter match {

				case p @ BooleanParameter(name, required, default) =>
					Switch(
						checked = props.value.map(_.asInstanceOf[Boolean]),
						disabled = Some(!props.isEditable),
						invalid = Some(true),
						name = name,
						onBlur = onBlur,
						onChange = Some((value: Boolean) => onChange(p, value)),
						onFocus = onFocus,
						required = Some(required)
					).withRef(switchRef)


				case p @ CodeSnippetParameter(name, required, _, validators) =>
					div()


				case p @ ColorParameter(name, required, _) =>
					ColorPicker(name = name, format = Some("hex"))


				case p @ CountryParameter(name, required, _, validators) =>
					div()


				case p @ EmailParameter(name, required, default, pattern, validators) =>
					Input(
						autoFocus = Some(props.autoFocus),
						borderColor = borderColor,
						clearable = Some(true),
						disabled = Some(!props.isEditable),
						helpText = description(p),
						name = name,
						onChange = Some((value: String) => onChange(p, value)),
						pattern = pattern,
						required = Some(required),
						size = Some("large"),
						`type` = Some("email"),
						value = props.value.map(_.asInstanceOf[String])
					).withKey(name).withRef(inputRef)


				case p @ EmojiParameter(name, required, _) =>
					div()



				case p @ HTMLParameter(name, required, _, validators) =>
					div()


				case p @ IntParameter(name, required, default, options, maxLength, placeholder, thousandSeparator, allowNegative, allowPositive, pattern, validators) =>
					Input(
						autoFocus = Some(props.autoFocus),
						disabled = Some(!props.isEditable),
						helpText = description(p),
						inputMode = Some("decimal"),
						maxLength = maxLength,
						name = name,
						numbersOnly = Some(true),
						onBlur = onBlur,
						onChange = Some((value: String) => onChange(p, value)),
						onFocus = onFocus,
						pattern = pattern,
						placeholder = placeholder.map(_.toString),
						required = Some(required),
						size = Some("large"),
						step = Some(1),
						`type` = Some("number"),
						value = props.value.map(_.asInstanceOf[String])
					).withKey(name).withRef(inputRef)



				case p @ IPAddressParameter(name, required, default, portDefault, options, validators) =>
					Fragment(
						Input(
							autoFocus = Some(props.autoFocus),
							borderColor = borderColor,
							className = Some("parameter-host"),
							clearable = Some(true),
							disabled = Some(!props.isEditable),
							helpText = description(p),
							name = name,
							onBlur = onBlur,
							onChange = Some((value: String) => onChange(p, value)),
							onFocus = onFocus,
							placeholder = Some(i"common.parameters.host.domain"),
							required = Some(required),
							size = Some("large"),
							value = props.value.map(_.asInstanceOf[String])
						).withKey(name).withRef(inputRef),
						Input(
							autoFocus = Some(props.autoFocus),
							borderColor = borderColor,
							className = Some("parameter-port"),
							disabled = Some(!props.isEditable),
							helpText = description(p),
							inputMode = Some("decimal"),
							maxLength = Some(5),
							name = name,
							numbersOnly = Some(true),
							onBlur = onBlur,
							onChange = Some((value: String) => onChange(p, value)),
							onFocus = onFocus,
							placeholder = Some(i"common.parameters.host.port"),
							required = Some(required),
							size = Some("large"),
							`type` = Some("number"),
							value = props.value.map(_.asInstanceOf[String])
						).withKey(name)
					)


				case p @ JSONParameter(name, required, _, validators) =>
					div()


				case p @ LongParameter(name, required, _, maxLength, placeholder, pattern, _) =>
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
						onChange = Some((value: String) => onChange(p, value)),
						onFocus = onFocus,
						pattern = pattern,
						placeholder = placeholder.map(_.toString),
						required = Some(required),
						size = Some("large"),
						step = Some(1),
						`type` = Some("number"),
						value = props.value.map(_.asInstanceOf[Long].toString)
					).withKey(name).withRef(inputRef)


				case p @ MarkdownParameter(name, required, _, validators) =>
					div()


				case p @ PasswordParameter(name, required, default, validators) =>
					Input(
						autoFocus = Some(props.autoFocus),
						borderColor = borderColor,
						disabled = Some(!props.isEditable),
						helpText = description(p),
						name = name,
						onBlur = onBlur,
						onChange = Some((value: String) => onChange(p, value)),
						onFocus = onFocus,
						required = Some(required),
						size = Some("large"),
						togglePassword = Some(true),
						`type` = Some("password"),
						value = props.value.map(_.asInstanceOf[String])
					).withKey(name).withRef(inputRef)

				case p @ SearchQueryParameter(name, required, default, validators) =>
					div()


				case p @ StringParameter(name, required, default, options, placeholder, maxLength, multiLine, inputFormat, pattern, validators) =>
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
								onChange = Some((value: String) => onChange(p, value)),
								onFocus = onFocus,
								pattern = pattern,
								placeholder = placeholder,
								required = Some(required),
								rows = Some(3),
								size = Some("large"),
								value = props.value.map(_.asInstanceOf[String])
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
								onChange = Some((value: String) => onChange(p, value)),
								onFocus = onFocus,
								pattern = pattern,
								placeholder = placeholder,
								required = Some(required),
								size = Some("large"),
								value = props.value.map(_.asInstanceOf[String])
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
							onChange = Some((value: String) => onChange(p, value)),
							onFocus = onFocus,
							options = options.map(o => MenuItem(optionTitle(props.parameter, o._1), value = Some(o._2))),
							placeholder = Some("Select .."),
							required = Some(required),
							size = Some("large"),
							value = props.value.map(_.asInstanceOf[String])
						).withKey(name).withRef(selectRef)
					}


				case p @ TagsParameter(name, required, default, validators) =>
					div()


				case p @ TimeZoneParameter(name, required, _, validators) =>
					div()


				case p @ URIParameter(name, required, default, options, pattern, validators) =>
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
						value = props.value.map(_.asInstanceOf[String])
					).withKey(name).withRef(inputRef)


				case (_: Any) =>
					div()
			}
		)
	}
