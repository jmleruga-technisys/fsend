package com.fif.fpay.android.connection.connectionFinal

import com.fif.fpay.android.fsend.conection.services.SocketService
import com.fif.fpay.android.fsend.conection.EventListener
import io.socket.client.Socket

class SocketBuilder() {
    private var mSocketService: SocketService? = null
    private var SOCKET_URL: String? = null
    private var SOCKET_PATH: String? = null
    private var SOCKET_QUERY: String? = null
    private var mEventListener: EventListener? = null
    private var mSocket: Socket? = null
    private var timeOut: Long? = null

    fun withUrl(url: String):SocketBuilder {
        this.SOCKET_URL = url
        return this
    }

    fun withEventListener(eventListener: EventListener):SocketBuilder {
        this.mEventListener = eventListener
        return this
    }

    fun withSocket(socket: Socket):SocketBuilder {
        this.mSocket = socket
        return this
    }

    fun withPath(path: String):SocketBuilder {
        this.SOCKET_PATH = path
        return this
    }

    fun withQuery(query: String):SocketBuilder {
        this.SOCKET_QUERY = query
        return this
    }

    fun withTimeout(timeOut: Long):SocketBuilder{
        this.timeOut = timeOut
        return this
    }

    fun build(): SocketService {
        val socketService = SocketService()
        this.SOCKET_URL?.let { socketService.SOCKET_URL = this.SOCKET_URL!! }
        this.SOCKET_PATH?.let { socketService.SOCKET_PATH = this.SOCKET_PATH!! }
        this.SOCKET_QUERY?.let { socketService.SOCKET_QUERY = this.SOCKET_QUERY!! }
        this.mEventListener?.let { socketService.mEventListener = this.mEventListener!! }
        this.mSocket?.let { socketService.mSocket = this.mSocket!! }
        this.timeOut?.let { socketService.timeOut = this.timeOut!! }

        return socketService
    }

    fun createSocket(url: String, eventListener: EventListener, path: String, query: String, timeOut: Long): SocketService {
        var socketBuilder =  SocketBuilder()
        if(url.isNotEmpty()){
            socketBuilder.withUrl(url)
        }
        eventListener?.let { socketBuilder.withEventListener(eventListener) }
        if(path.isNotEmpty()){
            socketBuilder.withPath(path)
        }
        if(!query.isNullOrEmpty()){
            socketBuilder.withQuery(query)
        }
        socketBuilder.withTimeout(timeOut)

        return socketBuilder.build()
    }

}