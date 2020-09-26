package com.fif.fpay.android.fsend.conection

import android.util.Log
import com.fif.fpay.android.fsend.errors.DError
import com.fif.fpay.android.fsend.errors.IError
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

interface IConnectionLayer {
    fun callService(baseURL:String,
                    path:String,
                    params:HashMap<String,Any>,
                    header:HashMap<String,String>,
                    method: FIFMethod,
                    isEncrypted:Boolean,
                    success:(response: Response<Any>) -> Unit,
                    failure :(error: IError) -> Unit

    )
}

open class ConnectionLayer : IConnectionLayer {

    companion object {
        private var INSTANCE: ConnectionLayer? = null

        val instance: ConnectionLayer
            get() {
                if (INSTANCE == null) {
                    INSTANCE =
                        ConnectionLayer()
                }

                return INSTANCE!!
            }
    }

    override fun callService(baseURL:String,
                             path:String,
                             params:HashMap<String,Any>,
                             header:HashMap<String,String>,
                             method: FIFMethod,
                             isEncrypted:Boolean,
                             success:(response: Response<Any>) -> Unit,
                             failure :(error: IError) -> Unit
                    

    ){
        val endpoind = RestBuilder.createService(
            BaseRetrofitApi::class.java,
            baseURL,
            isEncrypted,
            2000
        )

        var call = when (method){
            FIFMethod.get -> endpoind.getData(path,header)
            FIFMethod.post -> endpoind.postData(path,header,params)
            FIFMethod.patch -> endpoind.patchData(path,header,params)
            FIFMethod.put -> endpoind.putData(path,header,params)
            FIFMethod.delete -> endpoind.deleteData(path,header, params)
        }

        call.enqueue(object : retrofit2.Callback<Any> {

            override fun onFailure(call: Call<Any>, t: Throwable) {
                failure(CError(message = t.message.toString(), friendlyMessage = ""))
                Log.d("ConnectionLayer",t.message.toString())
            }

            override fun onResponse(
                call: Call<Any>,
                response: Response<Any>
            ) {
                try {
                    handleResponse(response)
                } catch (e: Throwable) {
                    failure(ConnectionError.ERROR_GENERIC)
                }
            }

            private fun handleResponse(response: Response<Any>) {
                Log.d("ConnectionLayer", response.code().toString())
                if (response.isSuccessful)
                    success(response)
                else
                    failure(parseFailure(response.errorBody()))
            }

            private fun parseFailure(errorBody: ResponseBody?): IError {
                val contentType = errorBody?.contentType()
                val type = contentType?.type().orEmpty()
                val subtype = contentType?.subtype().orEmpty()
                val contentTypeStr = "$type/$subtype"

                if ("application/json".equals(contentTypeStr, ignoreCase = true)) {
                    val body = errorBody?.string().orEmpty()
                    return Gson().fromJson(body, DError::class.java)
                        ?: ConnectionError.ERROR_GENERIC
                }
                return ConnectionError.ERROR_GENERIC
            }

        })
    }

    fun postRequest(){

    }
}
