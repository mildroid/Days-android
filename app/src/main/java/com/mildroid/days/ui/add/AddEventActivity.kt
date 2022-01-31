package com.mildroid.days.ui.add

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.whenResumed
import com.mildroid.days.databinding.ActivityAddEventBinding
import com.mildroid.days.ui.unsplash.UnsplashPhotoListActivity
import com.mildroid.days.utils.ADD_EVENT_INITIAL_DATE
import com.mildroid.days.utils.daysUntilNow
import com.mildroid.days.utils.log
import com.mildroid.days.utils.start
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.MonthDay
import java.time.Year

class AddEventActivity : AppCompatActivity() {

    private val viewModel: AddEventViewModel by viewModels()

    private val binding: ActivityAddEventBinding by lazy {
        ActivityAddEventBinding.inflate(layoutInflater)
    }

    private var desireDate = java.time.LocalDate.now().toKotlinLocalDate()

    init {
        lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.desireDate.collect {
                        desireDate = it
                        val days = it.daysUntilNow().inWholeDays
                        if (days != 0L) {
                            binding.addEventDate.text = "in $days Days"
                        } else
                            binding.addEventDate.text = ""

                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.init()
        if (savedInstanceState != null)
            binding.initDatePicker(
                LocalDate.parse(
                    savedInstanceState.getString(
                        ADD_EVENT_INITIAL_DATE
                    )!!
                )
            )
        else
            binding.initDatePicker()
    }

    private fun ActivityAddEventBinding.init() {
        initDatePicker()

        addEventNext.setOnClickListener {
            start<UnsplashPhotoListActivity>()
        }

        addEventBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun ActivityAddEventBinding.initDatePicker(
        date: LocalDate = java.time.LocalDate.now().toKotlinLocalDate()
    ) {
        addEventDatePicker.init(
            date.year,
            date.monthNumber - 1,
            date.dayOfMonth
        ) { _, year, monthOfYear, dayOfMonth ->
            val desire = LocalDate(year, monthOfYear + 1, dayOfMonth)
            viewModel.onDatePickerSpanned(desire)
        }

        addEventDatePicker.minDate = System.currentTimeMillis() - 1000
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(ADD_EVENT_INITIAL_DATE, desireDate.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()

        binding.addEventTitle.post {
            binding.addEventTitle.requestFocus()
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.addEventTitle, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}