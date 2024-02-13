package com.chimallidigital.solucionint.ui.cronometro

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivityCronometroBinding
import com.chimallidigital.solucionint.domain.model.Cronometro.Splits
import com.chimallidigital.solucionint.ui.cronometro.dialogue.DialogueLogSplitAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CronometroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCronometroBinding
    private lateinit var dialogueLogSplitAdapter: DialogueLogSplitAdapter
    private val cronometroViewModel: CronometroViewModel by viewModels()
    private var time: Int = 0
    private var lap: Int = 0
    private var stateAnimator = false
    private var isRunning = false
    private var timerSeconds = 0
    private var isBTN1 = false
    private var isBTN2 = false
    private var timeSplit: String = "00:00:00"
    private var countSplit: Int = 0
    private var newlist: MutableList<Splits> = mutableListOf()
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
                    binding.tvUnitTypes.text = getString(R.string.seconds)
                    if (timerSeconds==59){
                        dissappearCronometroAnim(binding.tvSeconds)
                        dissappearCronometroAnim(binding.tvUnitTypes)
                    }
                } else {
                    val time2 = String.format("%02d:%02d", minutes, seconds)
                    if (timerSeconds==60){
                        binding.tvUnitTypes.text = getString(R.string.minutes)
                        appearCronometroAnim(binding.tvUnitTypes)
                        appearCronometroAnim(binding.tvMinutes)
                    }
                    binding.tvSeconds.isVisible=false
                    binding.tvHours.isVisible = false
                    binding.tvMinutes.isVisible=true
                    binding.tvMinutes.text = time2
                    binding.tvUnitTypes.text = getString(R.string.minutes)
                    if (timerSeconds==3599){
                        dissappearCronometroAnim(binding.tvMinutes)
                        dissappearCronometroAnim(binding.tvUnitTypes)
                    }
                }
            }
            if (hours > 0) {
                val time3 = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                if (timerSeconds==3600){
                    binding.tvUnitTypes.text = getString(R.string.hours)
                    appearCronometroAnim(binding.tvHours)
                    appearCronometroAnim(binding.tvUnitTypes)
                }
                binding.tvHours.isVisible=true
                binding.tvMinutes.isVisible=false
                binding.tvSeconds.isVisible = false
                binding.tvHours.text = time3
                binding.tvUnitTypes.text = getString(R.string.hours)
                if (timerSeconds==35998){
                    dissappearCronometroAnim(binding.tvHours)
                }
            }
            if (timerSeconds >= 359999) {
                val time0 = String.format("%02d:%02d:%02d", 99, 59, 59)
                binding.tvHours.setTextColor(getColor(R.color.red))
                binding.tvHours.text = time0
                binding.tvUnitTypes.text = getString(R.string.infinito)
                if (timerSeconds==359999){
                    appearCronometroAnim(binding.tvHours)
                    appearCronometroAnim(binding.tvFinDelMapa)
                }
                stopTimer()
                isRunning=true
                isBTN1=false
                binding.tvSeconds.isVisible = false
                binding.tvMinutes.isVisible = false
                binding.tvHours.isVisible=true
                binding.tvFinDelMapa.isVisible=true

            }
            timeSplit = String.format("%02d:%02d:%02d", hours, minutes, seconds)
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
        dialogueLogSplitAdapter = DialogueLogSplitAdapter()
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
        binding.constBTNStart.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.constBTN1Background, binding.constShadowBTN1)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("poppy_key", "X= $x")
                        Log.i("poppy_key", "Y= $y")
                        if (x > -5f && x < 682f && y > -6f && y < 402f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(binding.constBTN1Background, binding.constShadowBTN1)
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -5f && x < 682f && y > -6f && y < 402f && !stateAnimator) {
                            btnAnimation(binding.constBTN1Background, binding.constShadowBTN1)
                            if (!isBTN1) {
                                startTimer()
                                binding.tvBTN2.text = getString(R.string.btnSplit)
                                transitionColorBTNSplit(
                                    binding.constBTN2Background,
                                    binding.constShadowBTN2
                                )
                                isBTN1 = true
                                isBTN2 = true
                            } else {
                                stopTimer()
                                isBTN1 = false
                            }
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        binding.constBTNRestart.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.constBTN2Background, binding.constShadowBTN2)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("poppy_key2", "X= $x")
                        Log.i("poppy_key2", "Y= $y")
                        if (x > -5f && x < 682f && y > -6f && y < 402f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(binding.constBTN2Background, binding.constShadowBTN2)
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -5f && x < 682f && y > -6f && y < 402f && !stateAnimator) {
                            btnAnimation(binding.constBTN2Background, binding.constShadowBTN2)
                            if (!isBTN2) {
                                resetTimer()
                                countSplit = 0
                                binding.tvLap.text = secondsFormatted(countSplit)
                                binding.tvUnitTypes.text = getString(R.string.seconds)
                                binding.tvLogSplit.text = getString(R.string.vacio)
                                dialogueLogSplitAdapter.updateList(mutableListOf())
                                newlist = mutableListOf()
                                appearCronometroAnim(binding.tvSeconds)
                                appearCronometroAnim(binding.tvUnitTypes)
                                appearCronometroAnim(binding.tvLogSplit)
                                binding.tvMinutes.isVisible=false
                                binding.tvHours.isVisible=false
                                timeSplit = "00:00:00"
                                btnStart(binding.constLap, countSplit)
                                btnBackUP(binding.constLap)
                                btnEnd(binding.constLap, countSplit, true)
                            } else {
                                btnStart(binding.constLap, countSplit)
                                countSplit++
                                logSPlit(timeSplit, timerSeconds, countSplit)
                                dissappearCronometroAnim(binding.tvLogSplit)
                                binding.tvLogSplit.text = timeSplit
                                appearCronometroAnim(binding.tvLogSplit)
                                if (countSplit <= 99) {
                                    binding.tvLap.text = secondsFormatted(countSplit)
                                }
                                btnEnd(binding.constLap, countSplit, false)
                            }
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        binding.constElapsedTime.setOnClickListener { showDialogueLogSplit() }

    }
    private fun cronometroTimeAnim(view: View, view2: View){
        val appear= AnimatorInflater.loadAnimator(
            view.context,
            R.animator.cronometro_appear
        ) as AnimatorSet
        val dissappear= AnimatorInflater.loadAnimator(
            view.context,
            R.animator.cronometro_dissapear
        ) as AnimatorSet
        appear.apply {
            setTarget(view2)
            doOnStart { view2.isVisible=true }
        }
        dissappear.apply {
            setTarget(view)
            doOnEnd { view.isVisible=false }
        }
        val animatorset= AnimatorSet()
        animatorset.apply {
            playSequentially(dissappear, appear)
            start()
        }
    }
    private fun appearCronometroAnim(view: View){
        val appear= AnimatorInflater.loadAnimator(
            view.context,
            R.animator.cronometro_appear
        ) as AnimatorSet
        appear.apply {
            setTarget(view)
            doOnStart {
                view.isVisible=true
            }
            start()
        }
    }
    private fun dissappearCronometroAnim(view: View){
        val dissappear= AnimatorInflater.loadAnimator(
            view.context,
            R.animator.cronometro_dissapear
        ) as AnimatorSet
        dissappear.apply {
            doOnEnd { view.isInvisible }
            setTarget(view)
            start()
        }
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

    private fun showDialogueLogSplit() {
        val dialogo = Dialog(this)
        dialogo.setContentView(R.layout.dialogue_log_split)

        val btnDLSBack: ImageView = dialogo.findViewById(R.id.btnDLSBack)
        val rvDialogueLogSplit: RecyclerView = dialogo.findViewById(R.id.rvDialogueLogSplit)

        btnDLSBack.setOnClickListener { dialogo.hide() }
        rvDialogueLogSplit.apply {
            layoutManager = LinearLayoutManager(rvDialogueLogSplit.context)
            adapter = dialogueLogSplitAdapter
        }
        dialogo.show()
    }

    private fun logSPlit(totalTime: String, timerSeconds: Int, countSplit: Int) {
        var elapsedTime: String
        val hours = timerSeconds / 3600
        val minutes = (timerSeconds % 3600) / 60
        val seconds = timerSeconds % 60

        var timerSecondsSave = timerSeconds
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cronometroViewModel.splits.collect {
                    if (newlist.isNotEmpty()) {
                        val timerSecondsValue = newlist.get(newlist.size - 1).timerSeconds
                        timerSecondsSave = timerSecondsSave - timerSecondsValue
                        val hours2 = timerSecondsSave / 3600
                        val minutes2 = (timerSecondsSave % 3600) / 60
                        val seconds2 = timerSecondsSave % 60
                        Log.i("poppy", timerSecondsSave.toString())
                        elapsedTime = String.format("%02d:%02d:%02d", hours2, minutes2, seconds2)
                    } else {
                        elapsedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    }
                    newlist.add(
                        Splits(
                            elapsedTime = elapsedTime,
                            totalTime = totalTime,
                            timerSeconds = timerSeconds,
                            countSplit = countSplit
                        )
                    )
                    Log.i("poppy", newlist.toString())
                    dialogueLogSplitAdapter.updateList(newlist)
                }
            }
        }
    }

    private fun startTimer() {
        if (!isRunning) {
            handler.postDelayed(runnable, 1000)
            isRunning = true

            binding.tvBTN1.text = getString(R.string.btnStop)
            transitionColorBTNStop(binding.constBTN1Background, binding.constShadowBTN1)
        }
    }

    private fun stopTimer() {
        if (isRunning) {
            handler.removeCallbacks(runnable)
            isRunning = false

            binding.tvBTN1.text = getString(R.string.btnResume)
            transitionColorBTNStartResume(binding.constBTN1Background, binding.constShadowBTN1)
            binding.tvBTN2.text = getString(R.string.btnRestart)
            transitionColorBTNRestart(binding.constBTN2Background, binding.constShadowBTN2)
            isBTN2 = false
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
        binding.tvFinDelMapa.isVisible = false
        binding.tvBTN1.text = getString(R.string.btnStart)
    }

    private fun secondsFormatted(time: Int): String {
        val timeFormatted = String.format("%02d", time)
        return timeFormatted
    }

    private fun btnStart(view: View, num: Int) {
        val decreaseAnimationX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.9f)

        val decreaseAnimationY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.9f)


        val animatorSet = AnimatorSet()
        animatorSet.apply {
            duration = 300
            interpolator = BounceInterpolator()
            doOnStart {
                if (num == 0) {
                    transitionColorGrayTOChimalli(view)
                } else {
                    transitionColorSecundarioTOChimalli(view)
                }
            }
            playTogether(decreaseAnimationX, decreaseAnimationY)
            start()
        }
    }

    private fun btnEnd(view: View, num: Int, status: Boolean) {
        val increaseAnimationX = ObjectAnimator.ofFloat(view, "scaleX", 0.9f, 1.0f)
        val increaseAnimationY = ObjectAnimator.ofFloat(view, "scaleY", 0.9f, 1.0f)


        val animatorSet = AnimatorSet()
        animatorSet.apply {
            duration = 300
            interpolator = BounceInterpolator()
            doOnStart {
                if (status) {
                    transitionColorChimalliTOGray(view)
                } else {
                    if (num <= 99) {
                        transitionColorChimalliTOSecundario(view)
                    } else {
                        transitionColorChimalliTORed(view)
                    }
                }
            }
            playTogether(increaseAnimationX, increaseAnimationY)
            start()
        }
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

    private fun transitionColorBTNStop(view: View, view2: View) {
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

    private fun transitionColorBTNStartResume(view: View, view2: View) {
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

    private fun transitionColorBTNSplit(view: View, view2: View) {
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

    private fun transitionColorBTNRestart(view: View, view2: View) {
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

    private fun transitionColorGrayTOChimalli(view: View) {
        val colors = arrayOf(
            ColorDrawable(getColor(R.color.gray)),
            ColorDrawable(getColor(R.color.chimalli))
        )
        val transition = TransitionDrawable(colors)
        view.background = transition
        transition.startTransition(400)
        view.setBackgroundColor(getColor(R.color.chimalli))
    }

    private fun transitionColorSecundarioTOChimalli(view: View) {
        val colors = arrayOf(
            ColorDrawable(getColor(R.color.secundario)),
            ColorDrawable(getColor(R.color.chimalli))
        )
        val transition = TransitionDrawable(colors)
        view.background = transition
        transition.startTransition(400)
        view.setBackgroundColor(getColor(R.color.chimalli))
    }

    private fun transitionColorChimalliTORed(view: View) {
        val colors = arrayOf(
            ColorDrawable(getColor(R.color.chimalli)),
            ColorDrawable(getColor(R.color.red))
        )
        val transition = TransitionDrawable(colors)
        view.background = transition
        transition.startTransition(400)
        view.setBackgroundColor(getColor(R.color.red))
    }

    private fun transitionColorChimalliTOSecundario(view: View) {
        val colors = arrayOf(
            ColorDrawable(getColor(R.color.chimalli)),
            ColorDrawable(getColor(R.color.secundario))
        )
        val transition = TransitionDrawable(colors)
        view.background = transition
        transition.startTransition(400)
        view.setBackgroundColor(getColor(R.color.secundario))
    }

    private fun transitionColorChimalliTOGray(view: View) {
        val colors = arrayOf(
            ColorDrawable(getColor(R.color.chimalli)),
            ColorDrawable(getColor(R.color.gray))
        )
        val transition = TransitionDrawable(colors)
        view.background = transition
        transition.startTransition(400)
        view.setBackgroundColor(getColor(R.color.gray))
    }
}