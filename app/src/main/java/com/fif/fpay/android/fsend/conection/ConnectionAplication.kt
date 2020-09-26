package com.fif.fpay.android.fsend.conection

import android.app.Application
import android.content.Context

class ConnectionAplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        appComponent().inject(this)

    }

    companion object {
        var applicationComponent: ApplicationComponent? = null

        fun setAppContext(mockApplicationContext: Context) {
            context = mockApplicationContext
        }

        lateinit var context: Context
            private set
    }
}

private fun buildDagger(): ApplicationComponent {
    if (ConnectionAplication.applicationComponent == null) {
       /* ConnectionAplication.applicationComponent = DaggerApplicationComponent
            .builder()
            .build()*/
    }
    return ConnectionAplication.applicationComponent!!
}

fun appComponent(): ApplicationComponent {
    return buildDagger()
}