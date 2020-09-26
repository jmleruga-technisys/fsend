package com.fif.fpay.android.fsend.conection.interceptor

import com.fif.fpay.android.fsend.conection.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AddCookieInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        SessionManager.instance!!.cookiesString?.let {
            builder.addHeader("Cookie", it)
        }
        return chain.proceed(builder.build())
    }
}