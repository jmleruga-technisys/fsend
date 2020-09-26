package com.fif.fpay.android.fsend.errors

interface IError {
    val code: String
    val message: String
    val friendlyMessage: String
}

abstract class ErrorException(error: IError) : Exception("${error.code} - ${error.message} - ${error.friendlyMessage}")
open class TechnicalException(error: IError) : ErrorException(error)
open class BusinessException(error: IError) : ErrorException(error)

