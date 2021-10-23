package com.vdemelo.allstarktrepos

import android.app.Application
import com.vdemelo.allstarktrepos.di.serviceModule
import com.vdemelo.allstarktrepos.di.viewModelModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import timber.log.Timber
import timber.log.Timber.DebugTree


class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        val myModules: List<Module> =
            listOf(
                viewModelModule,
                serviceModule
            )

        startKoin {
            androidLogger(org.koin.core.logger.Level.DEBUG)
            androidContext(this@BaseApplication)
            modules(myModules)
        }
    }
}