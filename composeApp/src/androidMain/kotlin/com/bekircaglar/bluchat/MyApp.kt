package com.bekircaglar.bluchat

import android.app.Application
import android.app.LocaleManager
import android.content.Context

class MyApp :Application() {
    override fun onCreate() {
        super.onCreate()
        AppContext.setUp(applicationContext)
    }
}