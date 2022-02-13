package com.mildroid.days.ui.event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.setPadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.whenResumed
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mildroid.days.GlideApp
import com.mildroid.days.R
import com.mildroid.days.databinding.ActivityEventBinding
import com.mildroid.days.ui.main.MainActivity
import com.mildroid.days.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@AndroidEntryPoint
class EventActivity : AppCompatActivity() {

    init {
        lifecycleScope.launch {
            whenResumed {
                showOperationArea()
            }

            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.viewState.collect(::stateHandler)
                }
            }
        }
    }

    private val viewModel: EventViewModel by viewModels()

    private val binding by lazy {
        ActivityEventBinding.inflate(layoutInflater)
    }

    private lateinit var viewType: EventViewType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialLoad()
        binding.init()
    }

    private fun ActivityEventBinding.init() {
        eventBack.setOnClickListener {
            onBackPressed()
        }

        eventMore.setOnClickListener {
            if (viewType == EventViewType.VIEW || viewType == EventViewType.SAVED) {
                EventOptionsBottomSheet()
                    .show(supportFragmentManager, EVENT_OPTIONS_SHEET)
            } else {
                viewModel.onEvent(EventStateEvent.ChangeViewType(EventViewType.SAVED))
                viewModel.onEvent(EventStateEvent.SaveEvent)
            }
        }

//        eventDetailsArea.translator()
    }

    private suspend fun stateHandler(state: EventViewState) {
        when (state) {
            EventViewState.IDLE -> {}
            is EventViewState.ViewType -> {
                viewType = state.viewType
                if (viewType == EventViewType.PREVIEW) {
                    binding.run {
                        eventTitle.text = state.event?.title
                        eventDate.text = state.event?.date?.inDaysRemaining()

                        eventTitle.fade(true, root)
                        eventDate.fade(true, root)
                    }
                }
                handleOperationsArea(state.viewType)
            }
        }
    }

    private fun initialLoad() {
        intent?.extras?.let {
            this.viewType = EventViewType.valueOf(it.getString(EVENT_VIEW_TYPE)!!)

            val image = it.getString(EVENT_IMAGE)
            GlideApp.with(this@EventActivity)
                .load(image)
                .transition(DrawableTransitionOptions.withCrossFade(350))
                .into(binding.eventImage)

            if (viewType == EventViewType.VIEW) {
                binding.eventTitle.text = it.getString(EVENT_TITLE)
                binding.eventDate.text = LocalDate.parse(it.getString(EVENT_DATE)!!).inDaysRemaining()

                val id = it.getInt(EVENT_ID)
                viewModel.onEvent(EventStateEvent.Init(id))
            } else {
                viewModel.onEvent(EventStateEvent.Init(null))
                binding.run {
                    eventTitle.fade(false, root)
                    eventDate.fade(false, root)
                }
            }

            viewModel.onEvent(EventStateEvent.ChangeViewType(viewType))
        }
    }

/*
    private fun View.translator() {
        lifecycleScope.launch {
            launch {
                delay(500)

                ObjectAnimator.ofFloat(
                    this@translator,
                    View.TRANSLATION_Y,
                    -380F
                ).apply {
                    interpolator = FastOutSlowInInterpolator()
                    duration = 1000

                }.start()
            }
        }

    }
*/

    private suspend fun showOperationArea() {
        delay(1000)
        binding.eventOperationArea.fade(true, binding.root)
    }

    private suspend fun handleOperationsArea(type: EventViewType) {
        binding.eventMore.apply {
            when (type) {
                EventViewType.SAVED -> {
                    fade(false, binding.root)
                    setImageResource(R.drawable.ic_more_24dp)
                    setPadding(6)

                    delay(1000)
                    fade(true, binding.root)
                }

                EventViewType.PREVIEW -> {
                    setImageResource(R.drawable.ic_tick_square_white_24dp)
                    setPadding(16)
                }

                EventViewType.VIEW -> {}
            }
        }

        /*binding.eventShare.apply {
            when (type) {
                EventViewType.VIEW -> {
                    fade(true, binding.root)
                }
                EventViewType.PREVIEW -> gone()
            }
        }*/
    }

    override fun onBackPressed() {
        when (viewType) {
            EventViewType.VIEW, EventViewType.PREVIEW -> super.onBackPressed()
            EventViewType.SAVED -> {
                start<MainActivity>()
                finishAffinity()
            }
        }
    }

}

enum class EventViewType {
    VIEW,
    PREVIEW,
    SAVED
}