package com.leviancode.android.gsmbox

import android.app.Application
import com.leviancode.android.gsmbox.core.data.repository.AppDatabase
import com.yariksoffice.lingver.Lingver

class SmsBoxApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Lingver.init(this)
        AppDatabase.init(applicationContext)
    }
}