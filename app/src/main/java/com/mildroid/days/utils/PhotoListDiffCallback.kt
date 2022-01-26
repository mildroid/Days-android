package com.mildroid.days.utils

import androidx.recyclerview.widget.DiffUtil
import com.mildroid.days.domain.Photo

class PhotoListDiffCallback() : DiffUtil.ItemCallback<Photo>() {

    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }
}
