package com.fif.fpay.android.fsend.conection

import java.util.*

class SessionManager private constructor() {
    private val sessionIdInBankingServerSYNC = Any()
    val cookies: HashMap<String?, String?> =
        HashMap<String?, String?>()
    var type = ""
    var credentialsFilled: ArrayList<String?>? = null
        private set

    val cookiesString: String
        get() {
            var separator = ""
            var strCookies = ""
            val values: Collection<String?> = cookies.values
            val iterator = values.iterator()
            var i = 0
            while (iterator.hasNext()) {
                if (i != 0) {
                    separator = "; "
                }
                strCookies = strCookies + separator + iterator.next()
                ++i
            }
            //CLog.i("cookies string : $strCookies")
            return strCookies
        }

    fun parseCookie(cookie: String?): Array<String?> {
        val strings = arrayOfNulls<String>(2)
        if (cookie != null) {
            val indexOfEquals = cookie.indexOf("=")
            strings[0] = cookie.substring(0, indexOfEquals)
            strings[1] = cookie.substring(indexOfEquals + 1, cookie.indexOf(";"))
        }
        return strings
    }

    val bankingServerJSessionId: String?
        get() {
            synchronized(sessionIdInBankingServerSYNC) {
                return instance!!.cookies["JSESSIONID"]
            }
        }

    fun putCredentials(credential: String?) {
        if (credentialsFilled == null) {
            credentialsFilled = ArrayList<String?>()
        }
        credentialsFilled!!.add(credential)
    }

    fun setNullCredentialsFilled() {
        credentialsFilled = null
    }

    fun cleanSessionIdInBankingServer() {
        synchronized(
            sessionIdInBankingServerSYNC
        ) { instance!!.cookies.clear() }
    }

    companion object {
        const val JSESSIONID_NAME = "JSESSIONID"
        var instance: SessionManager? = null
            get() {
                if (null == field) {
                    field = SessionManager()
                }
                return field
            }
            private set
    }
}
