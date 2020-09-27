package com.fif.fpay.android.fsend.service

import com.fif.fpay.android.fsend.conection.BaseResponse
import com.fif.fpay.android.fsend.conection.UpdateStateRequest
import com.fif.fpay.android.fsend.data.ClientInfo
import com.fif.fpay.android.fsend.data.Shipment
import com.fif.fpay.android.fsend.data.TrackingRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface IApiService {
    @GET("/api/unknown")
    fun doGetShipmentInfo(): Call<Shipment?>?

     @GET("/orders")
     suspend fun getOrders(
         @Query("userId") userId: String
     ): Response<ArrayList<Shipment>?>

    @PATCH("/orders/{id}")
    suspend fun updateState(
        @Path("id") id: String,
        @Body updateStateRequest: UpdateStateRequest
    ): Response<Any>


    @PATCH("/tracking/{id}")
    suspend fun tracking(
        @Path("id") id: String,
        @Body trackingRequest: TrackingRequest
    ): Response<Any>
}