package no.iktdev.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RemoteViews
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import no.iktdev.ui.toPixel
import no.iktdev.widget.databinding.ViewWidgetPreviewerBinding
import kotlin.math.abs

class WidgetPreviewerView(context: Context, attrs: AttributeSet? = null): ConstraintLayout(context, attrs, 0) {
    val binding = ViewWidgetPreviewerBinding.inflate(LayoutInflater.from(context), this, true)


    fun setRenderDimensions(
        w: Int? = null, h: Int? = null,
        mw: Int? = null, mh: Int? = null
    ) {
        val setWidth = w ?: toPixel(context, 200)
        val setHeight = h ?: toPixel(context, 80)
        val setMaxHeight = mw ?: abs(this.width * 0.8).toInt()
        val setMaxWidth = mh ?: abs(this.height * 0.8).toInt()

        binding.previewer.updateLayoutParams {
            minWidth = setWidth
         //   height = setHeight
            /*if (setMaxHeight > 0) maxHeight = setMaxHeight
            if (setMaxWidth > 0) maxWidth = setMaxWidth*/
        }
    }

    fun setRenderSize(w: Int, h: Int) {
        binding.previewer.updateLayoutParams {
            minimumHeight = toPixel(context, 100)
            minimumWidth = toPixel(context, 124)
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

    private var renderContext: Context? = null
    fun setRenderContext(context: Context) {
        renderContext = context
    }
    fun render(remoteViews: RemoteViews) {
        renderContext?.let {
            binding.previewer.removeAllViews()
            val renderPreviewView = remoteViews.apply(it, null)
            renderPreviewView?.layoutParams?.apply {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                width = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            binding.previewer.addView(renderPreviewView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ))
        }
    }

}