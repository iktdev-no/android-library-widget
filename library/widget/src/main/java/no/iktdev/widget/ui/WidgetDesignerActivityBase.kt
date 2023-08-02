package no.iktdev.widget.ui

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import no.iktdev.widget.WidgetBase
import no.iktdev.widget.WidgetDesignManager

abstract class WidgetDesignerActivityBase() : AppCompatActivity(), WidgetDesignManager.DesignerUpdated {
    var widgetId: Int? = null
    open var widget: WidgetBase? = null
        set(value) {
            field = value
            onUpdate()
        }

    open fun getRegisteredWidgetProviderToWidgetMap(): Map<String, (Context) -> WidgetBase> {
        return emptyMap()
    }

    private fun getWidgetBasedOnProvider(providerName: String?): WidgetBase? {
        if (providerName.isNullOrBlank()) return null
        val map = getRegisteredWidgetProviderToWidgetMap()
        val instance = map[providerName] ?: return null
        return instance.invoke(this)
    }

    private fun getClassNameOfProvider(): String? {
        val result =  widgetId?.let { id ->
            getProviderInfo(id)?.provider?.className
        }
        return result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(RESULT_CANCELED)
        widgetId = intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)

        val providerName = getClassNameOfProvider()
        widget = getWidgetBasedOnProvider(providerName)
        widget?.widgetId = widgetId

        val designManager = widget?.designManager
        designManager?.let {
            it.getWidgetEditView(this)?.let { v ->
                onAddWidgetEditor(v)
            }
            it.getWidgetStyleView(this)?.let { v ->
                onAddWidgetStyler(v)
            }
            it.setOnWidgetDesignUpdateListener(this)
        }
    }

    fun getProviderInfo(widgetId: Int): AppWidgetProviderInfo? {
        return AppWidgetManager.getInstance(this).getAppWidgetInfo(widgetId)
    }

    abstract fun onAddWidgetEditor(view: View)
    abstract fun onAddWidgetStyler(view: View)
    abstract fun onUpdateWidgetRender(view: RemoteViews)

    open fun onSaveWidget() {
        setResult(RESULT_OK)
    }


    override fun onUpdate() {
        widget?.create().let {
            if (it != null) onUpdateWidgetRender(it)
        }
    }
}