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
import com.mildroid.days.ui.event.EventActivity
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
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    entryHandler()
                }
            }

            launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.events.collect {
                        collectEvents(it)
                    }
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
            if (selectedUpcomingEvents.contains(it)) {
                selectedUpcomingEvents.remove(it)

                if (selectedUpcomingEvents.isEmpty())
                    (binding as ActivityUpcomingEventsBinding).upcomingSubmit.text =
                        getString(R.string.skip)

            } else {
                selectedUpcomingEvents.add(it)
                (binding as ActivityUpcomingEventsBinding).run {
                    this.upcomingSubmit.text = getString(R.string.continue_)
                }
            }
        }
    }

    private val selectedUpcomingEvents: MutableList<Event> by lazyOf(mutableListOf())

    private suspend fun entryHandler() {
        viewModel.viewType.collect {
            this.viewType = it

            when (it) {
                EntryTime.FIRST -> {
                    binding = ActivityUpcomingEventsBinding.inflate(layoutInflater)
                    (binding as ActivityUpcomingEventsBinding).init()
                }
                EntryTime.LAST -> {
                    binding = ActivityMainBinding.inflate(layoutInflater)
                    (binding as ActivityMainBinding).init()
                }
                EntryTime.NONE -> {}
            }

            if (it == EntryTime.FIRST || it == EntryTime.LAST)
                setContentView(binding.root)
        }
    }

    private fun collectEvents(events: List<Event>) {
        events.size.log("onFun")
        when (viewType) {
            EntryTime.FIRST -> pagerAdapter.submitList(events)
            EntryTime.LAST -> listAdapter.submitList(events)
            EntryTime.NONE -> {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.areWeReady.value
            }
        }

        super.onCreate(savedInstanceState)
    }

    private fun ActivityMainBinding.init() {
        val headerAdapter = EventListHeaderAdapter {
//            start<AddEventActivity>()
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
            viewModel.saveEvents(selectedUpcomingEvents)

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

}

enum class EntryTime {
    FIRST,
    LAST,
    NONE
}