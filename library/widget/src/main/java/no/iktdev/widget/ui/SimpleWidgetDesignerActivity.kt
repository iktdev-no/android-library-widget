package no.iktdev.widget.ui

import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RemoteViews
import no.iktdev.widget.ThemeMode
import no.iktdev.widget.WidgetDesignManager
import no.iktdev.widget.databinding.ActivityWidgetDesignerSimpleBinding
import no.iktdev.widget.view.WidgetBackgroundView

open class SimpleWidgetDesignerActivity() : WidgetDesignerActivityBase(),
    WidgetDesignManager.DesignerUpdated,
    WidgetBackgroundView.BackgroundStyleEvent{
    lateinit var binding: ActivityWidgetDesignerSimpleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWidgetDesignerSimpleBinding.inflate(LayoutInflater.from(this))
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backgroundStyleView.setBackgroundStyleEventListener(this)
        binding.widgetPreviewView.setRenderContext(applicationContext)
        setResult(RESULT_CANCELED)
        widgetId = intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID) ?: return
        binding.saveWidget.setOnClickListener {
            onSaveWidget()
        }

        widget?.loadWidgetFromStore {
            onUpdate()
        }
    }

    override fun onStart() {
        super.onStart()
        widgetId?.let { id ->
            val provider = getProviderInfo(id)
            binding.widgetPreviewView.setRenderSize(w = provider?.minWidth ?: 124, h = provider?.minHeight ?: 80)
        }

    }


    override fun onAddWidgetEditor(view: View) {
        binding.widgetRenderManagerEdit.addView(view)
    }
    override fun onAddWidgetStyler(view: View) {
        binding.widgetRenderManagerStyle.addView(view)
    }
    override fun onUpdateWidgetRender(view: RemoteViews) {
        binding.widgetPreviewView.render(view)
    }


    override fun onUpdate() {
        widget?.create().let {
            if (it != null) onUpdateWidgetRender(it)
        }
    }

    override fun onOpacityChanged(strength: Int) {
        widget?.setOpacity(strength)
        onUpdate()
    }

    override fun onThemeMode(mode: ThemeMode) {
        widget?.setThemeMode(mode)
        onUpdate()
    }
}