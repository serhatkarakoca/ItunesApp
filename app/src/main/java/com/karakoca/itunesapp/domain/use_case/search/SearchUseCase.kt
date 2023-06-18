package com.karakoca.itunesapp.domain.use_case.search

import com.karakoca.itunesapp.data.remote.repository.ItunesRepositoryImpl
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val itunesRepositoryImpl: ItunesRepositoryImpl){
    suspend operator fun invoke(term: String) = itunesRepositoryImpl.search(term = term)
}