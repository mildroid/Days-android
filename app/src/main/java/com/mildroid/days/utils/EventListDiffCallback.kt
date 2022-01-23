package com.mildroid.days.utils

import androidx.recyclerview.widget.DiffUtil
import com.mildroid.days.domain.Event

class EventListDiffCallback() : DiffUtil.ItemCallback<Event>() {

    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}
