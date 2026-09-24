package com.abrarshakhi.messman.core

import android.app.Application
import com.abrarshakhi.messman.core.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MessManApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MessManApp)
            modules(appModules)
        }
    }
}
