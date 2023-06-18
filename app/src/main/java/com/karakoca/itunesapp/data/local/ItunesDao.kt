package com.karakoca.itunesapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.karakoca.itunesapp.domain.model.SearchResult
import kotlinx.coroutines.flow.Flow

@Dao
interface ItunesDao {

    @Upsert
    suspend fun insertAll(data: List<SearchResult>)

    @Query("SELECT * FROM searchEntity")
    fun pagingSource(): PagingSource<Int, SearchResult>

    @Query("SELECT * FROM searchEntity")
    fun getAllData(): List<SearchResult>

    @Query("SELECT * FROM searchEntity")
    fun getAllDataFlow(): Flow<List<SearchResult>>

    @Query("DELETE FROM searchEntity")
    suspend fun clearAll()

    @Query("DELETE FROM searchEntity WHERE id = :id")
    suspend fun deleteItem(id: Int)

}