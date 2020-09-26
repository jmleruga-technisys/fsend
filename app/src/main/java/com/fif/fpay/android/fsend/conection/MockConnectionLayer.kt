package com.fif.fpay.android.connection.connectionFinal

import com.fif.fpay.android.fsend.conection.FIFMethod
import com.fif.fpay.android.fsend.conection.ConnectionError
import com.fif.fpay.android.fsend.conection.IConnectionLayer
import com.fif.fpay.android.fsend.errors.IError
import okhttp3.ResponseBody
import retrofit2.Response

/**
 * Created by Ezequiel Maloberti on 4/9/2019.
 */
class MockConnectionLayer : IConnectionLayer {

    private lateinit var onSuccessCall: (response: Response<Any>) -> Unit
    private lateinit var onFailureCall: (error: IError) -> Unit

    override fun callService(baseURL:String,
                             path:String,
                             params:HashMap<String,Any>,
                             header:HashMap<String,String>,
                             method: FIFMethod,
                             isEncrypted:Boolean,
                             success:(response: Response<Any>) -> Unit,
                             failure :(error: IError) -> Unit

    ) {
        onSuccessCall = success
        onFailureCall = failure
    }

    fun executeSuccess(response: Any) {
        onSuccessCall.invoke(Response.success(response))
    }

    fun executeFail(error: Throwable) {
        onFailureCall.invoke(ConnectionError.ERROR_GENERIC)
    }

    fun executeError(code: Int, body: ResponseBody) {
        onSuccessCall.invoke(Response.error(code, body))
    }
}