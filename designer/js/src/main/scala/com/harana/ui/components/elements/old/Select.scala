package com.harana.ui.components.elements.old

import com.harana.sdk.shared.utils.Random
import com.harana.ui.components.emptyFn
import com.harana.ui.external.select2.{Select2, Select2Item, Select2Options}
import slinky.core.Component
import slinky.core.annotations.react

@react class Select extends Component {

	case class State(selectedItem: Option[SelectItem])
	def initialState = State(selectedItem = props.selectedItem)

	case class Props(items: List[SelectItem],
									 selectedItem: Option[SelectItem] = None,
									 placeholder: Option[String] = None,
									 allowFilter: Boolean = true,
									 onSelect: SelectItem => Unit = emptyFn[SelectItem])

	def render() = {
		val options = Select2Options("Select ..", false, Some("100%"))
		Select2(props.items.map { item =>
			val id: String = item.id.getOrElse("")
			println(item.title + " - " + Random.short)
			new Select2Item {
				val text = item.title
				val id = item.id
				val selected = item.active
				val disabled = item.disabled
				val children = List()
			}
		}, options)
	}
}


	//	def render() = {
//		//TODO: Why can't this be inlined ?
//		val text = state.selectedItem.map(_.title).getOrElse(props.placeholder.getOrElse("Select .."))
//
//		BPSelect(
//			items = props.items.map(_.asInstanceOf[js.Object]).toJSArray,
//			itemRenderer = itemRenderer,
//			popoverProps = BPPopover.Props(defaultIsOpen = true, usePortal = true, minimal = true)
//		)(
//			BPButton(
//				rightIcon = "caret-down",
//				text = text
//			)()
//		)
//	}
//
//	val itemRenderer: ItemRenderer = (item: js.Object, _ : ItemRendererProps) => {
//		val selectItem = item.asInstanceOf[SelectItem]
//		val key = selectItem.id.getOrElse(selectItem.title)
//
//		BPMenuItem(
//			active = selectItem.active,
//			disabled = selectItem.disabled,
//			key = key,
//			label = selectItem.label.orUndefined,
//			text = selectItem.title
//		)
//	}
//}

case class SelectItem(title: String,
											id: Option[String] = None,
											label: Option[String] = None,
											active: Boolean = false,
											disabled: Boolean = false)