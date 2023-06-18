package com.karakoca.itunesapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.karakoca.itunesapp.R
import com.karakoca.itunesapp.databinding.ItemTrackBinding
import com.karakoca.itunesapp.databinding.ItemTrackGridBinding
import com.karakoca.itunesapp.databinding.ItemTrackHorizontalBinding
import com.karakoca.itunesapp.domain.model.SearchResult
import com.karakoca.itunesapp.presentation.viewholder.MusicViewHolder

class MusicAdapter(
    private var items: List<SearchResult>,
    private val itemType: ItemType,
    private val clickListener: ((SearchResult) -> Unit),
    private val deleteListener: ((Int) -> Unit)? = null
) :
    RecyclerView.Adapter<MusicViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (itemType) {
            ItemType.VERTICAL -> R.layout.item_track
            ItemType.GRID -> R.layout.item_track_grid
            ItemType.HORIZONTAL -> R.layout.item_track_horizontal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return when (viewType) {
            R.layout.item_track -> {
                val binding = DataBindingUtil.inflate<ItemTrackBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_track,
                    parent,
                    false
                )
                MusicViewHolder.VerticalViewHolder(binding, clickListener)
            }

            R.layout.item_track_grid -> {
                val binding = DataBindingUtil.inflate<ItemTrackGridBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_track_grid,
                    parent,
                    false
                )
                MusicViewHolder.GridViewHolder(binding, clickListener)
            }

            R.layout.item_track_horizontal -> {
                val binding = DataBindingUtil.inflate<ItemTrackHorizontalBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_track_horizontal,
                    parent,
                    false
                )
                MusicViewHolder.HorizontalViewHolder(binding, clickListener, deleteListener)
            }

            else -> throw IllegalStateException("Invalid ViewType")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        items.getOrNull(position)?.let {
            when (itemType) {
                ItemType.VERTICAL -> (holder as MusicViewHolder.VerticalViewHolder).bind(it)
                ItemType.GRID -> (holder as MusicViewHolder.GridViewHolder).bind(it)
                ItemType.HORIZONTAL -> (holder as MusicViewHolder.HorizontalViewHolder).bind(it)
            }
        }
    }

    fun updateList(newList: List<SearchResult>) {
        val diffCallback = MusicDiffUtil(newList, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newList
        diffResult.dispatchUpdatesTo(this)
    }

    class MusicDiffUtil(
        private val newList: List<SearchResult>,
        private val oldList: List<SearchResult>
    ) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            newList.getOrNull(newItemPosition) == oldList.getOrNull(newItemPosition)

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            newList.getOrNull(newItemPosition)?.trackId == oldList.getOrNull(oldItemPosition)?.trackId
    }

    enum class ItemType {
        VERTICAL, HORIZONTAL, GRID
    }
}