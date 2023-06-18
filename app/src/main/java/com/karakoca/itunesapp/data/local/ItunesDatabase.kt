package com.karakoca.itunesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.karakoca.itunesapp.domain.model.SearchResult

@Database(
    entities = [SearchResult::class],
    version = 1,
    exportSchema = false
)
abstract class ItunesDatabase: RoomDatabase() {
    abstract val dao: ItunesDao
}