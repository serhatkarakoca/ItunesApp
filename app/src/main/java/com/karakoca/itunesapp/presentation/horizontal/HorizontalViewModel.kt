package com.karakoca.itunesapp.presentation.horizontal

import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.karakoca.core.viewmodel.BaseViewModel
import com.karakoca.itunesapp.data.local.ItunesDatabase
import com.karakoca.itunesapp.domain.model.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HorizontalViewModel @Inject constructor(private val iTunesDb: ItunesDatabase): BaseViewModel() {

    fun getMusics(): Flow<List<SearchResult>> {
        return iTunesDb.dao.getAllDataFlow()
    }

    fun deleteMusicItem(trackId: Int) {
        viewModelScope.launch {
            iTunesDb.withTransaction {
                iTunesDb.dao.deleteItem(trackId)
            }
        }
    }
}