package com.fif.fpay.android.fsend.conection

import com.google.gson.annotations.SerializedName
import retrofit2.Response

class BaseResponse<T>(
    @SerializedName("code")
    var code: String? = "200",
    @SerializedName("payload")
    var payload: T? = null,
    @SerializedName("message")
    var message: String? = null) {

    constructor(throwableError: Throwable) : this() {
        code = "500"
        payload = null
        message = throwableError.message
    }

    constructor(response: Response<BaseResponse<T>>) : this() {
        code = response.code().toString()
        if (response.isSuccessful) {
            payload = response.body()?.payload
            message = null
        } else {
            payload = null
            message = response.errorBody()?.string() ?: response.message()
        }
    }

    fun isSuccessful(): Boolean = code?.toInt() in 200..299

}