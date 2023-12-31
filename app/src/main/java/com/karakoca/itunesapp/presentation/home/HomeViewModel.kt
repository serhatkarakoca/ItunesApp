package com.karakoca.itunesapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.karakoca.core.viewmodel.BaseViewModel
import com.karakoca.itunesapp.data.local.ItunesDatabase
import com.karakoca.itunesapp.data.remote.ApiService
import com.karakoca.itunesapp.data.remote.ItunesRemoteMediator
import com.karakoca.itunesapp.domain.model.SearchResult
import com.karakoca.itunesapp.domain.use_case.search.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val iTunesDb: ItunesDatabase,
    private val apiService: ApiService
) : BaseViewModel() {

    var searchTerm = "jack johnson"

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> get() = _state

    @OptIn(ExperimentalPagingApi::class)
    fun getHome(): Flow<PagingData<SearchResult>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, prefetchDistance = 2),
            remoteMediator = ItunesRemoteMediator(
                searchTerm = searchTerm ?: "",
                itunesDb = iTunesDb,
                apiService = apiService
            ),
            initialKey = null,
            pagingSourceFactory = {
                iTunesDb.dao.pagingSource()
            }
        ).flow
    }

    fun clearItems() {
        viewModelScope.launch {
            iTunesDb.withTransaction {
                iTunesDb.dao.clearAll()
            }
        }
    }

    fun getLocalDataSize(): Int {
        return iTunesDb.dao.getAllData().size
    }

    fun searchMusic(term: String) {
        searchTerm = term
        _state.value = State.OnQueryChanged(term)
    }

    fun clearState() {
        _state.value = State.Empty
    }

    sealed class State {
        data class OnQueryChanged(val term: String) : State()
        object Empty : State()
    }
}