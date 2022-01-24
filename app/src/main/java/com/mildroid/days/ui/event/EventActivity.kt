package com.mildroid.days.ui.event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mildroid.days.databinding.ActivityEventBinding
import com.mildroid.days.utils.log

class EventActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityEventBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent?.extras?.let {
            it.get("event").log()
        }
    }
}