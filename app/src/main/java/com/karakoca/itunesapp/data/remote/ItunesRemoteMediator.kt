package com.karakoca.itunesapp.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.karakoca.itunesapp.data.local.ItunesDatabase
import com.karakoca.itunesapp.data.remote.dto.toSearchResult
import com.karakoca.itunesapp.domain.model.SearchResult
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ItunesRemoteMediator(
    private val searchTerm: String,
    private val itunesDb: ItunesDatabase,
    private val apiService: ApiService
): RemoteMediator<Int, SearchResult>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SearchResult>
    ): MediatorResult {
        return try {

            val loadKey = when(loadType) {
                LoadType.REFRESH -> { 1 }
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = itunesDb.dao.getAllData().size
                    if (lastItem == 0)
                            1
                    else (lastItem / state.config.pageSize) + 1
                }
            }

            val musics = apiService.search(
                term = searchTerm,
                offset = loadKey.toInt(),
                limit = state.config.pageSize
            )

            itunesDb.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    itunesDb.dao.clearAll()
                }
                val musicEntities = musics.body()?.toSearchResult()
                itunesDb.dao.insertAll(musicEntities ?: emptyList())
            }

            MediatorResult.Success(
                endOfPaginationReached = (musics.body()?.resultCount ?: 0) < state.config.pageSize
            )
        } catch(e: IOException) {
            MediatorResult.Error(e)
        } catch(e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}