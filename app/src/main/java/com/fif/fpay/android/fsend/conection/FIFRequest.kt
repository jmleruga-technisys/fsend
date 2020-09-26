package com.fif.fpay.android.fsend.conection

//import EncryptAdapter - Hacerla del lado de android

class FIFRequest {
     var endPoint: String = ""
     var requestContentType: FIFContentType =
         FIFContentType.json
     var responseContentType: FIFContentType =
         FIFContentType.json
     var method: FIFMethod =
         FIFMethod.post
     var headers: Map<String, String>? = null
     var isPrivate: Boolean = true
     var parameters: Map<String, String>? = null
     var parametersString: String? = null
     var isDecrypt: Boolean = true
     //var encryptAdapter: EncryptAdapter?
     var serverUrl: String? = ""

     init{
        this.endPoint = endPoint
        this.isPrivate = isPrivate
        this.requestContentType = requestContentType
        this.responseContentType = responseContentType
        this.method = method
        this.headers = if (headers.isNullOrEmpty()) mapOf() else null
        this.parameters = parameters
        this.parametersString = parametersString
        this.isDecrypt = isDecrypt
//        this.encryptAdapter = isEncrypt ? EncryptAdapter() : null
    }
}

