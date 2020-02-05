package net.simplifiedcoding.data.network

import net.simplifiedcoding.exceptions.ApiException
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()?.string().toString()
            throw ApiException(error)
        }
    }
}

