package no.iktdev.widget.simple

import no.iktdev.widget.WidgetBase
import no.iktdev.widget.WidgetDesignManager
import no.iktdev.widget.WidgetDesignerEvent

abstract class SimpleWidgetDesignManager<W: WidgetBase>(widget: W, styleListener: ISimpleWidgetDesignerEvent.SimpleStyleListener? = null, elementListener: WidgetDesignerEvent.ElementListener? = null):
    WidgetDesignManager<W, WidgetDesignerEvent.StyleListener, WidgetDesignerEvent.ElementListener>(widget, styleListener, elementListener)  {
}