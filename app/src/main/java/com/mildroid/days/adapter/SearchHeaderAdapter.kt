package com.mildroid.days.adapter

import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.mildroid.days.databinding.SearchHeaderBinding

class SearchHeaderAdapter(
    private val onSearchInputChanged: (Editable?) -> Unit

) : RecyclerView.Adapter<SearchHeaderAdapter.SearchHeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SearchHeaderViewHolder.from(parent, onSearchInputChanged)

    override fun onBindViewHolder(holder: SearchHeaderViewHolder, position: Int) {

    }

    override fun getItemCount() = 1

    class SearchHeaderViewHolder(
        private val binding: SearchHeaderBinding,
        onSearchInputChanged: (Editable?) -> Unit

    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.searchHeaderInput.addTextChangedListener {
                onSearchInputChanged.invoke(it)
            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onSearchInputChanged: (Editable?) -> Unit

            ) = SearchHeaderViewHolder(
                SearchHeaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                onSearchInputChanged
            )
        }
    }
}