package com.fif.fpay.android.fsend.conection

import com.fif.fpay.android.fsend.errors.IError
import com.fif.fpay.android.fsend.errors.TechnicalException


data class CError(override val code: String = "999", override val message: String = "Hubo un problema con el llamado al servicio. Intente nuevamente.",
                  override val friendlyMessage: String) :
    IError

enum class ConnectionError(override val code: String, override val message: String,
                           override val friendlyMessage: String) : IError {
    ERROR_GENERIC("999", "Hubo un error inesperado. Intente nuevamente.", ""),
    ERROR_ROOT_DEVICE("401", "Lalal", ""),
    ERROR_SERVICE_GENERIC("453", "Hubo un problema con el llamado al servicio. Intente nuevamente.", ""),
    ERROR_CRYPTO_PAYLOAD("500", "No se pudo recuperar el payload de la respuesta.", "No se pudo recuperar el payload de la respuesta."),
    ERROR_SOCKET_CONNECTION("500", "No se puede conectar al socket.", "No se puede conectar al socket."),
    ERROR_DISCONNECTION("1001", "Se perdió la conexión con el socket.", "Se perdió la conexión con el socket.")
}

class ConnectionException(code: String, message: String, friendlyMessage: String): TechnicalException(
    CError(
        code,
        message,
        friendlyMessage
    )
)
class PayloadException(): TechnicalException(ConnectionError.ERROR_CRYPTO_PAYLOAD)
class SocketConnectionException(): TechnicalException(ConnectionError.ERROR_SOCKET_CONNECTION)