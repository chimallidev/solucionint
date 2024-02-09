package com.chimallidigital.solucionint.ui.cronometro

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnStart
import androidx.core.view.isVisible
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivityCronometroBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CronometroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCronometroBinding
    private var time: Int = 0
    private var lap: Int = 0
    private var stateAnimator = false
    private var isRunning = false
    private var timerSeconds = 0
    private var isBTN1= false
    private var isBTN2= false
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            timerSeconds++
            val hours = timerSeconds / 3600
            val minutes = (timerSeconds % 3600) / 60
            val seconds = timerSeconds % 60


            if (hours == 0) {
                if (minutes == 0) {
                    binding.tvSeconds.isVisible = true
                    binding.tvMinutes.isVisible = false
                    binding.tvHours.isVisible = false
                    binding.tvSeconds.text = secondsFormatted(seconds)
                    binding.tvUnitTypes.text= getString(R.string.seconds)
                } else {
                    val time2 = String.format("%02d:%02d", minutes, seconds)
                    binding.tvSeconds.isVisible = false
                    binding.tvMinutes.isVisible = true
                    binding.tvHours.isVisible = false
                    binding.tvMinutes.text = time2
                    binding.tvUnitTypes.text= getString(R.string.minutes)
                }
            }
            if (hours > 0) {
                val time3 = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                binding.tvSeconds.isVisible = false
                binding.tvMinutes.isVisible = false
                binding.tvHours.isVisible = true
                binding.tvHours.text = time3
                binding.tvUnitTypes.text= getString(R.string.hours)
            }
            if (timerSeconds>=359999){
                stopTimer()
                val time0 = String.format("%02d:%02d:%02d", 99, 59, 59)
                binding.tvSeconds.isVisible = false
                binding.tvMinutes.isVisible = false
                binding.tvHours.isVisible = true
                binding.tvHours.setTextColor(getColor(R.color.red))
                binding.tvHours.text = time0
                binding.tvFinDelMapa.isVisible=true
                binding.tvUnitTypes.text= getString(R.string.infinito)
            }
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCronometroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initListeners()
    }

    @SuppressLint("ClickableViewAccesability")
    private fun initListeners() {
        binding.tvUnitTypes.text = getString(R.string.seconds)
        binding.tvBTN1.text = getString(R.string.btnStart)
        binding.tvBTN2.text = getString(R.string.btnRestart)
        binding.tvSeconds.text = secondsFormatted(time)
        binding.tvLap.text = secondsFormatted(lap)
        binding.tvLogSplit.text = getString(R.string.vacio)
        binding.cvBTNBack.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnBackPressed(binding.cvBTNBack)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("kv2", "X= $x")
                        Log.i("kv2", "Y= $y")
                        if (x > 2f && x < 138f && y > -8f && y < 128f) {
                        } else {
                            if (!stateAnimator) {
                                btnBackUP(binding.cvBTNBack)
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > 2f && x < 138f && y > -8f && y < 128f && !stateAnimator) {
                            btnBackUP(binding.cvBTNBack)
                            onBackPressedDispatcher.onBackPressed()
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        binding.constBTNStart.setOnClickListener {
            if (!isBTN1){
                startTimer()
                binding.tvBTN2.text= getString(R.string.btnSplit)
                transitionColorBTNSplit(binding.constBTN2Background, binding.constShadowBTN2)
                isBTN1=true
                isBTN2= true
            }else{
                stopTimer()
                isBTN1=false
            }
            }
        binding.constBTNRestart.setOnClickListener {
            if (!isBTN2){
                resetTimer()
            }else{

            }
        }

    }

    private fun startTimer() {
        if (!isRunning) {
            handler.postDelayed(runnable, 1000)
            isRunning = true

            binding.tvBTN1.text= getString(R.string.btnStop)
            transitionColorBTNStop(binding.constBTN1Background, binding.constShadowBTN1)
        }
    }

    private fun stopTimer() {
        if (isRunning) {
            handler.removeCallbacks(runnable)
            isRunning = false

            binding.tvBTN1.text= getString(R.string.btnResume)
            transitionColorBTNStartResume(binding.constBTN1Background, binding.constShadowBTN1)
            binding.tvBTN2.text= getString(R.string.btnRestart)
            transitionColorBTNRestart(binding.constBTN2Background, binding.constShadowBTN2)
            isBTN2= false
        }
    }

    private fun resetTimer() {
        stopTimer()
        timerSeconds = 0
        binding.tvSeconds.isVisible = true
        binding.tvMinutes.isVisible = false
        binding.tvHours.isVisible = false
        binding.tvSeconds.text = secondsFormatted(timerSeconds)
        binding.tvHours.setTextColor(getColor(R.color.accent))
        binding.tvFinDelMapa.isVisible=false
        binding.tvBTN1.text= getString(R.string.btnStart)
    }

    private fun secondsFormatted(time: Int): String {
        val timeFormatted = String.format("%02d", time)
        return timeFormatted
    }

    private fun btnBackUP(view: View) {
        val increaseAnimationX = ObjectAnimator.ofFloat(view, "scaleX", 0.9f, 1.0f)
        val increaseAnimationY = ObjectAnimator.ofFloat(view, "scaleY", 0.9f, 1.0f)


        val animatorSet = AnimatorSet()
        animatorSet.apply {
            duration = 300
            interpolator = BounceInterpolator()
            doOnStart { transitionColorEnd(view) }
            playTogether(increaseAnimationX, increaseAnimationY)
            start()
        }
    }

    private fun btnBackPressed(view: View) {
        val decreaseAnimationX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.9f)

        val decreaseAnimationY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.9f)


        val animatorSet = AnimatorSet()
        animatorSet.apply {
            duration = 300
            interpolator = BounceInterpolator()
            doOnStart { transitionColorStart(view) }
            playTogether(decreaseAnimationX, decreaseAnimationY)
            start()
        }
    }
    private fun transitionColorBTNStop(view: View, view2: View){
        val colors = arrayOf(
            ColorDrawable(getColor(R.color.chimalli)),
            ColorDrawable(getColor(R.color.red))
        )
        val shadowColors = arrayOf(
            ColorDrawable(getColor(R.color.chimalli_oscuro)),
            ColorDrawable(getColor(R.color.red_dark))
        )
        val transition = TransitionDrawable(colors)
        view.background = transition
        transition.startTransition(300)
        val shadowTransition = TransitionDrawable(shadowColors)
        view2.background = shadowTransition
        shadowTransition.startTransition(300)
        view.setBackgroundColor(getColor(R.color.red))
        view2.setBackgroundColor(getColor(R.color.red_dark))
    }
    private fun transitionColorBTNStartResume(view: View, view2: View){
        val colors = arrayOf(
            ColorDrawable(getColor(R.color.red)),
            ColorDrawable(getColor(R.color.chimalli))
        )
        val shadowColors = arrayOf(
            ColorDrawable(getColor(R.color.red_dark)),
            ColorDrawable(getColor(R.color.chimalli_oscuro))
        )
        val transition = TransitionDrawable(colors)
        view.background = transition
        transition.startTransition(300)
        view.setBackgroundColor(getColor(R.color.chimalli))
        val shadowTransition = TransitionDrawable(shadowColors)
        view.background = transition
        shadowTransition.startTransition(300)
        view2.setBackgroundColor(getColor(R.color.chimalli_oscuro))
    }
    private fun transitionColorBTNSplit(view: View, view2: View){
        val colors = arrayOf(
            ColorDrawable(getColor(R.color.secundario)),
            ColorDrawable(getColor(R.color.green))
        )
        val shadowColors = arrayOf(
            ColorDrawable(getColor(R.color.secundario_oscuro)),
            ColorDrawable(getColor(R.color.green_dark))
        )
        val transition = TransitionDrawable(colors)
        view.background = transition
        transition.startTransition(300)
        view.setBackgroundColor(getColor(R.color.green))
        val shadowTransition = TransitionDrawable(shadowColors)
        view.background = transition
        shadowTransition.startTransition(300)
        view2.setBackgroundColor(getColor(R.color.green_dark))
    }
    private fun transitionColorBTNRestart(view: View, view2: View){
        val colors = arrayOf(
            ColorDrawable(getColor(R.color.green)),
            ColorDrawable(getColor(R.color.secundario))
        )
        val shadowColors = arrayOf(
            ColorDrawable(getColor(R.color.green_dark)),
            ColorDrawable(getColor(R.color.secundario_oscuro))
        )
        val transition = TransitionDrawable(colors)
        view.background = transition
        transition.startTransition(300)
        view.setBackgroundColor(getColor(R.color.secundario))
        val shadowTransition = TransitionDrawable(shadowColors)
        view.background = transition
        shadowTransition.startTransition(300)
        view2.setBackgroundColor(getColor(R.color.secundario_oscuro))
    }

    private fun transitionColorStart(view: View) {
        val colors = arrayOf(
            ColorDrawable(getColor(R.color.gray_alpha)),
            ColorDrawable(getColor(R.color.gray))
        )
        val transition = TransitionDrawable(colors)
        view.background = transition
        transition.startTransition(300)
        view.setBackgroundColor(getColor(R.color.gray))
    }

    private fun transitionColorEnd(view: View) {
        val colors = arrayOf(
            ColorDrawable(getColor(R.color.gray)),
            ColorDrawable(getColor(R.color.gray_alpha))
        )
        val transition = TransitionDrawable(colors)
        view.background = transition
        transition.startTransition(300)
        view.setBackgroundColor(getColor(R.color.gray_alpha))
    }
}