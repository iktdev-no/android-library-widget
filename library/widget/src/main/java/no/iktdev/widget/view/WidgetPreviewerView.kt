package no.iktdev.widget.view

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RemoteViews
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import no.iktdev.widget.R
import no.iktdev.widget.databinding.ViewWidgetPreviewerBinding

class WidgetPreviewerView(context: Context, attrs: AttributeSet? = null): LinearLayout(context, attrs, 0) {
    val binding = ViewWidgetPreviewerBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val wm = WallpaperManager.getInstance(context)
        val background = if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d(this::class.java.simpleName, "Missing permission 'READ_EXTERNAL_STORAGE', so view cannot draw wallpaper with preview")
            wm.drawable
        }
        else { ContextCompat.getDrawable(context, R.drawable.background) }
        background?.let {
            binding.background.setImageDrawable(background)
            val matrix = binding.background.imageMatrix
            val drawable = binding.background.drawable
            val intrinsicWidth = drawable.intrinsicWidth
            val intrinsicHeight = drawable.intrinsicHeight
            val screenWidth = resources.displayMetrics.widthPixels
            val screenHeight = resources.displayMetrics.heightPixels
            val scaleRatio = if (intrinsicWidth > intrinsicHeight) {
                screenHeight.toFloat() / intrinsicWidth.toFloat()
            } else {
                screenWidth.toFloat() / intrinsicHeight.toFloat()
            }
            matrix.postScale(scaleRatio, scaleRatio)
            binding.background.imageMatrix = matrix

        }
    }

    fun setRenderSize(w: Int, h: Int) {
        binding.previewer.updateLayoutParams {
            height = h
            width = w
        }
    }

    private var renderContext: Context? = null
    fun setRenderContext(context: Context) {
        renderContext = context
    }
    fun render(remoteViews: RemoteViews) {
        renderContext?.let {
            binding.previewer.removeAllViews()
            val renderPreviewView = remoteViews.apply(it, binding.previewer)
            binding.previewer.addView(renderPreviewView)
        }
    }

}