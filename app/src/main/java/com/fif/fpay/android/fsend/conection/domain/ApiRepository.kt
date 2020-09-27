package com.fif.fpay.android.fsend.conection.domain

import com.fif.fpay.android.fsend.conection.RestBuilder
import com.fif.fpay.android.fsend.conection.UpdateStateRequest
import com.fif.fpay.android.fsend.data.Shipment
import com.fif.fpay.android.fsend.data.TrackingRequest
import com.fif.fpay.android.fsend.errors.*
import com.fif.fpay.android.fsend.service.IApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

typealias OnSuccess<T> = (T) -> Unit
typealias OnFailure = (IError) -> Unit

interface IApiRepository {
    suspend fun getOrders(
        userId: String,
        success: OnSuccess<ArrayList<Shipment>?>,
        failure: OnFailure
    )

    suspend fun updateState(
        id: String,
        shortcode: String,
        state: String,
        success: OnSuccess<Boolean>,
        failure: OnFailure
    )

    suspend fun tracking(
        id: String,
        latitude: String,
        longitude: String,
        success: OnSuccess<Boolean>,
        failure: OnFailure
    )
}

class ApiRepository: IApiRepository {

    private var endpoind: IApiService =
        RestBuilder.createService(IApiService::class.java, "http://34.70.88.186:80/", false, 20)

    override suspend fun getOrders(
        userId: String,
        success: OnSuccess<ArrayList<Shipment>?>,
        failure: OnFailure
    ) {
        GlobalScope.async {
            try {
                val result = endpoind.getOrders(userId)

                if (result.isSuccessful){
                        success(result.body()!!)
                } else {
                    failure(
                        DError(
                            result.code().toString(),
                            result.message().toString(),
                            result.message().toString()
                        )
                    )
                }
            } catch (ex: BusinessException) {
                failure(DError("400", ex.message.toString()))
            } catch (ex: TechnicalException) {
                failure(DError("500", ex.message.toString()))
            } catch (ex: Exception) {
                failure(GenericError.ERROR_SERVICE_GENERIC)
            }

        }
    }


    override suspend fun updateState(
        id: String,
        shortcode: String,
        state: String,
        success: OnSuccess<Boolean>,
        failure: OnFailure
    ) {
        GlobalScope.async {
            try {
                val result = endpoind.updateState(id, UpdateStateRequest( shortcode,state))

                if (result.isSuccessful){
                    success(true)
                } else {
                    failure(
                        DError(
                            result.code().toString(),
                            result.message().toString(),
                            result.message().toString()
                        )
                    )
                }
            } catch (ex: BusinessException) {
                failure(DError("400", ex.message.toString()))
            } catch (ex: TechnicalException) {
                failure(DError("500", ex.message.toString()))
            } catch (ex: Exception) {
                failure(GenericError.ERROR_SERVICE_GENERIC)
            }

        }
    }

    override suspend fun tracking(
        id: String,
        latitude: String,
        longitude: String,
        success: OnSuccess<Boolean>,
        failure: OnFailure
    ) {
        GlobalScope.async {
            try {
                val result = endpoind.tracking(id, TrackingRequest(latitude,longitude))

                if (result.isSuccessful){
                    success(true)
                } else {
                    failure(
                        DError(
                            result.code().toString(),
                            result.message().toString(),
                            result.message().toString()
                        )
                    )
                }
            } catch (ex: BusinessException) {
                failure(DError("400", ex.message.toString()))
            } catch (ex: TechnicalException) {
                failure(DError("500", ex.message.toString()))
            } catch (ex: Exception) {
                failure(GenericError.ERROR_SERVICE_GENERIC)
            }

        }
    }
}