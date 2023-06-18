package com.karakoca.itunesapp.data.remote

import com.karakoca.itunesapp.data.remote.dto.SearchDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search")
    suspend fun search(@Query("term") term: String, @Query("limit") limit: Int=20, @Query("offset") offset: Int=0): Response<SearchDTO>

}