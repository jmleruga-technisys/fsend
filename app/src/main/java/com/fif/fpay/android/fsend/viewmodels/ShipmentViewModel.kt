package com.fif.fpay.android.fsend.viewmodels

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fif.fpay.android.fsend.commons.AbsentLiveData
import com.fif.fpay.android.fsend.commons.Resource
import com.fif.fpay.android.fsend.data.*
import com.fif.fpay.android.fsend.service.APIClient
import com.fif.fpay.android.fsend.service.IApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class ShipmentViewModel : ViewModel() {
    var shipments: ArrayList<Shipment>? = null
    var iApiService: IApiInterface? = null
    var getClientShipment: LiveData<Resource<Any>>? = null
    private var clientShipmentLiveData: MutableLiveData<Any> = MutableLiveData()
    //private val onboardingRepository: IOnboardingRepository = OnboardingRepository()

    init {
        mockShipments()
        iApiService = APIClient.client!!.create(IApiInterface::class.java)

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
                            Position("-34.2", "32.1"), "Aranguren 242, Glew", "Casa blanca"
                        ),
                        TimeRange(
                            LocalDateTime.of(2018, 6, 25, 22, 22, 22),
                            LocalDateTime.of(2018, 7, 25, 23, 22, 15)
                        )
                    ),
                    "DELIVERED",
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
                            Position("-34.2", "32.1"), "Nuestras Malvinas 277, Glew", "Casa blanca"
                        ),
                        TimeRange(
                            LocalDateTime.of(2018, 6, 25, 22, 22, 22),
                            LocalDateTime.of(2018, 7, 25, 23, 22, 15)
                        )
                    ),
                    "FAILED",
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
                            Position("-34.2", "32.1"), "Calle 10 1077, Glew", "depto 4D"
                        ),
                        TimeRange(
                            LocalDateTime.of(2018, 6, 25, 22, 22, 22),
                            LocalDateTime.of(2018, 7, 25, 23, 22, 15)
                        )
                    ),
                    "CREATED",
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
                            Position("-34.2", "32.1"), "Nuestras Malvinas 277, Glew", "Casa blanca"
                        ),
                        TimeRange(
                            LocalDateTime.of(2018, 6, 25, 22, 22, 22),
                            LocalDateTime.of(2018, 7, 25, 23, 22, 15)
                        )
                    ),
                    "IN_PROGRESS",
                    "1",
                    "1"
                )
            )
        }
    }


//    var getCustomerType: LiveData<Resource<Any>>
//
//
//    private var customerIdLiveData: MutableLiveData<Any> = MutableLiveData()
//
//    var getAskPassword: LiveData<Event<String?>>
//
//    init {
//        getCustomerType = Transformations.switchMap(customerIdLiveData) {
//            if (it == null)
//                AbsentLiveData.create()
//            else {
//                //onboardingRepository.getClientTypeLiveData(xVersion, it)
//            }
//        }
//
//        getAskPassword = Transformations.map(getCustomerType) { response ->
//            when(response.status) {
//                Status.ERROR -> Event(response.message)
//                else -> Event(null)
//            }
//        }
//    }
}