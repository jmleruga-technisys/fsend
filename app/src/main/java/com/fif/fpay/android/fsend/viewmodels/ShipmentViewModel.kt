package com.fif.fpay.android.fsend.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fif.fpay.android.fsend.commons.AbsentLiveData
import com.fif.fpay.android.fsend.commons.Event
import com.fif.fpay.android.fsend.commons.Resource
import com.fif.fpay.android.fsend.commons.Status

class ShipmentViewModel : ViewModel() {
    //private val onboardingRepository: IOnboardingRepository = OnboardingRepository()


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