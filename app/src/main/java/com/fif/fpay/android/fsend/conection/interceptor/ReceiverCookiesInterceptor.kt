package com.fif.fpay.android.fsend.conection.interceptor

import com.fif.fpay.android.fsend.conection.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class ReceiverCookiesInterceptor : Interceptor {

    companion object {
        private const val SET_COOKIE = "Set-Cookie"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        val headers = originalResponse.headers(SET_COOKIE)
        if (headers.isNotEmpty()) SessionManager.instance!!.cookies[headers[0]
                ?.split(";")?.get(0)?.split("=")?.get(0)] = headers[0]
        return originalResponse
    }
}