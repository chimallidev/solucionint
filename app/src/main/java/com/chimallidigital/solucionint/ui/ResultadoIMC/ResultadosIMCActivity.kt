package com.chimallidigital.solucionint.ui.ResultadoIMC

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MotionEvent
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.core.view.isInvisible
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivityResultadosImcactivityBinding
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.RESULTADOS_IMC
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class ResultadosIMCActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultadosImcactivityBinding
    private var intersticial: InterstitialAd? = null

    private var stateAnimator= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityResultadosImcactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAds()
        initUI()
        showAds()
        initAds()
    }
    private fun initAds() {
        var adRequest: AdRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this,
            "ca-app-pub-2853474354867358/5204410312",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(intersticialAd: InterstitialAd) {
                    intersticial = intersticialAd
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    intersticial = null
                }
            })
    }

    fun showAds() {
        intersticial?.show(this)
    }

    private fun initUI() {
        val imc= intent.getFloatExtra(RESULTADOS_IMC,0f)
        initListeners(imc)
    }

    private fun initListeners(imc: Float) {
        intersticial?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            }

            override fun onAdShowedFullScreenContent() {
                intersticial = null
            }
        }
        when(imc){
            in 0f.. 18.4f->{
                binding.tvTitulo.text= getString(R.string.bajo_peso)
                binding.tvTitulo.setTextColor(getColor(R.color.azul))
                binding.ivIMC.setImageResource(R.drawable.bajo_peso)
                binding.tvNumberIMC.text= imc.toString()
                binding.tvNumberIMC.setTextColor(getColor(R.color.azul))
            }
            in 18.5f.. 24.9f->{
                binding.tvTitulo.text= getString(R.string.peso_normal)
                binding.tvTitulo.setTextColor(getColor(R.color.green))
                binding.ivIMC.setImageResource(R.drawable.peso_normal)
                binding.tvNumberIMC.text= imc.toString()
                binding.tvNumberIMC.setTextColor(getColor(R.color.green))
            }
            in 25f.. 29.9f->{
                binding.tvTitulo.text= getString(R.string.sobrepeso)
                binding.tvTitulo.setTextColor(getColor(R.color.amarillo))
                binding.ivIMC.setImageResource(R.drawable.sobrepeso)
                binding.tvNumberIMC.text= imc.toString()
                binding.tvNumberIMC.setTextColor(getColor(R.color.amarillo))
            }
            in 30f.. 34.9f->{
                binding.tvTitulo.text= getString(R.string.obesidad_uno)
                binding.tvTitulo.setTextColor(getColor(R.color.red))
                binding.ivIMC.setImageResource(R.drawable.obesidad_grado_uno)
                binding.tvNumberIMC.text= imc.toString()
                binding.tvNumberIMC.setTextColor(getColor(R.color.red))
            }
            in 35f.. 39.9f->{
                binding.tvTitulo.text= getString(R.string.obesidad_dos)
                binding.tvTitulo.setTextColor(getColor(R.color.red))
                binding.ivIMC.setImageResource(R.drawable.obesidad_grado_dos)
                binding.tvNumberIMC.text= imc.toString()
                binding.tvNumberIMC.setTextColor(getColor(R.color.red))
            }
            else->{
                if (imc>=40){
                    binding.tvTitulo.text= getString(R.string.obesidad_tres)
                    binding.tvTitulo.setTextColor(getColor(R.color.red))
                    binding.ivIMC.setImageResource(R.drawable.obesidad_grado_tres)
                    binding.tvNumberIMC.text= imc.toString()
                    binding.tvNumberIMC.setTextColor(getColor(R.color.red))
                }
            }
        }
        binding.BTNRegresar.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.backgroundBTNRegresar, binding.shadowBTNRegresar)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("BTN_Regresar_Res_IMC", "X= $x")
                        Log.i("BTN_Regresar_Res_IMC", "Y= $y")
                        if (x > -8f && x < 659f && y > -4f && y < 158f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(
                                    binding.backgroundBTNRegresar, binding.shadowBTNRegresar
                                )
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -8f && x < 659f && y > -4f && y < 158f && !stateAnimator) {
                            btnAnimation(binding.backgroundBTNRegresar, binding.shadowBTNRegresar)
                            onBackPressedDispatcher.onBackPressed()
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
    }
    private fun btnPressed(view: View, view2: View) {
        view.animate().apply {
            duration = 200
            translationY(10f)
            scaleX(0.9f)
            scaleY(0.9f)
            interpolator = BounceInterpolator()
            withStartAction {
                view2.isInvisible = true
            }
            start()
        }
    }

    private fun btnAnimation(view: View, view2: View) {
        view.animate().apply {
            duration = 200
            translationY(0f)
            scaleX(1.0f)
            scaleY(1.0f)
            interpolator = BounceInterpolator()
            withEndAction {
                view2.isInvisible = false
            }
            start()
        }
    }
}