package com.fif.fpay.android.fsend.viewmodels

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fif.fpay.android.fsend.commons.Event
import com.fif.fpay.android.fsend.commons.Resource
import com.fif.fpay.android.fsend.commons.Status
import com.fif.fpay.android.fsend.conection.domain.ApiRepository
import com.fif.fpay.android.fsend.conection.domain.IApiRepository
import com.fif.fpay.android.fsend.data.*
import com.fif.fpay.android.fsend.errors.GenericError
import com.fif.fpay.android.fsend.errors.IError
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.collections.ArrayList

typealias OnSuccess<T> = (T) -> Unit?
typealias OnFailure = (IError) -> Unit?

class ShipmentViewModel : ViewModel() {
    var shipments: ArrayList<Shipment>? = null
    var dataFromMarkers: HashMap<LatLng,Int>? = null
    var currentShipment: Shipment? = null
    private val repository: IApiRepository = ApiRepository()
    private var userId = "01"
    var gotShipments: LiveData<Event<ArrayList<Shipment>?>>
    private var clientShipmentLiveData: MutableLiveData<Resource<ArrayList<Shipment>>> = MutableLiveData()

    var qrLiveData = MutableLiveData<Resource<Boolean?>>()
    var dataFromMarketLiveData = MutableLiveData<Resource<HashMap<LatLng,Int>>>()
    var inProgressLiveData = MutableLiveData<Resource<Boolean?>>()

    var validatedQr: LiveData<Event<Boolean?>>
    var setInProgress: LiveData<Event<Boolean?>>


    init {
        //mockShipments()

        gotShipments = Transformations.map(clientShipmentLiveData) { response ->
            when(response.status) {
                Status.SUCCESS ->
                    Event(response.data)
                else -> Event(null)
            }
        }

        validatedQr = Transformations.map(qrLiveData) {
            when (it.status) {
                Status.SUCCESS -> Event(true)
                else -> Event(null)
            }
        }

        setInProgress = Transformations.map(inProgressLiveData) {
            when (it.status) {
                Status.SUCCESS -> Event(true)
                else -> Event(null)
            }
        }

    }

    fun getShipments(failure: OnFailure) {
        GlobalScope.async {
            repository.getOrders(userId,
            success = {
                it?.let {
                    shipments = it
                    clientShipmentLiveData.postValue(Resource.success(it))
                }
            },
            failure = {
                failure.invoke(it)
            })
        }
    }

    fun setFinalize(shortcode: String, failure: OnFailure){
        this.updateState(currentShipment!!, shortcode,
        success = {
            qrLiveData.postValue(Resource.success(true))
        },
        failure = failure)
    }

    fun setInProgress(shipment: Shipment, failure: OnFailure){
        updateState(shipment, "",
        success = {
            inProgressLiveData.postValue(Resource.success(it))
        },
        failure ={
            failure.invoke(it)
        })
    }


    fun updateState(shipment: Shipment, shortcode: String, success: OnSuccess<Boolean>, failure: OnFailure) {
        try {
            GlobalScope.async {
                repository.updateState(shipment.id, shortcode, shipment.state,
                    success = {
                        success.invoke(it)
                    },
                    failure = {
                        failure.invoke(it)
                    })
            }
        }catch (e: Exception){
            failure.invoke(GenericError.ERROR_BAD_INPUT)
        }
    }
}