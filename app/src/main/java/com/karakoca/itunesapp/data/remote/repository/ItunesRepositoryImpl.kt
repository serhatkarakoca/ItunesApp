package com.karakoca.itunesapp.data.remote.repository

import com.karakoca.core.model.Resource
import com.karakoca.itunesapp.data.remote.ApiService
import com.karakoca.itunesapp.data.remote.dto.toSearchResult
import com.karakoca.itunesapp.domain.model.SearchResult
import com.karakoca.itunesapp.domain.repository.ItunesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOError
import javax.inject.Inject

class ItunesRepositoryImpl @Inject constructor(private val mApi: ApiService) : ItunesRepository {
    override suspend fun search(term: String): Flow<Resource<List<SearchResult>>> = flow {
        emit(Resource.Loading)
        try {
            val request = mApi.search(term)
            val response = request.body()
            if (request.isSuccessful && response != null) {
                emit(Resource.Success(response.toSearchResult()))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e))
        } catch (e: IOError) {
            emit(Resource.Error(e))
        }
    }
}