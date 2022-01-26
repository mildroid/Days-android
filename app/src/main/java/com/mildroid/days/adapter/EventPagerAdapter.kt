package com.mildroid.days.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.mildroid.days.GlideApp
import com.mildroid.days.databinding.EventPagerItemBinding
import com.mildroid.days.domain.Event
import com.mildroid.days.domain.Photo
import com.mildroid.days.utils.EventListDiffCallback
import com.mildroid.days.utils.daysUntilNow

class EventPagerAdapter(
    private val onItemClicked: (Event) -> Unit

) : ListAdapter<Event, EventPagerAdapter.UpcomingEventPagerViewHolder>(EventListDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int

    ) = UpcomingEventPagerViewHolder.from(parent, onItemClicked)

    override fun onBindViewHolder(holder: UpcomingEventPagerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UpcomingEventPagerViewHolder(
        private val binding: EventPagerItemBinding,
        private val onItemClicked: (Event) -> Unit

    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var event: Event
        private var isCheckAnimated = false

        init {
            itemView.setOnClickListener {
                onItemClicked.invoke(event)

                binding.eventPagerItemCheck.run {
                    if (isCheckAnimated) reverseAnimationSpeed()

                    playAnimation()
                    isCheckAnimated = true
                }
            }
        }

        internal fun bind(event: Event) {
            this.event = event

            binding.run {
                eventPagerItemTitle.text = event.title
                eventPagerItemDate.text = "in ${event.date.daysUntilNow().inWholeDays} Days"

//                eventPagerItemImage.background = ColorDrawable(Color.parseColor(event.photo?.color))
                eventPagerItemImage.loadImage(event.image, event.photo)
            }
        }

        private fun ImageView.loadImage(image: String?, photo: Photo?) {
            var imageUrl = image
            photo?.let {
                imageUrl = it.urls.regular
            }

            GlideApp
                .with(this)
                .applyDefaultRequestOptions(
                    RequestOptions().transform(CenterCrop(), RoundedCorners(24))
                )
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade(200))
                .into(this)
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onItemClicked: (Event) -> Unit
            ) = UpcomingEventPagerViewHolder(
                EventPagerItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                onItemClicked
            )
        }
    }
}