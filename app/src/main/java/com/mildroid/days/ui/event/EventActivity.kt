package com.mildroid.days.ui.event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.setPadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.whenResumed
import com.mildroid.days.GlideApp
import com.mildroid.days.R
import com.mildroid.days.databinding.ActivityEventBinding
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
            if (viewType == EventViewType.VIEW) {
                EventOptionsBottomSheet()
                    .show(supportFragmentManager, EVENT_OPTIONS_SHEET)
            } else {
                viewModel.onEvent(EventStateEvent.ViewType(EventViewType.VIEW))
                viewModel.onEvent(EventStateEvent.SaveEvent)
            }
        }

//        eventDetailsArea.translator()
    }

    private fun stateHandler(state: EventViewState) {
        when (state) {
            EventViewState.IDLE -> {}
            is EventViewState.EventDetails -> {
                binding.run {
                    eventTitle.text = state.title
                    eventDate.text = LocalDate.parse(state.date).toReadableText()

                    eventTitle.fade(true, root)
                    eventDate.fade(true, root)
                }
            }
            is EventViewState.ViewTypeChange -> {
                viewType = state.viewType
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
                .into(binding.eventImage)

            if (viewType == EventViewType.VIEW) {
                binding.eventTitle.text = it.getString(EVENT_TITLE)
                binding.eventDate.text = LocalDate.parse(it.getString(EVENT_DATE)!!).toReadableText()

                val id = it.getInt(EVENT_ID)
                viewModel.onEvent(EventStateEvent.Initial(id))
            } else {
                viewModel.onEvent(EventStateEvent.Initial(null))
                binding.run {
                    eventTitle.fade(false, root)
                    eventDate.fade(false, root)
                }
            }

            viewModel.onEvent(EventStateEvent.ViewType(viewType))
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

    private fun handleOperationsArea(type: EventViewType) {
        binding.eventMore.apply {
            when (type) {
                EventViewType.VIEW -> {
                    gone()
                    setImageResource(R.drawable.ic_more_24dp)
                    setPadding(6)
                    fade(true, binding.root)
                }
                EventViewType.PREVIEW -> {
                    setImageResource(R.drawable.ic_tick_square_white_24dp)
                    setPadding(16)
                }
            }
        }

        binding.eventShare.apply {
            when (type) {
                EventViewType.VIEW -> {
                    fade(true, binding.root)
                }
                EventViewType.PREVIEW -> gone()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        /*when (viewType) {
            EventViewType.VIEW -> super.onBackPressed()
            EventViewType.PREVIEW -> TODO()
        }*/
    }
}

enum class EventViewType {
    VIEW,
    PREVIEW
}