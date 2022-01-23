package com.mildroid.days.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import com.mildroid.days.R
import com.mildroid.days.ui.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = FragmentContainerView(this).also {
            it.id = R.id.fragment_container_view

            setContentView(it)
        }

        supportFragmentManager
            .beginTransaction()
            .add(root.id, SettingsFragment())
            .commit()
    }

}