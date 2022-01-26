package com.mildroid.days.ui.unsplash

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mildroid.days.databinding.ActivitySearchUnsplashBinding
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SearchUnsplashActivity : AppCompatActivity() {

    private val binding: ActivitySearchUnsplashBinding by lazy {
        ActivitySearchUnsplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}