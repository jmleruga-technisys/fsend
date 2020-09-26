package com.fif.fpay.android.fsend.conection

import android.os.Build
import java.io.UnsupportedEncodingException
import java.net.*
import java.text.SimpleDateFormat
import java.util.*


class HeaderBuilder {

    enum class RequestType private constructor() {
        KEEPALIVE,
        UPDATE_REQUEST,
        RESPONSE_DATA,
        OTHER
    }

    companion object {

        val TAG = instance.javaClass.simpleName

        val USER_REQUEST_MESSAGETYPE = "0x8002"
        val AUTHENTICATION_REQUEST_MESSAGETYPE = "0x8004"
        val NOTIFICATION_REQUEST_MESSAGETYPE = "0x8100"
        private val ENCRYPTED_REQUEST = "X-Encrypted-Request"
        private val ENCODE_REQUESTS = "encodeRequests"
        private val ENCRYPTED_REQUEST_VALUE = "1"
        private val RECYCLED_TOKEN = "recycled_token"
        private val SCOPE = "scope"
        private val CYBERBANK_LANGUAGE = "cyberbank_language"
        private val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS"
        private val ZONE_FORMAT = "Z"
        private val COOKIE = "Cookie"
        private val DEVICE_FINGER_PRINT = "deviceFingerprint"
        private val ACCEPT = "accept"
        private val ACCEPT_VALUE = "application/xml, image/*, application/json"
        private val MESSAGE_TYPE = "message_type"
        private val MESSAGE_SUBTYPE = "message_subtype"
        private val VERSION = "version"
        private val FRESH_TOKEN = "fresh_token"
        private val CREDENTIAL_SERVICE = "banking/login/user/validate/credential"
        private val SLAVE_PLATFORM = "slave_platform"
        private val OPERATIVE_SYSTEM = "Android OS"
        private val SLAVE_DEVICE_IDENTIFICATION = "slave_device_identification"
        private val SLAVE_LATITUDE = "slave_latitude"
        private val SLAVE_LONGITUDE = "slave_longitude"
        private val SLAVE_ALTITUDE = "slave_altitude"
        private val SLAVE_RADIUS = "slave_radius"
        private val SLAVE_LOCATION_SOURCE = "slave_location_source"
        private val SLAVE_DATETIME = "slave_datetime"
        private val SLAVE_TIMEZONE = "slave_timezone"
        private val SLAVE_COUNTRY = "slave_country"
        private val SLAVE_STATE = "slave_state"
        private val SLAVE_CITY = "slave_city"
        private val SLAVE_NEIGHBORHOOD = "slave_neighborhood"
        private val MASTER_PLATFORM = "master_platform"
        private val BANKING_SERVER = "BankingServer"
        private val MASTER_DEVICE_IDENTIFICATION = "master_device_identification"
        private val SOURCE_VALUE = "source_value"
        private val SOURCE_TYPE = "source_type"
        private val ACTIVITY_FLAG = "ACTIVITY_FLAG"
        private val ACTIVITY_FLAG_VALUE = "true"
        private val ANDROID_ID = "android_id="
        private val MAC_ADDRESS = ";mac_address="
        private val IP = ";ip="
        private val BRAND = ";brand="
        private val MODEL = ";model="
        private val EMPTY_VALUE = ""
        private val BANKING_REGULAR_EXPRESSION = "banking/.+"
        private val SECURITY_REGULAR_EXPRESSION = "security/.+"
        private val BANKING_LOGIN_REGULAR_EXPRESSION = "banking/login/.+"
        private val CHARSET_ENCODING = "UTF-8"
        private val SESSION_ID = "session"
        private val FINGERPRINT = "risk-fingerprint"
        private val EMAILAGE = "slave_device_identification"
        private val X_FLOW_COUNTRY = "x-flow-country"
        private val FP_FLOW_COUNTRY = "fp-flow-country"
        private val X_FLOW_CHANNEL = "x-flow-channel"
        private val MOBILE_CHANNEL = "mobile"

        private var INSTANCE: HeaderBuilder? = null

        @JvmStatic
        val instance: HeaderBuilder
            get() {
                if (INSTANCE == null) {
                    INSTANCE = HeaderBuilder()
                }

                return INSTANCE!!
            }

        var randomSessionId = ""
            set(randomSessionId) {
                field = randomSessionId
            }
        var isLoginFingerprint = ""
            set(isLoginFingerprint) {
                field = isLoginFingerprint
            }
        var lastReceivedToken = ""
            set(lastReceivedToken) {
                field = lastReceivedToken
            }
        private var lastSentToken = ""
        var lastReceivedKeepaliveToken = ""
            set(lastReceivedKeepaliveToken) {
                field = lastReceivedKeepaliveToken
            }
        var lastReceivedResponseDataToken = ""
            set(lastReceivedResponseDataToken) {
                field = lastReceivedResponseDataToken
            }
        var lastReceivedUpdateRequestToken = ""
            set(lastReceivedUpdateRequestToken) {
                field = lastReceivedUpdateRequestToken
            }
        private val latitude = 0.0
        private val longitude = 0.0
        private val altitude = 0.0
        var radius = 0.0
            set(radius) {
                field = radius
            }
        private val accuracy = 0.0f
        var provider = ""
            set(provider) {
                field = provider
            }
        val masterDeviceIdentification = ""
        var firstJSessionId: String? = null
            private set
        var lastJsessionId: String? = null
            set(lastJsessionId) {
                field = lastJsessionId
            }
        var availableVersion: String? = null
            set(availableVersion) {
                field = availableVersion
            }
        private val AVAILABLE_VERSION_FLAG = "available_version"

        fun build(): HashMap<String?, String?> {
            return getHeaderValues()
        }

        fun getHeaderValues(
            path: String? = null,
            messageType: String? = null
        ): HashMap<String?, String?> {
            val instance = SessionManager.instance
            val header = HashMap<String?, String?>()
            header.put(COOKIE, instance!!.cookiesString)
//            header.put(DEVICE_FINGER_PRINT, SharedPreferencesUtils.getFingerprint())
//            header.put(SCOPE, PropertiesReader.getScope())
//            header.put(CYBERBANK_LANGUAGE, MultiLanguageUtil.getApplicationtLanguage())
            header.put(ACCEPT, "application/xml, image/*, application/json")
            messageType?.let { header.put(MESSAGE_TYPE, it) }
            path?.let { header.put(MESSAGE_SUBTYPE, it) }
            val selects = instance.cookies
            val var6 = selects.entries.iterator()

            var cookieAux: Array<String?>
            while (var6.hasNext()) {
                val entry = var6.next() as java.util.Map.Entry<*, *>
                cookieAux = instance.parseCookie(entry.value as String)
                header.put(cookieAux[0], if (cookieAux[1] == null) "" else cookieAux[1])
            }

//            var pInfo: PackageInfo? = null
//
//            try {
//                pInfo = GeneralConfiguration.getInstance().currScreen.packageManager.getPackageInfo(
//                    GeneralConfiguration.getInstance().currScreen.packageName,
//                    0
//                )
//            } catch (var9: Exception) {
//                CLog.e("Error: " + var9.message)
//            }
//
//            if (null != pInfo) {
//                val version = pInfo.versionName
//                header.put(VERSION, version)
//            }

            header.put(SLAVE_PLATFORM, OPERATIVE_SYSTEM)
            //header.put(SLAVE_DEVICE_IDENTIFICATION, androidID)
            header.put(SLAVE_LATITUDE, getLatitude())
            header.put(SLAVE_LONGITUDE, getLongitude())
            header.put(SLAVE_ALTITUDE, getAltitude())
            header.put(SLAVE_RADIUS, getAccuracy())
            header.put(SLAVE_LOCATION_SOURCE, provider)
            header.put(SLAVE_DATETIME, dateTime)
            header.put(SLAVE_TIMEZONE, timeZone)
            header.put(SLAVE_COUNTRY, country)
            header.put(SLAVE_STATE, state)
            header.put(SLAVE_CITY, city)
            header.put(SLAVE_NEIGHBORHOOD, neighborhood)
            header.put(MASTER_PLATFORM, BANKING_SERVER)
            header.put(MASTER_DEVICE_IDENTIFICATION, masterDeviceIdentification)
            header.put(SOURCE_VALUE, "")
            header.put(SOURCE_TYPE, OPERATIVE_SYSTEM)
            header.put(EMAILAGE,"udid=;mac address=;ip=${getIPAddress(true)};brand=;model=")
            header.put(X_FLOW_CHANNEL, MOBILE_CHANNEL)
            header.put(X_FLOW_COUNTRY, country)
            header.put(FP_FLOW_COUNTRY, country)
            if (availableVersion != null) {
                header.put(AVAILABLE_VERSION_FLAG, availableVersion!!)
            }

            randomSessionId.let {header.put(SESSION_ID, it) }

            isLoginFingerprint.let { header.put(FINGERPRINT, it) }


            return header
        }

        /*    private val androidID: String
                get() {
                    var response = ""
                    val applicationContexInstance = ApplicationContext.getInstance()
                    val androidID = applicationContexInstance.deviceID
                    val macAddress = applicationContexInstance.macAddress
                    val ipAddress = applicationContexInstance.ipAddress
                    var brand: String? = null
                    var model: String? = null

                    try {
                        brand = URLEncoder.encode(Build.MANUFACTURER, "UTF-8")
                        model = URLEncoder.encode(Build.VERSION.RELEASE, "UTF-8")
                    } catch (var8: UnsupportedEncodingException) {
                        CLog.e("HeaderBuilder getAndroidID", var8)
                    }

                    if (androidID != null) {
                        response = "android_id=$androidID"
                    }

                    if (macAddress != null) {
                        response = "$response;mac_address=$macAddress"
                    }

                    if (ipAddress != null) {
                        response = "$response;ip=$ipAddress"
                    }

                    if (ipAddress != null) {
                        response = "$response;brand=$brand"
                    }

                    if (ipAddress != null) {
                        response = "$response;model=$model"
                    }

                    return response
                }*/

        private val dateTime: String
            get() {
                val sf1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                return sf1.format(Date())
            }

        private val timeZone: String
            get() {
                val sf = SimpleDateFormat("Z")
                return sf.format(Date())
            }

        fun getLatitude(): String {
            return latitude.toString()
        }

        fun getLongitude(): String {
            return longitude.toString()
        }

        fun getAltitude(): String {
            return altitude.toString()
        }

        fun getAccuracy(): String {
            return accuracy.toString()
        }

        private val neighborhood: String
            get() = ""

        private val city: String
            get() = ""

        private val state: String
            get() = ""

        var country: String = "cl"

        fun cleanFirstJSessionId() {
            firstJSessionId = null
        }

        fun getLastSentToken(): String {
            return lastSentToken
        }

        fun setLastSentToken(lastSentToken: String) {
            var lastSentToken = lastSentToken
            lastSentToken = lastSentToken
        }
    }
}
