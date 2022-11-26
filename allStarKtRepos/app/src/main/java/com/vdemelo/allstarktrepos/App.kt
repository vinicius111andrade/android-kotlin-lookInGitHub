package com.vdemelo.allstarktrepos

import android.app.Application
import com.vdemelo.allstarktrepos.di.appModule
import com.vdemelo.allstarktrepos.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidContext(androidContext = this@App)
            modules(appModule, networkModule)
        }

    }
}
