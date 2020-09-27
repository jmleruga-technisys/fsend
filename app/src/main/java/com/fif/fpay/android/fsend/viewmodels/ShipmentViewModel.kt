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

    fun mockShipments() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            shipments = arrayListOf(
                Shipment(
                    arrayListOf(
                        Product("Cerveza", 20.0, "Unidades", "Imaging")
                    ),
                    ClientInfo(
                        "Maxi de Glew",
                        "220294020",
                        Address(
                            Location(Position("-34.2", "32.1")), "Aranguren 242, Glew", "Casa blanca"
                        ),
                        TimeRange(
                            "",
                            ""
                        )
                    ),
                    "DELIVERED",
                    "1",
                    "1",
                    "1"
                ),
                Shipment(
                    arrayListOf(
                        Product("Vino", 2.0, "Unidades", ""),
                        Product("Fernet", 1.0, "Litro", "")
                    ),
                    ClientInfo(
                        "Tarry Pollo",
                        "220294020",
                        Address(
                            Location(Position("-34.2", "32.1")), "Nuestras Malvinas 277, Glew", "Casa blanca"
                        ),
                        TimeRange(
                            "",
                            ""
                        )
                    ),
                    "FAILED",
                    "1",
                    "1",
                    "1"
                ),
                Shipment(
                    arrayListOf(
                        Product("Desodorantes", 2.0, "Unidades", ""),
                        Product("Explosivos", 30.0, "Kilos", "")
                    ),
                    ClientInfo(
                        "Juanito",
                        "+5492216057065",
                        Address(
                            Location(Position("-34.2", "32.1")), "Calle 10 1077, Glew", "depto 4D"
                        ),
                        TimeRange(
                            "",
                            ""
                        )
                    ),
                    "CREATED",
                    "1",
                    "1",
                "1"
                ),
                Shipment(
                    arrayListOf(
                        Product("Vino", 2.0, "Unidades", ""),
                        Product("Fernet", 1.0, "Litro", "")
                    ),
                    ClientInfo(
                        "Tarry Pollo",
                        "220294020",
                        Address(
                            Location(Position("-34.2", "32.1")), "Nuestras Malvinas 277, Glew", "Casa blanca"
                        ),
                        TimeRange(
                            "",
                            ""
                        )
                    ),
                    "IN_PROGRESS",
                    "1",
                    "1",
                    "1"
                )
            )
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
        //success.invoke(mockPaymentInfo())
    }

    fun setFinalize(shortcode: String, failure: OnFailure){
        this.updateState(shortcode,
        success = {
            qrLiveData.postValue(Resource.success(true))
        },
        failure = failure)
    }

    fun setInProgress(failure: OnFailure){
        updateState("",
        success = {
            inProgressLiveData.postValue(Resource.success(it))
        },
        failure ={
            failure.invoke(it)
        })
    }


    fun updateState(shortcode: String, success: OnSuccess<Boolean>, failure: OnFailure) {
        try {
            GlobalScope.async {
                repository.updateState(currentShipment!!.id, shortcode, currentShipment!!.state,
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