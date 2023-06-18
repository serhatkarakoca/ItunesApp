package com.karakoca.itunesapp.presentation.home

import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.karakoca.core.viewmodel.BaseViewModel
import com.karakoca.itunesapp.data.local.ItunesDatabase
import com.karakoca.itunesapp.data.remote.ApiService
import com.karakoca.itunesapp.data.remote.ItunesRemoteMediator
import com.karakoca.itunesapp.domain.model.SearchResult
import com.karakoca.itunesapp.domain.use_case.search.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val iTunesDb: ItunesDatabase,
    private val apiService: ApiService
) : BaseViewModel() {

    var searchTerm = "rihanna"

    @OptIn(ExperimentalPagingApi::class)
    fun getHome(): Flow<PagingData<SearchResult>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = ItunesRemoteMediator(
                searchTerm = searchTerm,
                itunesDb = iTunesDb,
                apiService = apiService
            ),
            initialKey = null,
            pagingSourceFactory = {
                iTunesDb.dao.pagingSource()
            }
        ).flow.cachedIn(viewModelScope)
    }
}