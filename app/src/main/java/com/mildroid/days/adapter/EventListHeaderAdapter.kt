package com.mildroid.days.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mildroid.days.databinding.EventListHeaderBinding

class EventListHeaderAdapter :
    RecyclerView.Adapter<EventListHeaderAdapter.EventListHeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EventListHeaderViewHolder.from(parent)

    override fun onBindViewHolder(holder: EventListHeaderViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = 1

    class EventListHeaderViewHolder(
        private val binding: EventListHeaderBinding

    ) : RecyclerView.ViewHolder(binding.root) {

        internal fun bind() {

        }

        companion object {
            fun from(
                parent: ViewGroup

            ) = EventListHeaderViewHolder(
                EventListHeaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

        }
    }
}