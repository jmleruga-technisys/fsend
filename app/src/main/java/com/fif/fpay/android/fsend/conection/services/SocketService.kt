package com.fif.fpay.android.fsend.conection.services

import android.util.Log
import com.fif.fpay.android.fsend.conection.SocketConnectionException
import com.fif.fpay.android.fsend.conection.EventListener
import io.socket.client.IO
import io.socket.client.Socket.*
import java.net.URISyntaxException
import io.socket.emitter.Emitter
import okhttp3.OkHttpClient
import java.lang.Exception
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.cert.X509Certificate
import javax.net.ssl.*
import javax.security.cert.CertificateException


interface ISocketService {

    @Throws(URISyntaxException::class)
    suspend fun connect(transactionId: String)

    fun disconnect()

    fun setEventListener(eventListener: EventListener)
}


class SocketService : ISocketService {
    val TAG = SocketService::class.java.simpleName
    var SOCKET_URL = "https://payments-wallet-qa.fif.tech/"
    var SOCKET_PATH = "/payment-request/websocket"
    var mTransactionId: String? = ""
    var SOCKET_QUERY: String? = "transactionId=$mTransactionId"
    private val EVENT_NEW_MESSAGE = "new message"
    private val EVENT_UPDATE = "update"
    private var INSTANCE: SocketService? = null
    var mEventListener: EventListener? = null
    var mSocket: io.socket.client.Socket? = null
    var timeOut: Long? = null

    /**
     * Returns single instance of this class, creating it if necessary.
     *
     * @return
     */
    val instance: SocketService
        get() {
            if (INSTANCE == null) {
                INSTANCE =
                    SocketService()
            }

            return INSTANCE as SocketService
        }

    fun setOptions(): IO.Options{
        val opts = IO.Options()
        opts.path = SOCKET_PATH
        opts.query = SOCKET_QUERY
        opts.secure = true
        opts.transports = arrayOf("websocket")
        opts.forceNew = true
        opts.reconnection = true
        opts.upgrade = false
        opts.timeout = timeOut!!
        try {
            val myHostnameVerifier = object : HostnameVerifier {
                override fun verify(hostname: String, session: SSLSession): Boolean {
                    return true
                }
            }
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            })

            var mySSLContext: SSLContext? = null
            try {
                mySSLContext = SSLContext.getInstance("Default")
                try {
                    mySSLContext!!.init(null, trustAllCerts, null)
                } catch (e: KeyManagementException) {
                    e.printStackTrace()
                }

            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }

            val okHttpClient = OkHttpClient.Builder().hostnameVerifier(myHostnameVerifier)
                .sslSocketFactory(mySSLContext!!.socketFactory).build()

            IO.setDefaultOkHttpWebSocketFactory(okHttpClient)
            IO.setDefaultOkHttpCallFactory(okHttpClient)
            opts.callFactory = okHttpClient;
            opts.webSocketFactory = okHttpClient;
            return opts
        } catch (e: URISyntaxException) {
            Log.d("ERROR :", e.toString());
        }
        return opts
    }

    /**
     * Connect to the server.
     *
     * @param username
     * @throws URISyntaxException
     */
    @Throws(URISyntaxException::class)
    override suspend fun connect(transactionId: String) {
        try{
            mTransactionId = transactionId
            SOCKET_QUERY = "transactionId=$mTransactionId"
            var opts:IO.Options = this.setOptions()
            mSocket = IO.socket(SOCKET_URL, opts)

            // Register the incoming events and their listeners
            // on the socket.
            if(mSocket != null) {
                mSocket!!.on(EVENT_CONNECT, onConnect)
                mSocket!!.on(EVENT_CONNECTING, onConnecting)
                mSocket!!.on(EVENT_CONNECT_ERROR, onConnectError)
                mSocket!!.on(EVENT_CONNECT_TIMEOUT, onConnectTimeout)
                mSocket!!.on(EVENT_RECONNECT, onReconnect)
                mSocket!!.on(EVENT_DISCONNECT, onDisconnect);
                mSocket!!.on(EVENT_NEW_MESSAGE, onNewMessage);
                mSocket!!.on(EVENT_UPDATE, onUpdate)
                mSocket!!.connect()
            }
        }catch (e: Exception){
            throw SocketConnectionException()
            //Log.d("ConnectError", e.message)
        }
    }

    /**
     * Disconnect from the server.
     *
     */
    override fun disconnect() {
        if (mSocket == null)
            mSocket = IO.socket(SOCKET_URL, this.setOptions())
        if(mSocket!!.connected())
            mSocket!!.disconnect()
    }

    /**
     * Send chat message to the server.
     *
     * @param chatMessage
     * @return
     */
    fun sendMessage(chatMessage: String){
            mSocket!!.emit(EVENT_NEW_MESSAGE, chatMessage)
    }

    /**
     * Set eventListener.
     *
     * When server sends events to the socket, those events are passed to the
     * RemoteDataSource -> Repository -> Presenter -> View using EventListener.
     *
     * @param eventListener
     */
    override fun setEventListener(eventListener: EventListener) {
        mEventListener = eventListener
    }

    private val onConnect = Emitter.Listener { args ->
        Log.i(TAG, "call: onConnect")
        mSocket!!.emit("connected", mTransactionId)
        mEventListener?.let { it.onConnect(args) }
    }

    private val onConnecting = Emitter.Listener { args ->
        Log.i(TAG, "call: onConnecting")
        mEventListener?.let { it.onConnecting(args) }
    }

    private val onConnectError = Emitter.Listener { args ->
        Log.i(TAG, "call: onConnectError" + (args[0] as Exception).message)
        mEventListener?.let { it.onConnectError(args) }
    }

    private val onConnectTimeout = Emitter.Listener { args ->
        Log.i(TAG, "call: onConnectTimeout")
        mSocket!!.disconnect()
    }

    private val onReconnect = Emitter.Listener { args ->
        Log.i(TAG, "call: onReconnect")
    }

    private val onDisconnect = Emitter.Listener { args ->
            Log.i(TAG, "call: onDisconnect")
            mEventListener?.let { it.onDisconnect(args) }
    }

    private val onNewMessage = Emitter.Listener { args ->
            Log.i(TAG, "call: onNewMessage")
            mEventListener?.let { it.onNewMessage(args) }
    }

    private val onUpdate = Emitter.Listener { args ->
            mEventListener?.let { it.onUpdate(args) }
            Log.i(TAG, "call: onUpdate")
    }

}