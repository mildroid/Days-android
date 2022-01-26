package com.mildroid.days.utils

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment

fun Any?.log(msg: Any? = "", tag: String = "bandOfBrothers") {
    Log.d(tag, "$msg -> $this")
}

inline fun <reified A : Activity> Context.start(
    options: ActivityOptions? = null,
    configIntent: Intent.() -> Unit = {}
) {
    startActivity(
        Intent(this, A::class.java).apply(configIntent),
        options?.toBundle()
    )
}

// dp to px converter
fun Int.toPx(displayMetrics: DisplayMetrics) = (this * displayMetrics.density)

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = APP_SETTINGS_PREFERENCES)