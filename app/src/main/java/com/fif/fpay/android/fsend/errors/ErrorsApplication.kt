package com.fif.fpay.android.fsend.errors

import android.app.Application
import com.fif.fpay.android.fsend.conection.StringsProvider

class ErrorsApplication : Application() {

    companion object {
        lateinit var stringsProvider: StringsProvider

        fun init(stringsProvider: StringsProvider) {
            Companion.stringsProvider = stringsProvider
        }
    }

    override fun onCreate() {
        super.onCreate()
        //appComponent().inject(this)
    }
}
