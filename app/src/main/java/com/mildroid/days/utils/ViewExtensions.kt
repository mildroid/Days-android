package com.mildroid.days.utils

import android.view.View
import android.view.ViewGroup
import androidx.transition.Fade
import androidx.transition.TransitionManager

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
