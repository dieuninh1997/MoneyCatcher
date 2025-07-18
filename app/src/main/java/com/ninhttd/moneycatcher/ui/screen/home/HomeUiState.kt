package com.ninhttd.moneycatcher.ui.screen.home

import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class HomeUiState(
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessageIds: List<Int> = persistentListOf()
)

sealed class LoadResult<out T> {
    class Loading<T> : LoadResult<T>()
    data class Success<T>(val data: T) : LoadResult<T>()
    data class Error<T>(val throwable: Throwable) : LoadResult<T>()
}

fun <T> Flow<T>.asLoadResult(): Flow<LoadResult<T>> = flow {
    emit(LoadResult.Loading())
    try {
        collect { value ->
            emit(LoadResult.Success(value))
        }
    } catch (e: Exception) {
        emit(LoadResult.Error(e))
    }
}