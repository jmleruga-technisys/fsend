package com.fif.fpay.android.connection.utils.interceptor

import okhttp3.*

class PayloadInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var newRequest = chain.request()
        if (newRequest.body() != null) {

            val method = newRequest.method()
            val contentType = newRequest.header("Content-Type") ?: "application/json"
            val buffer = okio.Buffer()
            newRequest.body()?.writeTo(buffer)
            val source = "{ \"payload\": ${buffer.readUtf8()}}"

            newRequest = newRequest.newBuilder()
                .method(method, RequestBody.create(MediaType.parse(contentType), source ))
                .build()

        }

        val response = chain.proceed(newRequest)

        return response
    }


}