package com.chimallidigital.solucionint.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivityMainBinding
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.ADS_MAIN_STATE
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private var adsState= false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initNavigation()
    }
//    private fun checkAdsState(){
//        adsState= intent.getBooleanExtra(ADS_MAIN_STATE, false)
//        Log.i("ads_main", "AdsState= $adsState")
//
//        if(adsState){
//            showAds()
//            initAds()
//            adsState= false
//        }
//    }

    private fun initNavigation() {
        val navHost=supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController=navHost.navController
        binding.bottomNavView.setupWithNavController(navController)
    }
}