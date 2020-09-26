package com.fif.fpay.android.fsend.conection

import com.fif.fpay.android.connection.utils.interceptor.PayloadInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


object RestBuilder {
    private var TIMEOUT = 180
    private var adapter: Retrofit? = null
    private var gson: Gson? = null

    private val httpsClientBuilder: OkHttpClient.Builder
        get() {
            var client: OkHttpClient.Builder
            client = try {
                val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(null as KeyStore?)
                val trustManagers = trustManagerFactory.trustManagers
                if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                    throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
                }
                val trustManager = trustManagers[0] as X509TrustManager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
                val sslSocketFactory = sslContext.socketFactory
                OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, trustManager)
            } catch (e: Exception) {
                OkHttpClient().newBuilder()
            }

            return client
        }

    fun <S> createService(serviceClass: Class<S>, baseUrl: String, securitize: Boolean, timeout: Int): S {
        TIMEOUT = timeout
        return createService(
            serviceClass,
            baseUrl,
            securitize,
            true
        )
    }

    fun <S> createService(serviceClass: Class<S>, baseUrl: String, securitize: Boolean, usePayload: Boolean = true): S {
        val builder: OkHttpClient.Builder = if (baseUrl.startsWith("https://")) httpsClientBuilder else OkHttpClient().newBuilder()
        val client: OkHttpClient
        gson = GsonBuilder().setLenient().create()

        builder.readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
        builder.connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)



        builder.addInterceptor { chain ->
            val original: Request = chain.request()
            // Request customization: add request headers
            val headers = HeaderBuilder.build()
            val requestBuilder: Request.Builder = original.newBuilder()
            headers.map {
                requestBuilder.header(it.key, it.value)
            }

            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }

        client = builder.build()
        adapter = Retrofit.Builder()
            .baseUrl(getBaseUrl(baseUrl))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson!!))
            //.addCallAdapterFactory(LiveDataCallAdapterFactory()) // Use if services return LiveData
            .build()
        return adapter!!.create(serviceClass)
    }

    fun <S> parseError(errorClass: Class<S>, response: Response<*>): S? {
        val converter = adapter!!.responseBodyConverter<S>(errorClass, arrayOfNulls(0))

        try {
            return converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            return null
        }

    }

    private fun getBaseUrl(baseUrl: String): HttpUrl {
        if (useAssets(baseUrl)) {
            return HttpUrl.get(baseUrl.replace("assets:", "http:"))
        }
        return HttpUrl.get(baseUrl)
    }

    private fun useAssets(baseUrl: String) =
        baseUrl.regionMatches(0, "assets:", 0, 7, ignoreCase = true)

}