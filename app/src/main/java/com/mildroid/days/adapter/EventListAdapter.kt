package com.mildroid.days.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mildroid.days.R
import com.mildroid.days.databinding.EventListItemBinding
import com.mildroid.days.domain.Event
import com.mildroid.days.utils.EventListDiffCallback
import com.mildroid.days.utils.daysUntilNow

class EventListAdapter(
    private val onItemClicked: (Event) -> Unit

) : ListAdapter<Event, EventListAdapter.EventListViewHolder>(EventListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EventListViewHolder.from(
            parent,
            onItemClicked
        )


    override fun onBindViewHolder(holder: EventListViewHolder, position: Int) {
        holder.bind(
            getItem(position)
        )
    }

    class EventListViewHolder(
        private val binding: EventListItemBinding,
        private val onItemClicked: (Event) -> Unit,
        private val imageHeight: Int

    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var event: Event

        init {
            itemView.setOnClickListener {
                onItemClicked.invoke(event)
            }
        }

        internal fun bind(event: Event) {
            this.event = event

            binding.run {
                eventListItemTitle.text = event.title
                eventListItemDate.text = "in ${event.date.daysUntilNow().inWholeDays} Days"

                eventListItemImage.addView(
                    eventImage(itemView.context, event.image!!, imageHeight)
                )
            }
        }

        private fun eventImage(
            context: Context,
            image: String,
            height: Int
        ) = ImageView(context).apply {
            Glide
                .with(this)
                .applyDefaultRequestOptions(
                    RequestOptions().transform(CenterCrop(), RoundedCorners(16))
                )
                .load(image)
                .into(this)

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                height
            )

            transitionName = "event_image"
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onItemClicked: (Event) -> Unit
            ) = EventListViewHolder(
                EventListItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                onItemClicked,
                parent.measuredWidth
            )
        }
    }
}