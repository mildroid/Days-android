package com.mildroid.days.ui.unsplash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mildroid.days.R
import com.mildroid.days.adapter.ListHeaderAdapter
import com.mildroid.days.adapter.PhotoListAdapter
import com.mildroid.days.adapter.SearchHeaderAdapter
import com.mildroid.days.databinding.ActivitySearchUnsplashBinding
import com.mildroid.days.domain.state.UnsplashPhotoListStateEvent
import com.mildroid.days.domain.state.UnsplashPhotoListViewState
import com.mildroid.days.utils.EndlessRecyclerViewScrollListener
import com.mildroid.days.utils.fade
import com.mildroid.days.utils.log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class UnsplashPhotoListActivity : AppCompatActivity() {

    init {
        lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.viewState.collect(::stateHandler)
                }
            }
        }
    }

    private val viewModel: UnsplashPhotoListViewModel by viewModels()

    private val gridLayoutManager by lazy {
        GridLayoutManager(
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
    }

    private val binding: ActivitySearchUnsplashBinding by lazy {
        ActivitySearchUnsplashBinding.inflate(layoutInflater)
    }

    private val photoAdapter by lazy {
        PhotoListAdapter {
            it.log()
        }
    }

    private val scrollListener: EndlessRecyclerViewScrollListener by lazy {
        object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(
                page: Int,
                totalItemsCount: Int,
                view: RecyclerView?
            ) {
                this@UnsplashPhotoListActivity.page = page
                page.log("page Increased")
                viewModel.onEvent(
                    UnsplashPhotoListStateEvent.Fetch(
                        this@UnsplashPhotoListActivity.page,
                        this@UnsplashPhotoListActivity.query
                    )
                )
            }
        }
    }

    private var page: Int = 1
    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.init()
        viewModel.onEvent(UnsplashPhotoListStateEvent.Fetch(page = this.page))
    }

    private fun ActivitySearchUnsplashBinding.init() {
        unsplashPhotoList.apply {
            adapter = providerAdapter()
            layoutManager = gridLayoutManager
            addOnScrollListener(scrollListener)
        }
    }

    private fun providerAdapter(): ConcatAdapter {
        val headerAdapter = ListHeaderAdapter(getString(R.string.choose_image)) {
            onBackPressed()
        }

        val searchHeader = SearchHeaderAdapter {
            lifecycleScope.launchWhenResumed {
                delay(1000)

                this@UnsplashPhotoListActivity.query = it.toString()
                viewModel.onEvent(UnsplashPhotoListStateEvent.Fetch(page, it.toString()))
            }
        }

        return ConcatAdapter(headerAdapter, searchHeader, photoAdapter)
    }

    private fun stateHandler(state: UnsplashPhotoListViewState) {
        when (state) {
            is UnsplashPhotoListViewState.Data -> {
                loading(false)
                photoAdapter.submitList(state.photos)
            }
            is UnsplashPhotoListViewState.Error -> {
                loading(false)
            }
            UnsplashPhotoListViewState.Loading -> loading(true)
            UnsplashPhotoListViewState.Reset -> {
                photoAdapter.submitList(null)
                scrollListener.resetState()
                this@UnsplashPhotoListActivity.page = 1
            }
        }
    }

    private fun loading(really: Boolean) {
        binding.unsplashPhotoListProgressbar.fade(really, binding.root)
    }

}