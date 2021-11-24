package com.leviancode.android.gsmbox

import android.app.Application
import com.leviancode.android.gsmbox.utils.di.*
import com.yariksoffice.lingver.Lingver
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SmsBoxApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Lingver.init(this)
        startKoin {
            androidContext(applicationContext)
            modules(modules)
        }
    }

    companion object {
        val modules = listOf(
            databaseModule,
            viewModelsModule,
            repositoriesModule,
            useCasesModule,
            managersModule
        )
    }
}