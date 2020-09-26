package com.fif.fpay.android.fsend.conection

import retrofit2.Call
import retrofit2.http.*

interface BaseRetrofitApi{

    @GET("{path}")
    fun getData(
        @Path("path", encoded = true) path: String,
        @HeaderMap headers: Map<String, String>
    ): Call<Any>

    @POST("{path}")
    @JvmSuppressWildcards
    fun postData(
        @Path("path",encoded = true) path: String,
        @HeaderMap headers: Map<String, String>,
        @Body body :Map<String,Any>
    ): Call<Any>


    @PUT("{path}")
    @JvmSuppressWildcards
    fun putData(
        @Path("path",encoded = true) path: String,
        @HeaderMap headers: Map<String, String>,
        @Body body :Map<String,Any>
    ): Call<Any>


    @PATCH("{path}")
    @JvmSuppressWildcards
    fun patchData(
        @Path("path",encoded = true) path: String,
        @HeaderMap headers: Map<String, String>,
        @Body body :Map<String,Any>
    ): Call<Any>

    @DELETE("{path}")
    @JvmSuppressWildcards
    fun deleteData(
        @Path("path",encoded = true) path: String,
        @HeaderMap headers: Map<String, String>,
        @Body body :Map<String,Any>
    ): Call<Any>

}