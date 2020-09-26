package com.fif.fpay.android.fsend.commons

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

class Resource<T> private constructor(val status: Status, val code: String?, val data: T?, val message: String?) {
    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS,
                null,
                data,
                null
            )
        }

        fun <T> error(msg: String, code: String? = null, data: T?): Resource<T> {
            return Resource(
                Status.ERROR,
                code,
                data,
                msg
            )
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(
                Status.LOADING,
                null,
                data,
                null
            )
        }
    }
}