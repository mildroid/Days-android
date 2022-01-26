package com.mildroid.days.ui.unsplash

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.whenStarted
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mildroid.days.R
import com.mildroid.days.adapter.ListHeaderAdapter
import com.mildroid.days.adapter.PhotoListAdapter
import com.mildroid.days.adapter.SearchHeaderAdapter
import com.mildroid.days.databinding.ActivitySearchUnsplashBinding
import com.mildroid.days.utils.log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class UnsplashPhotoListActivity : AppCompatActivity() {

    init {
        lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.photos.collect {
                        photoAdapter.submitList(it)
                    }
                }
            }
        }
    }

    private val viewModel: UnsplashPhotoListViewHolder by viewModels()

    private val binding: ActivitySearchUnsplashBinding by lazy {
        ActivitySearchUnsplashBinding.inflate(layoutInflater)
    }

    private val photoAdapter by lazy {
        PhotoListAdapter {
            it.log()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.init()
    }

    private fun ActivitySearchUnsplashBinding.init() {
        val headerAdapter = ListHeaderAdapter(getString(R.string.choose_image)) {
            onBackPressed()
        }

        val searchHeader = SearchHeaderAdapter {
            it.log()
        }

        val supAdapter = ConcatAdapter(headerAdapter, searchHeader, photoAdapter)

        val gridLayoutManager = GridLayoutManager(
            this@UnsplashPhotoListActivity,
            3
        ).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (position) {
                        0 -> 3
                        1 -> 3
                        else -> 1
                    }
                }
            }
        }

        searchUnsplashList.apply {
            adapter = supAdapter
            layoutManager = gridLayoutManager
        }
    }
}