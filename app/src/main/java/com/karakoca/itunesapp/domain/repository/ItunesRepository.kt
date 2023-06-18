package com.karakoca.itunesapp.domain.repository

import com.karakoca.core.model.Resource
import com.karakoca.itunesapp.domain.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface ItunesRepository {
    suspend fun search(term: String): Flow<Resource<List<SearchResult>>>
}