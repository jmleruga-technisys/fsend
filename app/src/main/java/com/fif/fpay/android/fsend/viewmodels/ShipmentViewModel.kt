package com.fif.fpay.android.fsend.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fif.fpay.android.fsend.commons.AbsentLiveData
import com.fif.fpay.android.fsend.commons.Resource
import com.fif.fpay.android.fsend.data.Address
import com.fif.fpay.android.fsend.data.ClientInfo
import com.fif.fpay.android.fsend.data.Product
import com.fif.fpay.android.fsend.data.Shipment
import com.fif.fpay.android.fsend.service.APIClient
import com.fif.fpay.android.fsend.service.IApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShipmentViewModel : ViewModel() {
    var shipments: ArrayList<Shipment>? = null
    var iApiService:IApiInterface?= null
    var getClientShipment : LiveData<Resource<Any>>? = null
    private var clientShipmentLiveData: MutableLiveData<Any> = MutableLiveData()
    //private val onboardingRepository: IOnboardingRepository = OnboardingRepository()

    init {
        mockShipments()
        iApiService = APIClient.client!!.create(IApiInterface::class.java)

    }

    fun mockShipments() {
        shipments = arrayListOf(
            Shipment(
                arrayListOf(
                    Product("Cerveza", 20.0, "Unidades")
                ),
                ClientInfo(
                    "Maxi de Glew",
                    "220294020",
                    Address("Aranguren 242, Glew", "", "Casa blanca")
                ),
                "DELIVERED"
            ),
            Shipment(
                arrayListOf(
                    Product("Vino", 2.0, "Unidades"),
                    Product("Fernet", 1.0, "Litro")
                ),
                ClientInfo(
                    "Tarry Pollo",
                    "220294020",
                    Address("Nuestras Malvinas 277, Glew", "", "Casa blanca")
                ),
                "FAILED"
            ),
            Shipment(
                arrayListOf(
                    Product("Desodorantes", 2.0, "Unidades"),
                    Product("Explosivos", 30.0, "Kilos")
                ),
                ClientInfo(
                    "Juanito",
                    "+5492216057065",
                    Address("Calle 10 1077, Glew", "", "depto 4D")
                ),
                "CREATED"
            ),
            Shipment(
                arrayListOf(
                    Product("Vino", 2.0, "Unidades"),
                    Product("Fernet", 1.0, "Litro")
                ),
                ClientInfo(
                    "Tarry Pollo",
                    "220294020",
                    Address("Nuestras Malvinas 277, Glew", "", "Casa blanca")
                ),
                "IN_PROGRESS"
            )
        )
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