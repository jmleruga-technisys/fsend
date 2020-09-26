package com.fif.fpay.android.fsend.service

import com.fif.fpay.android.fsend.data.ClientInfo
import com.fif.fpay.android.fsend.data.Shipment
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

 interface IApiInterface {
    @GET("/api/unknown")
    fun doGetShipmentInfo(): Call<Shipment?>?

//    @FormUrlEncoded
//    @POST("/api/users?")
//    fun doCreateUserWithField(
//        @Field("name") name: String?,
//        @Field("job") job: String?
//    ): Call<UserList?>?
}