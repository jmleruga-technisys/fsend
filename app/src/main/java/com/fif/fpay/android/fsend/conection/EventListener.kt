package com.fif.fpay.android.fsend.conection

interface EventListener {

    fun onConnect(vararg args: Any)

    fun onConnectError(vararg args: Any)

    fun onConnecting(vararg args: Any)

    fun onDisconnect(vararg args: Any)

    fun onNewMessage(vararg args: Any)

    fun onUpdate(vararg args: Any)

}