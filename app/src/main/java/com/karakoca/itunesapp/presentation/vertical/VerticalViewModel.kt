package com.karakoca.itunesapp.presentation.vertical

import com.karakoca.core.viewmodel.BaseViewModel
import com.karakoca.itunesapp.data.local.ItunesDatabase
import com.karakoca.itunesapp.domain.model.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class VerticalViewModel @Inject constructor(private val iTunesDb: ItunesDatabase): BaseViewModel() {

    fun getMusics(): Flow<List<SearchResult>> {
        return iTunesDb.dao.getAllDataFlow()
    }
}