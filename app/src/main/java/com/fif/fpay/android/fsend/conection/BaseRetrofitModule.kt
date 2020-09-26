package com.fif.fpay.android.fsend.conection

import com.fif.tech.android.commons.android_connection.utils.converter.XmlOrJsonConverterFactory
import com.fif.fpay.android.fsend.conection.interceptor.AddCookieInterceptor
import com.fif.fpay.android.fsend.conection.interceptor.ReceiverCookiesInterceptor
import dagger.Module
import okhttp3.Interceptor
import retrofit2.Converter

/**
 * @author lcunarr on 2019.08.22
 */
@Suppress("unused")
@Module
abstract class BaseRetrofitModule(
        val serviceUrl: String,
        val isDebug: Boolean,
        val connectTimeOut: Long = 120L,
        val readTimeOut: Long = 120L,
        val writeTimeOut: Long = 120L

) {
    @Suppress("MemberVisibilityCanBePrivate")
    protected val converterFactories: MutableList<Converter.Factory> = mutableListOf(
            XmlOrJsonConverterFactory.create())

    @Suppress("MemberVisibilityCanBePrivate")
    protected val interceptors: MutableList<Interceptor> = mutableListOf(
        AddCookieInterceptor(),
        ReceiverCookiesInterceptor()
    )
}