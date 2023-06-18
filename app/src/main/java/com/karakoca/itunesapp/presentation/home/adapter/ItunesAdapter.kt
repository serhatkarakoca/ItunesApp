package com.karakoca.itunesapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.karakoca.itunesapp.R
import com.karakoca.itunesapp.databinding.ItemTrackBinding
import com.karakoca.itunesapp.domain.model.SearchResult

class ItunesAdapter(private val clickListener: ((SearchResult) -> Unit)) :
    PagingDataAdapter<SearchResult, ItunesAdapter.ItunesViewHolder>(DIFF_UTIL) {


    class ItunesViewHolder(
        private val binding: ItemTrackBinding,
        private val clickListener: ((SearchResult) -> Unit)
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchResult) {
            binding.item = item
            binding.root.setOnClickListener {
                clickListener.invoke(item)
            }
        }
    }

    override fun onBindViewHolder(holder: ItunesViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItunesViewHolder {
        val binding = DataBindingUtil.inflate<ItemTrackBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_track,
            parent,
            false
        )
        return ItunesViewHolder(binding, clickListener)
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<SearchResult>() {
            override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
                return oldItem.trackId == newItem.trackId
            }

        }
    }
}