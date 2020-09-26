package com.fif.fpay.android.fsend.conection

import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        ApplicationModule::class
    ]
)
interface ApplicationComponent {
    fun inject(connApplication: ConnectionAplication)
}

@Module
class ApplicationModule(private val application: ConnectionAplication) {
    @Singleton
    @Provides
    fun provideContext(): Context {
        return application
    }
}
