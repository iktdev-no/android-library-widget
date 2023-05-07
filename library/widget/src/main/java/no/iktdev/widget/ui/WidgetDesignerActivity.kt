package no.iktdev.widget.ui

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import no.iktdev.widget.WidgetBase
import no.iktdev.widget.WidgetDesignManager

abstract class WidgetDesignerActivity() : AppCompatActivity(), WidgetDesignManager.DesignerUpdated {

    open var widget: WidgetBase? = null
        set(value) {
            field = value
            onUpdate()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val manager = widget?.designManager
        manager?.let {
            it.getWidgetEditView(this)?.let { v ->
                onAddWidgetEditor(v)
            }
            it.getWidgetStyleView(this)?.let { v ->
                onAddWidgetStyler(v)
            }
            it.setOnWidgetDesignUpdateListener(this)
        }
    }

    fun providerInfo(widgetId: Int): AppWidgetProviderInfo? {
        return AppWidgetManager.getInstance(this).getAppWidgetInfo(widgetId)
    }

    abstract fun onAddWidgetEditor(view: View)
    abstract fun onAddWidgetStyler(view: View)
    abstract fun onUpdateWidgetRender(view: RemoteViews)


    override fun onUpdate() {
        widget?.build().let {
            if (it != null) onUpdateWidgetRender(it)
        }
    }
}