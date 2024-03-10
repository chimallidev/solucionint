package com.chimallidigital.solucionint.ui.tabata_timer_playing

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
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
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivityTabataTimerPlayingBinding
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.CICLOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.CONJUNTOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.ITEMS_LIST
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TIEMPO_TOTAL
import com.chimallidigital.solucionint.domain.model.TabataTimer.ItemsCollection
import com.chimallidigital.solucionint.ui.home.MainActivity

class TabataTimerPlayingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTabataTimerPlayingBinding
    private var itemsList: List<ItemsCollection> = emptyList()
    private var position = 0
    private var countCiclos: Int = 0
    private var countConjuntos: Int = 0
    private var tiempoTotal: Int = 0
    private var countCiclos00: Int = 0
    private var countConjuntos00: Int = 1
    private var timerSeconds: Int = 0
    private var isRunning = false
    private var btn00State = false
    private var stateAnimator = false
    var mMediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            if (timerSeconds != 0) {
                timerSeconds--
                tiempoTotal--

                val hours = timerSeconds / 3600
                val minutes = (timerSeconds % 3600) / 60
                val seconds = timerSeconds % 60
                progressBar(itemsList.get(position - 1).tiempo, timerSeconds)
                binding.tvTiempoTotal.text = formatTotalTime(tiempoTotal)
                if (hours == 0) {
                    if (minutes == 0) {
                        binding.tvSegundos.isVisible = true
                        binding.tvMinutos.isVisible = false
                        binding.tvHoras.isVisible = false
                        binding.tvSegundos.text = formatZeroZero(seconds)
                        when (timerSeconds) {
                            1 -> {
                                binding.tvSegundos.setTextColor(getColor(R.color.red))
                                stopSound()
                                playSound(R.raw.robot_saying_1)
                            }

                            2 -> {
                                binding.tvSegundos.setTextColor(getColor(R.color.red))
                                stopSound()
                                playSound(R.raw.robot_saying_2)
                            }

                            3 -> {
                                binding.tvSegundos.setTextColor(getColor(R.color.red))
                                stopSound()
                                playSound(R.raw.robot_saying_3)
                            }
                        }
                        if (timerSeconds == 0) {
                            if (position != itemsList.size - 1) {
                                val timerBetween = itemsList.get(position).tiempo
                                val hours2 = timerBetween / 3600
                                val minutes2 = (timerBetween % 3600) / 60
                                val seconds2 = timerBetween % 60
                                if (hours2 == 0) {
                                    if (minutes2 == 0) {
                                        binding.tvSegundos.isVisible = true
                                        binding.tvMinutos.isVisible = false
                                        binding.tvHoras.isVisible = false
                                        binding.tvSegundos.text = formatZeroZero(seconds2)
                                        progressBar(timerBetween, timerBetween)
                                    } else {
                                        binding.tvSegundos.isVisible = false
                                        binding.tvMinutos.isVisible = true
                                        binding.tvHoras.isVisible = false
                                        binding.tvMinutos.text = formatMinutes(minutes2, seconds2)
                                        progressBar(timerBetween, timerBetween)
                                    }
                                }
                                if (hours2 > 0) {
                                    binding.tvSegundos.isVisible = false
                                    binding.tvMinutos.isVisible = false
                                    binding.tvHoras.isVisible = true
                                    binding.tvHoras.text = formatTotalTime(timerBetween)
                                    progressBar(timerBetween, timerBetween)
                                }
                                vibration(itemsList.get(position).vibration)
                                stopSound()
                                if (itemsList.get(position).track != -1) {
                                    playSound(itemsList.get(position).track)
                                }
                                playList(position)
                            } else {
                                val intent = Intent(
                                    this@TabataTimerPlayingActivity,
                                    FinishTabataTimerActivity::class.java
                                )
                                startActivity(intent)
                            }
                        }
                    } else {
                        binding.tvSegundos.isVisible = false
                        binding.tvMinutos.isVisible = true
                        binding.tvHoras.isVisible = false
                        binding.tvMinutos.text = formatMinutes(minutes, seconds)
                    }
                }
                if (hours > 0) {
                    binding.tvSegundos.isVisible = false
                    binding.tvMinutos.isVisible = false
                    binding.tvHoras.isVisible = true
                    binding.tvHoras.text = formatTotalTime(timerSeconds)
                }
            }
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTabataTimerPlayingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Your logic here
        timerSeconds = 0
        val intent = Intent(this, FinishTabataTimerActivity::class.java)
        startActivity(intent)
    }

    private fun initUI() {
        recoveryItemsList()
        initListeners()
        waitOnesecond()
        playList(position)
    }

    private fun initListeners() {
        binding.tvCiclosContador01.text = formatZeroZero(countCiclos)
        binding.tvConjuntosContador01.text = formatZeroZero(countConjuntos)
        binding.tvCiclosContador00.text = formatZeroZero(countCiclos00)
        binding.tvConjuntosContador00.text = formatZeroZero(countConjuntos00)

//        binding.BTNFinalizar.setOnClickListener {
//            timerSeconds = 0
//            val intent = Intent(this, FinishTabataTimerActivity::class.java)
//            startActivity(intent)
//        }
        binding.BTNPausar.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.backgroundBTNPausar, binding.shadowBTNPausar)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("tabata_playing_pausar", "X= $x")
                        Log.i("tabata_playing_pausar", "Y= $y")
                        if (x > -4f && x < 392f && y > -2f && y < 100f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(
                                    binding.backgroundBTNPausar, binding.shadowBTNPausar
                                )
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -4f && x < 392f && y > -2f && y < 100f && !stateAnimator) {
                            btnAnimation(binding.backgroundBTNPausar, binding.shadowBTNPausar)
                            if (btn00State) {
                                startTimer()
                                btn00State = false
                                binding.tvBTNPausar.text = getString(R.string.pausar)
                                transitionSecundarioTOGreen(
                                    binding.backgroundBTNPausar,
                                    binding.shadowBTNPausar
                                )
                            } else {
                                stopTimer()
                                btn00State = true
                                binding.tvBTNPausar.text = getString(R.string.continuar)
                                transitionGreenTOSecundario(
                                    binding.backgroundBTNPausar,
                                    binding.shadowBTNPausar
                                )
                            }
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        binding.BTNFinalizar.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.backgroundBTNFinalizar, binding.shadowBTNFinalizar)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("tabata_playing_Finalizar", "X= $x")
                        Log.i("tabata_playing_Finalizar", "Y= $y")
                        if (x > -4f && x < 392f && y > -2f && y < 100f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(
                                    binding.backgroundBTNFinalizar, binding.shadowBTNFinalizar
                                )
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -4f && x < 392f && y > -2f && y < 100f && !stateAnimator) {
                            btnAnimation(binding.backgroundBTNFinalizar, binding.shadowBTNFinalizar)
                            timerSeconds = 0
                            val intent = Intent(binding.BTNFinalizar.context, FinishTabataTimerActivity::class.java)
                            startActivity(intent)
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })

    }

    private fun recoveryItemsList() {
        val list = intent.extras
        itemsList = list?.get(ITEMS_LIST) as ArrayList<ItemsCollection>
        Log.i("KatyushaTabataList", "Lista recuperada: $itemsList")
        countCiclos = intent.getIntExtra(CICLOS, 0)
        countConjuntos = intent.getIntExtra(CONJUNTOS, 0)
        tiempoTotal = intent.getIntExtra(TIEMPO_TOTAL, 0)
    }

    private fun formatZeroZero(value: Int): String {
        val tiempo = String.format("%02d", value)
        return tiempo
    }

    private fun formatMinutes(minutes: Int, seconds: Int): String {
        val time = String.format("%02d:%02d", minutes, seconds)
        return time
    }

    private fun formatTotalTime(tiempo: Int): String {
        val hours = tiempo / 3600
        val minutes = (tiempo % 3600) / 60
        val seconds = tiempo % 60

        val time = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        return time
    }

    private fun startTimer() {
        if (timerSeconds != 0) {
            if (!isRunning) {
                handler.postDelayed(runnable, 1000)
                isRunning = true

            }
        }
    }

    private fun stopTimer() {
        if (isRunning) {
            handler.removeCallbacks(runnable)
            isRunning = false
        }
    }

    private fun playList(pos: Int) {
        position = pos
        Log.i("position_playTabata", "posición: $position")
        when (itemsList.get(position).tipo) {
            "preparación" -> {
                binding.tvName.setTextColor(getColor(R.color.bronce))
                binding.tvHoras.setTextColor(getColor(R.color.bronce))
                binding.tvMinutos.setTextColor(getColor(R.color.bronce))
                binding.tvSegundos.setTextColor(getColor(R.color.bronce))
                binding.cvTableroFondo.background =
                    AppCompatResources.getDrawable(binding.cvTableroFondo.context, R.color.bronce)
                binding.cvTableroFondo2.background =
                    AppCompatResources.getDrawable(binding.cvTableroFondo2.context, R.color.bronce)
            }

            "ejercicio" -> {
                binding.tvName.setTextColor(getColor(R.color.secundario))
                binding.tvHoras.setTextColor(getColor(R.color.secundario))
                binding.tvMinutos.setTextColor(getColor(R.color.secundario))
                binding.tvSegundos.setTextColor(getColor(R.color.secundario))
                binding.cvTableroFondo.background =
                    AppCompatResources.getDrawable(binding.cvTableroFondo.context, R.color.accent)
                binding.cvTableroFondo2.background =
                    AppCompatResources.getDrawable(binding.cvTableroFondo2.context, R.color.accent)
            }

            "descanso" -> {
                binding.tvName.setTextColor(getColor(R.color.chimalli))
                binding.tvHoras.setTextColor(getColor(R.color.chimalli))
                binding.tvMinutos.setTextColor(getColor(R.color.chimalli))
                binding.tvSegundos.setTextColor(getColor(R.color.chimalli))
                binding.cvTableroFondo.background =
                    AppCompatResources.getDrawable(binding.cvTableroFondo.context, R.color.chimalli)
                binding.cvTableroFondo2.background = AppCompatResources.getDrawable(
                    binding.cvTableroFondo2.context,
                    R.color.chimalli
                )
            }

            "descanso entre conjuntos" -> {
                binding.tvName.setTextColor(getColor(R.color.dorado))
                binding.tvHoras.setTextColor(getColor(R.color.dorado))
                binding.tvMinutos.setTextColor(getColor(R.color.dorado))
                binding.tvSegundos.setTextColor(getColor(R.color.dorado))
                binding.cvTableroFondo.background =
                    AppCompatResources.getDrawable(binding.cvTableroFondo.context, R.color.dorado)
                binding.cvTableroFondo2.background =
                    AppCompatResources.getDrawable(binding.cvTableroFondo2.context, R.color.dorado)
            }
        }
        if (position < itemsList.size) {
            timerSeconds = itemsList.get(position).tiempo
            binding.tvNumber.text = formatZeroZero(position + 1)
            binding.tvName.text = itemsList.get(position).tipo
            startTimer()
        }
        if (position == itemsList.size) {

        }
        if (itemsList.get(position).tipo == "ejercicio") {
            countCiclos00++
            binding.tvCiclosContador00.text = formatZeroZero(countCiclos00)
        }
        if (position != 0 && itemsList.get(position - 1).tipo == "descanso entre conjuntos") {
            countConjuntos00++
            countCiclos00 = 1
            binding.tvCiclosContador00.text = formatZeroZero(countCiclos00)
            binding.tvConjuntosContador00.text = formatZeroZero(countConjuntos00)
        }
        binding.tvTituloProximo.text = itemsList.get(position + 1).tipo
        position++
    }

    private fun waitOnesecond() {
        val runnable2 = object : Runnable {
            override fun run() {
                handler.postDelayed(this, 1000)
            }
        }
        handler.postDelayed(runnable2, 1000)
        handler.removeCallbacks(runnable2)
        val timerBetween = itemsList.get(position).tiempo
        val hours3 = timerBetween / 3600
        val minutes3 = (timerBetween % 3600) / 60
        val seconds3 = timerBetween % 60
        if (hours3 == 0) {
            if (minutes3 == 0) {
                binding.tvSegundos.isVisible = true
                binding.tvMinutos.isVisible = false
                binding.tvHoras.isVisible = false
                binding.tvSegundos.text = formatZeroZero(seconds3)
            } else {
                binding.tvSegundos.isVisible = false
                binding.tvMinutos.isVisible = true
                binding.tvHoras.isVisible = false
                binding.tvMinutos.text = formatMinutes(minutes3, seconds3)
            }
        }
        if (hours3 > 0) {
            binding.tvSegundos.isVisible = false
            binding.tvMinutos.isVisible = false
            binding.tvHoras.isVisible = true
            binding.tvHoras.text = formatTotalTime(timerBetween)
        }
    }

    fun progressBar(maxTime: Int, time: Int) {
        val timeBar = binding.pbTime
        Log.i("tiempoPB", "tiempo: $maxTime")
        timeBar.max = maxTime
        timeBar.progress = time
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

    private fun transitionSecundarioTOGreen(view: View, view2: View) {
        val colors = arrayOf(
            ColorDrawable(getColor(R.color.secundario)),
            ColorDrawable(getColor(R.color.green))
        )
        val colorsShadow = arrayOf(
            ColorDrawable(getColor(R.color.secundario_oscuro)),
            ColorDrawable(getColor(R.color.green_dark))
        )
        val transition = TransitionDrawable(colors)
        view.background = transition
        transition.startTransition(500)
        view.setBackgroundColor(getColor(R.color.green))

        val transition2 = TransitionDrawable(colorsShadow)
        view2.background = transition
        transition2.startTransition(500)
        view2.setBackgroundColor(getColor(R.color.green_dark))
    }

    private fun transitionGreenTOSecundario(view: View, view2: View) {
        val colors = arrayOf(
            ColorDrawable(getColor(R.color.green)),
            ColorDrawable(getColor(R.color.secundario))
        )
        val colorsShadow = arrayOf(
            ColorDrawable(getColor(R.color.green_dark)),
            ColorDrawable(getColor(R.color.secundario_oscuro))
        )
        val transition = TransitionDrawable(colors)
        view.background = transition
        transition.startTransition(500)
        view.setBackgroundColor(getColor(R.color.secundario))

        val transition2 = TransitionDrawable(colorsShadow)
        view2.background = transition
        transition2.startTransition(500)
        view2.setBackgroundColor(getColor(R.color.secundario_oscuro))
    }
}
