package no.iktdev.widget.simple

import no.iktdev.widget.WidgetDesignerEvent

interface ISimpleWidgetDesignerEvent {
    interface SimpleStyleListener: WidgetDesignerEvent.StyleListener {
        fun onStyleItemSelected(item: Any?)
    }
    interface SimpleElementListener: WidgetDesignerEvent.ElementListener {
        fun onItemSelected(item: Any?)
    }
}