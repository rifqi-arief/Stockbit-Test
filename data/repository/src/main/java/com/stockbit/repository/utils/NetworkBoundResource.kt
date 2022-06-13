package com.stockbit.repository.utils

import com.stockbit.model.BaseResponse
import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {

    val data = query().first()

    val flow = if (shouldFetch(data)){
        emit(Resource.loading(data))
        try{
            val response = fetch()
            saveFetchResult(response)
            query().map { Resource.success(response) }
        } catch (throwable : Throwable){
            query().map { Resource.error(throwable,it) }
        }
    } else {
        query().map { Resource.success(it) }
    }
    emitAll(flow)
}