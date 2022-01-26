package com.mildroid.days.ui.event

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import com.mildroid.days.GlideApp
import com.mildroid.days.databinding.ActivityEventBinding
import com.mildroid.days.utils.daysUntilNow
import com.mildroid.days.utils.fade
import com.mildroid.days.utils.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class EventActivity : AppCompatActivity() {

    init {
        lifecycleScope.launchWhenStarted {
            whenResumed {
                showOperationArea()
            }
        }
    }

    private val binding by lazy {
        ActivityEventBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.init()

        intent?.extras?.let {
            val title = it.getString("event_title")
            val date = LocalDate.parse(it.getString("event_date")!!)
            val image = it.getString("event_image")

            binding.run {
                eventTitle.text = title
                eventDate.text = "${date.daysUntilNow().inWholeDays} Days"

                eventImage.load(image!!) {
//                    supportStartPostponedEnterTransition()
                }

            }
        }
    }

    private fun ImageView.load(url: String, onLoadingFinished: () -> Unit = {}) {
/*
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onLoadingFinished.invoke()

                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onLoadingFinished.invoke()

                return false
            }
        }
*/

//        val requestOptions = RequestOptions.noTransformation()

        GlideApp.with(this)
            .load(url)
//            .apply(requestOptions)
//            .listener(listener)
            .into(this)
    }

    private fun ActivityEventBinding.init() {
        eventBack.setOnClickListener {
            onBackPressed()
        }

        eventMore.setOnClickListener {
            EventOptionsBottomSheet()
                .show(supportFragmentManager, "event_options_bottom_sheet")
        }

        eventDetailsArea.translator()
    }

    private fun View.translator() {
        lifecycleScope.launch {
            launch {
                delay(500)

                ObjectAnimator.ofFloat(
                    this@translator,
                    View.TRANSLATION_Y,
                    -260F
                ).apply {
                    interpolator = FastOutSlowInInterpolator()
                    duration = 1000

                }.start()
            }
        }

    }

    private suspend fun showOperationArea() {
        delay(1000)
        binding.eventOperationArea.fade(true, binding.root)
    }
}