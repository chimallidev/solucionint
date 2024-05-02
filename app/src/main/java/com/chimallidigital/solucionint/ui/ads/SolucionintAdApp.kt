package com.chimallidigital.solucionint.ui.ads

import android.app.Application
import com.google.android.gms.ads.MobileAds

class solucionintAdApp: Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
    }
}