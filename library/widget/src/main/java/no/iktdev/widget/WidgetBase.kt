package no.iktdev.widget

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity

abstract class WidgetBase(val context: Context) {

    abstract val designManager: WidgetDesignManager<WidgetBase, WidgetDesignerEvent.StyleListener, WidgetDesignerEvent.ElementListener>

    //val testw = testWidget(context)
    //val designManage2r: WidgetDesignManager<WidgetBase, WidgetDesignerEvent.StyleListener, WidgetDesignerEvent.ElementListener> = testWidgetManager(this)

    /*class testWidgetManager(context: WidgetBase): WidgetDesignManager<WidgetBase, WidgetDesignerEvent.StyleListener, WidgetDesignerEvent.ElementListener>(context, null, null) {
        override fun getWidgetStyleView(activity: Activity): View? {
            TODO("Not yet implemented")
        }

        override fun getWidgetEditView(activity: Activity): View? {
            TODO("Not yet implemented")
        }
    }*/

    /*class testWidget(context: Context): WidgetBase(context) {
        override val designManager: WidgetDesignManager<WidgetBase, WidgetDesignerEvent.StyleListener, WidgetDesignerEvent.ElementListener>
            get() = TODO("Not yet implemented")

        override fun build(): RemoteViews {
            TODO("Not yet implemented")
        }

    }*/


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

    abstract fun build(): RemoteViews


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