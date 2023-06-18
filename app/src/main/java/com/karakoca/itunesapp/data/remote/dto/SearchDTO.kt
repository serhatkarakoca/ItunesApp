package com.karakoca.itunesapp.data.remote.dto


import com.google.gson.annotations.SerializedName
import com.karakoca.itunesapp.domain.model.SearchResult

data class SearchDTO(
    @SerializedName("resultCount")
    val resultCount: Int?,
    @SerializedName("results")
    val results: List<Result?>?
)

fun SearchDTO.toSearchResult(): List<SearchResult> {
    return results?.map { result ->
        SearchResult(
            trackId = result?.trackId ?: 0,
            artistId = result?.artistId,
            artistName = result?.artistName,
            artworkUrl100 = result?.artworkUrl100,
            collectionName = result?.collectionName,
            currency = result?.currency,
            releaseDate = result?.releaseDate,
            trackName = result?.trackName,
            trackPrice = result?.trackPrice,
            trackTimeMillis = result?.trackTimeMillis
        )
    } ?: emptyList()
}