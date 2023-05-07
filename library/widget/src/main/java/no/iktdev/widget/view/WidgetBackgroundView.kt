package no.iktdev.widget.view

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.annotation.StyleableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import no.iktdev.widget.R
import no.iktdev.widget.ThemeMode
import no.iktdev.widget.databinding.ViewWidgetBackgroundBinding

class WidgetBackgroundView(context: Context, attrs: AttributeSet? = null): ConstraintLayout(context, attrs, 0) {
    val binding: ViewWidgetBackgroundBinding = ViewWidgetBackgroundBinding.inflate(LayoutInflater.from(context), this, true)
    var selectedTheme: ThemeMode = ThemeMode.AUTO
        private set
    var opacity: Int = 0
        private set

    init {
        binding.backgroundFollowSystemTheme.setOnCheckedChangeListener{ _, p1 ->
            binding.backgroundSelected.children.forEach { it.isEnabled = !p1 }
            if (p1) {
                selectedTheme = ThemeMode.AUTO
                listener?.onThemeMode(selectedTheme)
            } else {
                selectedTheme = darkOrLight
                listener?.onThemeMode(selectedTheme)
            }
        }
        binding.backgroundSelected.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.background_dark -> {
                    selectedTheme = ThemeMode.DARK
                    listener?.onThemeMode(selectedTheme)
                    darkOrLight = selectedTheme
                }
                R.id.background_light -> {
                    selectedTheme = ThemeMode.LIGHT
                    listener?.onThemeMode(selectedTheme)
                    darkOrLight = selectedTheme

                }
            }
        }
        binding.backgroundOpacitySlider.addOnChangeListener { _, value, fromUser -> if (fromUser) {
            listener?.onOpacityChanged(value.toInt())
            opacity = value.toInt()
        } }

        binding.backgroundDark.isChecked = true
        binding.backgroundFollowSystemTheme.setChecked(true)

        attrs?.let { attr ->
            context.obtainStyledAttributes(attr, R.styleable.WidgetBackgroundView).let {
                onTypedArray(it)
                it.recycle()
            }
        }
    }

    protected fun applyTextColorAttr(attr: TypedArray, text: TextView, @StyleableRes id: Int) {
        if (attr.hasValue(id)) {
            text.setTextColor(attr.getColor(id, 4054148))
        }
    }

    protected fun applyTextAttr(attr: TypedArray, text: TextView, @StyleableRes id: Int) {
        attr.getString(id)?.let { text.text = it }
    }

    fun onTypedArray(a: TypedArray) {
        val textView = listOf<TextView>(
            binding.titleBackgroundColor,
            binding.titleBackgroundManual,
            binding.titleBackgroundOpacity
        )
        textView.forEach { applyTextColorAttr(a, it, R.styleable.WidgetBackgroundView_textColor) }

        binding.backgroundDark.apply {
            if (a.hasValue(R.styleable.WidgetBackgroundView_textColor))
                this.setTextColor(a.getColor(R.styleable.WidgetBackgroundView_textColor, 4054148))
        }
        binding.backgroundLight.apply {
            if (a.hasValue(R.styleable.WidgetBackgroundView_textColor))
                this.setTextColor(a.getColor(R.styleable.WidgetBackgroundView_textColor, 4054148))
        }

        binding.titleBackgroundManualLine.apply {
            this.backgroundTintList = ColorStateList.valueOf(a.getColor(R.styleable.WidgetBackgroundView_dividerTint, 4054148))
        }
        binding.titleBackgroundOpacityLine.apply {
            this.backgroundTintList = ColorStateList.valueOf(a.getColor(R.styleable.WidgetBackgroundView_dividerTint, 4054148))
        }

        binding.backgroundOpacitySlider.apply {
            thumbTintList = ColorStateList.valueOf(a.getColor(R.styleable.WidgetBackgroundView_accentColor, 4054148))
            trackTintList = ColorStateList.valueOf(a.getColor(R.styleable.WidgetBackgroundView_textColor, 4054148))
            tickTintList = ColorStateList.valueOf(a.getColor(R.styleable.WidgetBackgroundView_accentColor, 4054148))
        }

        binding.card.setCardBackgroundColor(a.getColor(R.styleable.WidgetBackgroundView_cardBackgroundColor, 4054148))

        applyTextAttr(a, binding.backgroundLight, R.styleable.WidgetBackgroundView_textOptionLight)
        applyTextAttr(a, binding.backgroundDark, R.styleable.WidgetBackgroundView_textOptionDark)
        applyTextAttr(a, binding.titleBackgroundOpacity, R.styleable.WidgetBackgroundView_textTitleOpacity)
        applyTextAttr(a, binding.titleBackgroundColor, R.styleable.WidgetBackgroundView_textTitle)
        applyTextAttr(a, binding.titleBackgroundManual, R.styleable.WidgetBackgroundView_textTitleManualThemeColor)

    }

    private var listener: BackgroundStyleEvent? = null
    fun setBackgroundStyleEventListener(listener: BackgroundStyleEvent) {
        this.listener = listener
    }

    private var darkOrLight: ThemeMode = ThemeMode.DARK
    interface BackgroundStyleEvent {
        fun onOpacityChanged(strength: Int)
        fun onThemeMode(mode: ThemeMode)
    }

}