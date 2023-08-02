package no.iktdev.widget

import android.appwidget.AppWidgetProvider
import android.content.Context

abstract class WidgetManager : AppWidgetProvider() {
    companion object {
        /*fun <C : AppWidgetProvider> updateWidgets(context: Context, widget: Class<C>) {
            val widgetManager = AppWidgetManager.getInstance(context.applicationContext)
            val appWidgetIds = widgetManager.getAppWidgetIds(ComponentName(context, widget))
            val updateIntent = Intent(
                AppWidgetManager.ACTION_APPWIDGET_UPDATE,
                null,
                context.applicationContext,
                widget
            )
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            context.applicationContext.sendBroadcast(updateIntent)
        }*/
    }

    abstract class BaseWidgetStorageObject(
        @Transient open val widgetId: Int,
        @Transient open val opacity: Int,
        @Transient open val themeMode: ThemeMode
    )


    abstract fun <T : WidgetBase> save(context: Context, id: Int, w: T)
    abstract fun read(context: Context, id: Int): BaseWidgetStorageObject?


    open class WidgetStore<T : BaseWidgetStorageObject>(val context: Context) {
        open fun addOrUpdate(widget: T) {}

        open fun getWithId(widgetId: Int): T? {
            return null
        }

        open fun getWithIds(widgetIds: List<Int>): List<T> {
            return emptyList()
        }

        open fun remove(widgetId: Int) {}
    }


}