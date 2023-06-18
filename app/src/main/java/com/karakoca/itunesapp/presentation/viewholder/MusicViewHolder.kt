package com.karakoca.itunesapp.presentation.viewholder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.karakoca.itunesapp.databinding.ItemTrackBinding
import com.karakoca.itunesapp.databinding.ItemTrackGridBinding
import com.karakoca.itunesapp.databinding.ItemTrackHorizontalBinding
import com.karakoca.itunesapp.domain.model.SearchResult

sealed class MusicViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class VerticalViewHolder(
        val binding: ItemTrackBinding,
        val clickListener: ((SearchResult) -> Unit)
    ) : MusicViewHolder(binding) {
        fun bind(item: SearchResult) {
            binding.item = item
            binding.imgMusic.isVisible = false
            binding.root.setOnClickListener { clickListener(item) }
        }
    }

    class GridViewHolder(
        val binding: ItemTrackGridBinding,
        val clickListener: ((SearchResult) -> Unit)
    ) : MusicViewHolder(binding) {
        fun bind(item: SearchResult) {
            binding.item = item
            binding.root.setOnClickListener { clickListener(item) }
        }
    }

    class HorizontalViewHolder(
        val binding: ItemTrackHorizontalBinding,
        val clickListener: ((SearchResult) -> Unit),
        val deleteListener: ((Int) -> Unit)? = null
    ) : MusicViewHolder(binding) {
        fun bind(item: SearchResult) {
            binding.item = item
            binding.imgDelete.setOnClickListener {
                item.id.let { id -> deleteListener?.invoke(id) }
            }
            binding.root.setOnClickListener { clickListener(item) }
        }
    }
}