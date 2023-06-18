package com.karakoca.core.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karakoca.core.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseViewModel(
    val baseLiveData: MutableLiveData<State> = MutableLiveData()
): ViewModel() {

    inline fun <T> Flow<Resource<T>>.execute(
        requestType: RequestType = RequestType.PROGRESS,
        crossinline onComplete: (T) -> Unit,
        crossinline onError: (Throwable?) -> Unit,
    ) {
        onEach { response ->
            when (response) {
                is Resource.Loading -> baseLiveData.postValue(State.ShowLoading(requestType))
                is Resource.Error -> {
                    onError(response.error)
                    baseLiveData.postValue(State.ShowContent(requestType))
                // TODO error tiplerine göre otomatik mesajlar ayarlanabilir
                    // State.ShowError(response.error)
                }

                is Resource.Success -> {
                    response.data?.let { onComplete(it) }
                    baseLiveData.postValue(State.ShowContent(requestType))
                }
            }
        }.launchIn(viewModelScope)
    }

    sealed class State {
        data class ShowContent(val requestType: RequestType? = RequestType.PROGRESS): State()
        data class ShowError(val throwable: Throwable?): State()
        data class ShowLoading(val requestType: RequestType = RequestType.PROGRESS): State()
        object Empty: State()
    }

    enum class RequestType {
        PROGRESS,
        CUSTOM
    }
}