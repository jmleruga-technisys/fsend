package com.fif.fpay.android.fsend.errors

import androidx.annotation.StringRes
import com.fif.fpay.android.fsend.errors.ErrorsApplication.Companion.stringsProvider
import com.fif.fpay.android.fsend.R

enum class GenericError(override val code: String,
                        @StringRes private val idMessage: Int,
                        @StringRes private val idFriendlyMessage: Int) : IError {

    ERROR_GENERIC("999", R.string.error_generic, R.string.friendlyMessage_error_generic),
    ERROR_ROOT_DEVICE("401", R.string.error_root_device, R.string.friendlyMessage_error_root_device),
    ERROR_SERVICE_GENERIC("453", R.string.error_service_generic, R.string.friendlyMessage_error_service_generic),
    ERROR_BAD_INPUT("400", R.string.error_bad_input, R.string.friendlyMessage_error_bad_input),
    ERROR_404("404", R.string.error_404, R.string.friendlyMessage_error_404);

    override val message
        get() = stringsProvider.getString(idMessage)

    override val friendlyMessage
        get() = stringsProvider.getString(idFriendlyMessage)
}

data class DError(
    override val code: String = "999",
    override val message: String = stringsProvider.getString(R.string.error_generic),
    override val friendlyMessage: String = stringsProvider.getString(R.string.friendlyMessage_error_generic)
) : IError

class ConnectionLayerException: TechnicalException(GenericError.ERROR_GENERIC)
class BadResponseException: TechnicalException(GenericError.ERROR_SERVICE_GENERIC)
class BadInputException: BusinessException(GenericError.ERROR_BAD_INPUT)

enum class GenericErrorEvent(@StringRes private val resourceId: Int) : ErrorEvent {
    NONE(0),
    ERROR_GENERIC(R.string.friendlyMessage_error_generic),
    ERROR_ROOT_DEVICE(R.string.friendlyMessage_error_root_device),
    ERROR_SERVICE_GENERIC(R.string.friendlyMessage_error_service_generic),
    ERROR_BAD_INPUT(R.string.friendlyMessage_error_bad_input),
    ERROR_404(R.string.friendlyMessage_error_404);

    override fun getErrorResource() = resourceId
}
