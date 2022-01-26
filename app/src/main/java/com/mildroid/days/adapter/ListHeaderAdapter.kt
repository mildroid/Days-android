package com.mildroid.days.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mildroid.days.databinding.ListHeaderBinding

class ListHeaderAdapter(
    private val title: String,
    private val onHeaderClickListener: () -> Unit

) : RecyclerView.Adapter<ListHeaderAdapter.ListHeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListHeaderViewHolder.from(parent, onHeaderClickListener)

    override fun onBindViewHolder(holder: ListHeaderViewHolder, position: Int) {
        holder.bind(title)
    }

    override fun getItemCount() = 1

    class ListHeaderViewHolder(
        private val binding: ListHeaderBinding,
        private val onHeaderClickListener: () -> Unit

    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.listHeaderBack.setOnClickListener {
                onHeaderClickListener.invoke()
            }
        }

        internal fun bind(title: String) {
            binding.listHeader.text = title
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onHeaderClickListener: () -> Unit

            )  = ListHeaderViewHolder(
                ListHeaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                onHeaderClickListener
            )
        }
    }
}