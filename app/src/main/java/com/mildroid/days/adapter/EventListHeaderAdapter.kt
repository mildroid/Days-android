package com.mildroid.days.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mildroid.days.databinding.EventListHeaderBinding

class EventListHeaderAdapter(
    private val onAddClick: () -> Unit
) :
    RecyclerView.Adapter<EventListHeaderAdapter.EventListHeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EventListHeaderViewHolder.from(parent, onAddClick)

    override fun onBindViewHolder(holder: EventListHeaderViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = 1

    class EventListHeaderViewHolder(
        private val binding: EventListHeaderBinding,
        private val onAddClick: () -> Unit

    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.eventHeaderAdd.setOnClickListener {
                onAddClick.invoke()
            }
        }

        internal fun bind() {

        }

        companion object {
            fun from(
                parent: ViewGroup,
                onAddClick: () -> Unit

            ) = EventListHeaderViewHolder(
                EventListHeaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), onAddClick
            )

        }
    }
}