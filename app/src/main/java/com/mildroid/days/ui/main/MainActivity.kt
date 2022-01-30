package com.mildroid.days.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.mildroid.days.R
import com.mildroid.days.adapter.EventListAdapter
import com.mildroid.days.adapter.EventListHeaderAdapter
import com.mildroid.days.adapter.EventPagerAdapter
import com.mildroid.days.databinding.ActivityMainBinding
import com.mildroid.days.databinding.ActivityUpcomingEventsBinding
import com.mildroid.days.domain.Event
import com.mildroid.days.domain.state.EntryTime
import com.mildroid.days.domain.state.MainStateEvent
import com.mildroid.days.domain.state.MainViewState
import com.mildroid.days.ui.add.AddEventActivity
import com.mildroid.days.ui.event.EventActivity
import com.mildroid.days.utils.MAIN_VIEW_TYPE
import com.mildroid.days.utils.log
import com.mildroid.days.utils.start
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    init {
        lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.viewState.collect(::stateHandler)
                }
            }
        }
    }

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ViewBinding
    private lateinit var viewType: EntryTime

    private val listAdapter by lazy {
        EventListAdapter { event ->
            event.id.log("id")
            start<EventActivity> {
                putExtra("event_image", event.image)
                putExtra("event_title", event.title)
                putExtra("event_date", event.date.toString())
            }
        }
    }

    private val pagerAdapter by lazy {
        EventPagerAdapter {
            (binding as ActivityUpcomingEventsBinding).upcomingSubmit
                .text = getString(R.string.continue_)

            viewModel.onEvent(MainStateEvent.SelectedEvent(it))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.areWeReady.value
            }
        }

        super.onCreate(savedInstanceState)
        handleRotation(savedInstanceState)

        viewModel.onEvent(MainStateEvent.Init)
    }

    private fun ActivityMainBinding.init() {
        val headerAdapter = EventListHeaderAdapter {
            start<AddEventActivity>()
        }
        val supAdapter = ConcatAdapter(headerAdapter, listAdapter)

        mainList.apply {
            adapter = supAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

    }

    private fun ActivityUpcomingEventsBinding.init() {
        initPager()

        upcomingSubmit.setOnClickListener {
            viewModel.onEvent(MainStateEvent.SaveEvents)

            start<MainActivity>()
            finish()
        }
    }

    private fun ActivityUpcomingEventsBinding.initPager() {
        upcomingPager.apply {
            this.adapter = pagerAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3

            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        upcomingPager.setPageTransformer(
            CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(40))
                addTransformer { view: View, fl: Float ->
                    val r = 1 - abs(fl)
                    view.scaleY = 0.95f + r * 0.05f
                }
            })
    }

    private fun stateHandler(state: MainViewState) {
        state.log()
        when (state) {
            is MainViewState.Data -> collectEvents(state.events)
            MainViewState.EmptyData -> emptyDateHandler()
            MainViewState.Loading -> {}
            is MainViewState.ViewType -> entryHandler(state.entryTime)
        }
    }

    private fun emptyDateHandler() {
        when (viewType) {
            EntryTime.FIRST -> (binding as ActivityUpcomingEventsBinding).upcomingSubmit
                .text = getString(R.string.skip)

            EntryTime.LAST -> TODO()
        }
    }

    private fun collectEvents(events: List<Event>) {
        when (viewType) {
            EntryTime.FIRST -> pagerAdapter.submitList(events)
            EntryTime.LAST -> listAdapter.submitList(events)
        }
    }

    private fun entryHandler(type: EntryTime) {
        this.viewType = type

        when (type) {
            EntryTime.FIRST -> {
                binding = ActivityUpcomingEventsBinding.inflate(layoutInflater)
                (binding as ActivityUpcomingEventsBinding).init()
            }
            EntryTime.LAST -> {
                binding = ActivityMainBinding.inflate(layoutInflater)
                (binding as ActivityMainBinding).init()
            }
        }

        setContentView(binding.root)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(MAIN_VIEW_TYPE, viewType.toString())
        super.onSaveInstanceState(outState)
    }

    private fun handleRotation(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            viewType = EntryTime.valueOf(it.getString(MAIN_VIEW_TYPE)!!)
            entryHandler(viewType)
        }
    }
}