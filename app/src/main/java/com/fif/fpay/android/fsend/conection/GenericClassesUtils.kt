package com.fif.fpay.android.fsend.conection

import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type

object GenericClassesUtils {

 /***
     * Generic function to create an Instance of a Class from a Json using GSON
     * @param classToInstantiate
     * @param jsonObject
     * @param <T>
     * @return
    </T> */
    fun <T> generateClassFromJsonObject(classToInstantiate: Class<T>, jsonObject: JSONObject): T {

        val gson = GsonBuilder().create()
        return gson.fromJson(jsonObject.toString(), classToInstantiate)
    }

    /***
     * Generic function to create an Instance of a TypeToken from a Json using GSON
     * @param typeTokenToInstantiate
     * @param jsonObject
     * @param <T>
     * @return
    </T> */
    fun <T> generateClassFromJsonObject(typeTokenToInstantiate: Type, jsonObject: JSONObject): T {

        val gson = GsonBuilder().create()
        return gson.fromJson(jsonObject.toString(), typeTokenToInstantiate)
    }

    /***
     * Generic function to create an Instance of a TypeToken from a Json using GSON
     * @param typeTokenToInstantiate
     * @param jsonArray
     * @param <T>
     * @return
    </T> */
    fun <T> generateClassFromJsonObject(typeTokenToInstantiate: Type, jsonArray: JSONArray): T {

        val gson = GsonBuilder().create()
        return gson.fromJson(jsonArray.toString(), typeTokenToInstantiate)
    }
}
