package com.mildroid.days.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mildroid.days.GlideApp
import com.mildroid.days.databinding.PhotoListItemBinding
import com.mildroid.days.domain.Photo
import com.mildroid.days.utils.PhotoListDiffCallback
import com.mildroid.days.utils.log

class PhotoListAdapter(
    private val onPhotoClick: (Photo) -> Unit

) : ListAdapter<Photo, PhotoListAdapter.PhotoListViewHolder>(PhotoListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PhotoListViewHolder.from(parent, onPhotoClick)

    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<Photo>?) {
        "New List Submitted".log(list?.size)
        list?.let {
            val newList = mutableListOf<Photo>()
            newList.addAll(currentList)
            newList.addAll(list)

            super.submitList(newList)
        } ?: kotlin.run {
            super.submitList(list)
        }
    }

    class PhotoListViewHolder(
        private val binding: PhotoListItemBinding,
        onPhotoClick: (Photo) -> Unit

    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var photo: Photo

        init {
            itemView.setOnClickListener {
                onPhotoClick.invoke(photo)
            }
        }

        internal fun bind(photo: Photo) {
            this.photo = photo

            GlideApp
                .with(binding.root)
                .load(photo.urls.small)
                .into(binding.photoListItemPhoto)
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onPhotoClick: (Photo) -> Unit

            ) = PhotoListViewHolder(
                PhotoListItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                onPhotoClick
            )
        }
    }

}