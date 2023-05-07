package no.iktdev.widget

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * Class used to handle design changes.
 * This class will be responsible to render widget changes and widget design
 * Use this class to provide view that can be injected into Activities layout with bindings to widget itself, so that changes to layout will apply to widget.
 *
 *  [Activity] <---> [Extended@BaseWidget] <-----> [WidgetDesignManager]
 *     ^                               (Events/Listener)     ^
 *     |____________________[Layout]_________________________|
 *
 *
 *
 * @param onWidgetStyledListener listener for style change
 */
abstract class WidgetDesignManager<W: WidgetBase, S: WidgetDesignerEvent.StyleListener, E: WidgetDesignerEvent.ElementListener>(widget: W, onWidgetStyledListener: S?, onWidgetEditedListener: E?) {
    protected var widgetActivityListener: DesignerUpdated?= null
    fun setOnWidgetDesignUpdateListener(listener: DesignerUpdated) {
        widgetActivityListener = listener
    }

    /**
     * View to represent styles that can be applied for widget
     *
     * @param WidgetDesignActivity
     * @return Isolated Renderered view for activity
     */
    abstract fun getWidgetStyleView(activity: AppCompatActivity): View?

    /**
     * View to represent items that can be set for widget
     *
     * @param WidgetDesignActivity
     * @return Isolated Renderered view for activity
     */
    abstract fun getWidgetEditView(activity: AppCompatActivity): View?


    interface DesignerUpdated {
        fun onUpdate()
    }
}