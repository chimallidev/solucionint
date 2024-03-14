package com.chimallidigital.solucionint.ui.tabata_timer_playing

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.view.isVisible
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivityFinishTabataTimerBinding
import com.chimallidigital.solucionint.ui.home.MainActivity
import com.chimallidigital.solucionint.ui.tabata_timer.TabataTimerActivity

class FinishTabataTimerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinishTabataTimerBinding

    private val handler = Handler(Looper.getMainLooper())
    private var timerSeconds=0
    var mMediaPlayer: MediaPlayer? = null
    val runnable2 = object : Runnable {
        override fun run() {
            timerSeconds++
            if (timerSeconds==5){
                val intent= Intent(this@FinishTabataTimerActivity, MainActivity::class.java)
                startActivity(intent)
            }
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFinishTabataTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        waitOnesecond()
        stopSound()
        playSound(R.raw.pig_level_win_2)
        vibration(true)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        // Your logic here
        stopTimer()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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
}