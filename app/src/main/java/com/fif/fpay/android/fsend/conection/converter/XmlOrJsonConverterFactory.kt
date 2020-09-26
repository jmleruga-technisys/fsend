package com.fif.tech.android.commons.android_connection.utils.converter

import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.lang.reflect.Type

@Retention(AnnotationRetention.RUNTIME)
annotation class Xml

@Retention(AnnotationRetention.RUNTIME)
internal annotation class Json

class XmlOrJsonConverterFactory : Converter.Factory() {

    companion object {
        fun create(): XmlOrJsonConverterFactory {
            return XmlOrJsonConverterFactory()
        }
    }

    override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
    ): Converter<ResponseBody, *>? {

        for (annotation in annotations) {
            when {
                annotation.annotationClass == Xml::class -> return SimpleXmlConverterFactory
                        .createNonStrict(Persister(AnnotationStrategy()))
                        .responseBodyConverter(type, annotations, retrofit)
                annotation.annotationClass == Json::class -> return GsonConverterFactory
                        .create(GsonBuilder().setLenient().create())
                        .responseBodyConverter(type, annotations, retrofit)
            }
        }
        return GsonConverterFactory
                .create(GsonBuilder().setLenient().create())
                .responseBodyConverter(type, annotations, retrofit)
    }
}