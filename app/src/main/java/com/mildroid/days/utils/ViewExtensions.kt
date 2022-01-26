package com.mildroid.days.utils

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.mildroid.days.R

fun View.gone() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.fade(show: Boolean = true, parent: ViewGroup) {
    TransitionManager.beginDelayedTransition(parent, Fade(Fade.IN))
    if (show) this.show() else this.gone()
}

inline fun Fragment.verticalLayout(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    @ColorRes titleTextColor: Int = R.color.primary_text_color,
    crossinline onClick: () -> Unit
): View {
    val layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    val root = LinearLayout(requireContext()).apply {
        orientation = LinearLayout.HORIZONTAL
        setLayoutParams(layoutParams)
        isClickable = true
        isFocusable = true

        val outValue = TypedValue()
        context.theme.resolveAttribute(R.attr.selectableItemBackground, outValue, true)
        setBackgroundResource(outValue.resourceId)

        setOnClickListener {
            onClick()
        }
    }

    val titleTextView = TextView(requireContext()).apply {
        setText(title)
        textSize = 16f
        setTextColor(ContextCompat.getColor(context, titleTextColor))

        setLayoutParams(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            ).apply {
                setMargins(dp32, dp16, dp32, dp16)
            }
        )

//        typeface = ResourcesCompat.getFont(context, R.font.iransans_medium)

        setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
        compoundDrawablePadding = 32
    }

    return root.also {
        it.addView(titleTextView)
    }
}

fun Fragment.divider(): View = View(context).apply {
    layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        10
    )

    setBackgroundColor(ContextCompat.getColor(context, R.color.grey800))
}

val Fragment.dp32: Int
    get() = 32.toPx(resources.displayMetrics).toInt()

val Fragment.dp16: Int
    get() = 16.toPx(resources.displayMetrics).toInt()

fun Fragment.dp(value: Int): Int {
    return value.toPx(resources.displayMetrics).toInt()
}
