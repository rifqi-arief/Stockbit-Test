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
//            if (response.message?.uppercase().equals("success".uppercase())) {
//                response.data?.let { saveFetchResult(it) }
//                query().map { Resource.success(response) }
//            }else {
//                query().map { response.message?.let { it1 -> Resource.error(Throwable(it1),it) } }
//            }
        } catch (throwable : Throwable){
            query().map { Resource.error(throwable,it) }
        }
    } else {
        query().map { Resource.success(it) }
    }

    emitAll(flow)
}