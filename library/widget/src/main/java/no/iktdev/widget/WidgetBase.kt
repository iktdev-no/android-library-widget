package no.iktdev.widget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class WidgetBase(val context: Context) {
    var widgetId: Int? = null
    abstract fun getManager(): WidgetManager

    abstract fun getWidgetStore(): WidgetManager.BaseWidgetStorageObject?
    fun loadWidgetFromStore(onLoaded: () -> Unit = {}) {
        val store = getWidgetStore()
        store?.let {
            setOpacity(it.opacity)
            setThemeMode(it.themeMode)
            setWidgetDataFromStore(it)
        }
        onLoaded()
    }

    open fun <T>setWidgetDataFromStore(store: T) {

    }

    abstract val designManager: WidgetDesignManager<out WidgetBase, out WidgetDesignerEvent.StyleListener, out WidgetDesignerEvent.ElementListener>

    var opacityLevel: Int = 0
        private set
    var themeMode: ThemeMode = ThemeMode.AUTO
        private set


    fun setOpacity(level: Int) {
        opacityLevel = level
    }

    fun setThemeMode(mode: ThemeMode) {
        themeMode = mode
    }

    abstract fun create(): RemoteViews


    fun viewFromLayoutId(@LayoutRes layoutId: Int, parent: ViewGroup? = null, w: Int = 0, h: Int = 0): View? {
        try {
            val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
            view.measure(View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.UNSPECIFIED))
            view.layout(0,0, view.measuredWidth, view.measuredHeight)
            return view
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun renderUnsupportedViewAsImage(view: View ): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


    protected fun <A: AppCompatActivity> getPendingIntent(activity: Class<A>, action: String? = null, bundle: Bundle? = null): PendingIntent {
        return pendingIntent(activity, action, bundle)
    }

    private fun pendingIntent(activity: Class<*>, action: String? = null, bundle: Bundle? = null): PendingIntent {
        val intent = Intent(context, activity)
        action?.let { intent.action = it }
        bundle?.let { intent.putExtras(it) }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}