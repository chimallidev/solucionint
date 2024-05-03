package com.chimallidigital.solucionint.ui.tabata_timer_playing

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivityFinishTabataTimerBinding
import com.chimallidigital.solucionint.ui.home.MainActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class FinishTabataTimerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinishTabataTimerBinding
    private var intersticial: InterstitialAd? = null

    private val handler = Handler(Looper.getMainLooper())
    private var timerSeconds = 0
    private var stateAnimator = false
    var mMediaPlayer: MediaPlayer? = null
    val runnable2 = object : Runnable {
        override fun run() {
            timerSeconds++
            if (timerSeconds == 5) {
                showAds()
                initAds()
            }
            if (timerSeconds == 9) {
                binding.BTNBack.isVisible = true
            }

            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishTabataTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAds()
        initListeners()
        waitOnesecond()
        stopSound()
        playSound(R.raw.pig_level_win_2)
        vibration(true)
    }

    override fun onBackPressed() {
        // Your logic here
        showAds()
        initAds()
        stopTimer()
        binding.BTNBack.isVisible= true
    }

    private fun initListeners() {
        binding.BTNBack.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.backgroundBTNRegresar, binding.shadowBTNRegresar)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("regresar_FinishTabataBTN", "X= $x")
                        Log.i("regresar_FinishTabataBTN", "Y= $y")
                        if (x > -1f && x < 660f && y > -4f && y < 214f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(
                                    binding.backgroundBTNRegresar,
                                    binding.shadowBTNRegresar
                                )
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -1f && x < 660f && y > -4f && y < 214f && !stateAnimator) {
                            btnAnimation(binding.backgroundBTNRegresar, binding.shadowBTNRegresar)
                            val intent =
                                Intent(this@FinishTabataTimerActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        intersticial?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            }

            override fun onAdShowedFullScreenContent() {
                intersticial = null
            }
        }
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

    private fun waitOnesecond() {
        handler.postDelayed(runnable2, 1000)
    }

    private fun playSound(sonido: Int) {
        if (mMediaPlayer == null) {

            mMediaPlayer = MediaPlayer.create(this, sonido)
            mMediaPlayer!!.isLooping = false
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }

    fun stopSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    private fun vibration(isVibrationOn: Boolean) {
        val context = this
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrationEffect =
                VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).run {
                if (isVibrationOn) {
                    this.defaultVibrator.vibrate(vibrationEffect)
                } else {
                    this.defaultVibrator.cancel()
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect =
                VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
            (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).run {
                if (isVibrationOn) {
                    this.vibrate(vibrationEffect)
                } else {
                    this.cancel()
                }
            }
        } else {
            (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).run {
                if (isVibrationOn) {
                    this.vibrate(1000)
                } else {
                    this.cancel()
                }
            }
        }
    }

    private fun stopTimer() {
        handler.removeCallbacks(runnable2)
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