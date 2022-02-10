package com.brainymobile.android.smsbox

import android.app.Application
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SmsBoxApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Lingver.init(this)
    }
}