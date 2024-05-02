package com.chimallidigital.solucionint.ui.temporizador

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivityTemporizadorBinding
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.COUNT_TEMP_ADS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.SONIDO_TEMPORIZADOR
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TRACK_TEMPORIZADOR
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.VIBRACION_TEMPORIZADOR
import com.chimallidigital.solucionint.domain.model.Settings.SettingsModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Settings")

@AndroidEntryPoint
class TemporizadorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTemporizadorBinding
    private var intersticial: InterstitialAd?= null

    private var countAds= 2
    private var stateAnimator = false
    private var timerSeconds = 0
    private var contSegundos = 0
    private var contMinutos = 0
    private var contHoras = 0
    private var isRunning = false
    private var isBTN1 = false
    private var isFinishState = true
    private var switchState = false
    private var lastCount = false
    private var firstTime = true
    var savePosition: Int = 0
    var track: Int = -1
    var mMediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            if (timerSeconds != 0) {
                timerSeconds--

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
                        if (timerSeconds == 59) {
                            appearCronometroAnim(binding.tvSeconds)
                            appearCronometroAnim(binding.tvUnitTypes)
                        }
                    } else {
                        val time = String.format("%02d:%02d", minutes, seconds)

                        if (timerSeconds == 3599) {
                            appearCronometroAnim(binding.tvMinutes)
                            appearCronometroAnim(binding.tvUnitTypes)
                        }
                        binding.tvSeconds.isVisible = false
                        binding.tvMinutes.isVisible = true
                        binding.tvHours.isVisible = false
                        binding.tvMinutes.text = time
                        binding.tvUnitTypes.text = getString(R.string.minutes)
                        if (timerSeconds == 60) {
                            binding.tvUnitTypes.text = getString(R.string.minutes)
                            dissappearCronometroAnim(binding.tvUnitTypes)
                            dissappearCronometroAnim(binding.tvMinutes)
                        }
                    }
                }
                if (hours > 0) {
                    val time2 = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    binding.tvHours.isVisible = true
                    binding.tvMinutes.isVisible = false
                    binding.tvSeconds.isVisible = false
                    binding.tvHours.text = time2
                    binding.tvUnitTypes.text = getString(R.string.hours)
                    if (timerSeconds == 3600) {
                        dissappearCronometroAnim(binding.tvHours)
                        dissappearCronometroAnim(binding.tvUnitTypes)
                    }
                }
                when (timerSeconds) {
                    3 -> {
                        playSound(R.raw.robot_saying_3)
                        if (!lastCount) {
                            binding.tvSeconds.setTextColor(getColor(R.color.red))
                            lastCount = true
                        }
                    }

                    2 -> {
                        playSound(R.raw.robot_saying_2)
                        if (!lastCount) {
                            binding.tvSeconds.setTextColor(getColor(R.color.red))
                            lastCount = true
                        }
                    }

                    1 -> {
                        playSound(R.raw.robot_saying_1)
                        if (!lastCount) {
                            binding.tvSeconds.setTextColor(getColor(R.color.red))
                            lastCount = true
                        }
                    }
                }
            } else {
                if (!isFinishState) {
                    if (lastCount) {
                        binding.tvSeconds.setTextColor(getColor(R.color.accent))
                        lastCount = false
                    }
                    if (track != -1) {
                        playSound(track)
                        vibration(switchState)
                    }
                    resetTimer()
                    countAds+=1
                    CoroutineScope(Dispatchers.IO).launch {
                        guardarIntPreferencias(COUNT_TEMP_ADS, countAds)
                    }
                    contadorAds()
                    isFinishState = true
                }
            }
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTemporizadorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAds()
        stateSettings()
        initUI()
    }
    private fun initAds(){
        var adRequest: AdRequest = AdRequest.Builder().build()

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, object: InterstitialAdLoadCallback(){
            override fun onAdLoaded(intersticialAd: InterstitialAd) {
                intersticial= intersticialAd
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                intersticial= null
            }
        })
    }
    fun showAds(){
        intersticial?.show(this)
    }
    private fun contadorAds(){
        if(countAds==3){
            showAds()
            countAds=0
            CoroutineScope(Dispatchers.IO).launch {
                guardarIntPreferencias(COUNT_TEMP_ADS, countAds)
            }
            initAds()
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private suspend fun guardarPreferencias(key: String, value: Boolean) {
        dataStore.edit { Preferencias ->
            Preferencias[booleanPreferencesKey(key)] = value
        }
    }

    private suspend fun guardarIntPreferencias(key: String, value: Int) {
        dataStore.edit { Preferencias ->
            Preferencias[intPreferencesKey(key)] = value
        }
    }

    private fun stateSettings() {
        CoroutineScope(Dispatchers.IO).launch {
            getSettings().filter { firstTime }.collectLatest { settingsModel ->
                if (settingsModel != null) {
                    runOnUiThread {
                        switchState = settingsModel.vibracion_temporizador
                        savePosition = settingsModel.sonido_temporizador
                        track = settingsModel.track_temporizador
                        countAds= settingsModel.count_temp_ads
                        firstTime= !firstTime
                    }
                }
            }
        }
    }

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
        intersticial?.fullScreenContentCallback= object: FullScreenContentCallback(){
            override fun onAdDismissedFullScreenContent() {
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            }

            override fun onAdShowedFullScreenContent() {
                intersticial= null
            }
        }
        binding.constBack.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnBackPressed(binding.constBack)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("kv3", "X= $x")
                        Log.i("kv3", "Y= $y")
                        if (x > -2f && x < 118f && y > -10f && y < 118f) {
                        } else {
                            if (!stateAnimator) {
                                btnBackUP(binding.constBack)
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -2f && x < 118f && y > -10f && y < 118f && !stateAnimator) {
                            btnBackUP(binding.constBack)
                            onBackPressedDispatcher.onBackPressed()
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        binding.tvSeconds.text = secondsFormatted(timerSeconds)
        binding.tvUnitTypes.text = getString(R.string.seconds)
        binding.tvBTNStart.text = getString(R.string.btnStart)
        binding.tvBTNRestart.text = getString(R.string.btnRestart)
        binding.BTNAjustes.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnAjustesPressed(binding.BTNAjustes)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("ajustes", "X= $x")
                        Log.i("ajustes", "Y= $y")
                        if (x > 18f && x < 129f && y > 2f && y < 122f) {
                        } else {
                            if (!stateAnimator) {
                                btnAjustesUP(binding.BTNAjustes)
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > 18f && x < 129f && y > 2f && y < 122f && !stateAnimator) {
                            btnAjustesUP(binding.BTNAjustes)
                            showDialogueAjustes()
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        binding.BTNStart.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.backgroundBTNStart, binding.shadowBTNStart)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("start_temporizador", "X= $x")
                        Log.i("start_temporizador", "Y= $y")
                        if (x > -3f && x < 450f && y > -9f && y < 106f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(binding.backgroundBTNStart, binding.shadowBTNStart)
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -3f && x < 450f && y > -9f && y < 106f && !stateAnimator) {
                            btnAnimation(binding.backgroundBTNStart, binding.shadowBTNStart)
                            if (!isBTN1) {
                                startTimer()
                                isBTN1 = true
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
        binding.BTNRestart.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.backgroundBTNRestart, binding.shadowBTNRestart)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("restart_temporizador", "X= $x")
                        Log.i("restart_temporizador", "Y= $y")
                        if (x > -5f && x < 420f && y > -9f && y < 106f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(binding.backgroundBTNRestart, binding.shadowBTNRestart)
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -5f && x < 420f && y > -9f && y < 106f && !stateAnimator) {
                            btnAnimation(binding.backgroundBTNRestart, binding.shadowBTNRestart)
                            resetTimer()
                            appearCronometroAnim(binding.tvSeconds)
                            appearCronometroAnim(binding.tvUnitTypes)
                            if (lastCount) {
                                binding.tvSeconds.setTextColor(getColor(R.color.accent))
                                lastCount = false
                            }
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
    }

    private fun startTimer() {
        if (timerSeconds != 0) {
            if (!isRunning) {
                handler.postDelayed(runnable, 1000)
                isRunning = true
                isFinishState = false
                binding.tvBTNStart.text = getString(R.string.btnStop)
            }
        } else {
            Toast.makeText(this, "Asigna un tiempo en el boton AJUSTES", Toast.LENGTH_LONG).show()
        }
    }

    private fun stopTimer() {
        if (isRunning) {
            handler.removeCallbacks(runnable)
            isRunning = false
            isFinishState = true
            binding.tvBTNStart.text = getString(R.string.btnResume)
        }
    }

    private fun resetTimer() {
        stopTimer()
        timerSeconds = 0
        binding.tvSeconds.isVisible = true
        binding.tvMinutes.isVisible = false
        binding.tvHours.isVisible = false
        binding.tvUnitTypes.text = getString(R.string.seconds)
        binding.tvSeconds.text = secondsFormatted(timerSeconds)
        binding.tvBTNStart.text = getString(R.string.btnStart)
        isBTN1 = false
    }

    private fun showDialogueAjustes() {
        val dialogo = Dialog(this)
        dialogo.setContentView(R.layout.dialogue_temporizador_ajustes)

        val hours = resources.getStringArray(R.array.Hours)
        val minutes = resources.getStringArray(R.array.Minutos)
        val seconds = resources.getStringArray(R.array.Segundos)
        val sonidos = resources.getStringArray(R.array.Sonidos)

        val spinnerHours: Spinner = dialogo.findViewById(R.id.spinnerHours)
        val spinnerMinutes: Spinner = dialogo.findViewById(R.id.spinnerMinutes)
        val spinnerSeconds: Spinner = dialogo.findViewById(R.id.spinnerSeconds)
        val spinnerSonido: Spinner = dialogo.findViewById(R.id.spinnerSonido)
        val btnClose: ImageView = dialogo.findViewById(R.id.btnClose)
        val btnAceptar: ConstraintLayout = dialogo.findViewById(R.id.BTNAceptar)
        val btnPlaySonido: ConstraintLayout = dialogo.findViewById(R.id.BTNPlaySonido)
        val switch1: SwitchCompat = dialogo.findViewById(R.id.switch1)
        val backgroundPlaySonido: ConstraintLayout =
            dialogo.findViewById(R.id.backgroundBTNPlaySonido)
        val shadowPlaySonido: ConstraintLayout = dialogo.findViewById(R.id.shadowBTNPlaySonido)
        val backgroundBTNAceptar: ConstraintLayout = dialogo.findViewById(R.id.backgroundBTNAceptar)
        val shadowBTNAceptar: ConstraintLayout = dialogo.findViewById(R.id.shadowBTNAceptar)

        initSpinner(spinnerHours, hours, 2)
        initSpinner(spinnerMinutes, minutes, 1)
        initSpinner(spinnerSeconds, seconds, 0)
        initSpinnerPlaySonido(spinnerSonido, sonidos)

        if (switchState) {
            switch1.isChecked = true
        } else {
            switch1.isChecked = false
        }

        switch1.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (b) {
                switchState = true
            } else {
                switchState = false
            }
            CoroutineScope(Dispatchers.IO).launch {
                guardarPreferencias(VIBRACION_TEMPORIZADOR, switchState)
            }
        }
        btnPlaySonido.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(backgroundPlaySonido, shadowPlaySonido)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("playsonido_temp", "X= $x")
                        Log.i("playsonido_temp", "Y= $y")
                        if (x > -4f && x < 76f && y > -6f && y < 74f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(backgroundPlaySonido, shadowPlaySonido)
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -4f && x < 76f && y > -6f && y < 74f && !stateAnimator) {
                            btnAnimation(backgroundPlaySonido, shadowPlaySonido)
                            if (track != -1) {
                                Log.i("sound", "$track")
                                stopSound()
                                playSound(track)
                            }
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        btnClose.setOnClickListener { dialogo.hide() }
        btnAceptar.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(backgroundBTNAceptar, shadowBTNAceptar)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("aceptar_temp", "X= $x")
                        Log.i("aceptar_temp", "Y= $y")
                        if (x > -3f && x < 521f && y > -12f && y < 112f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(backgroundBTNAceptar, shadowBTNAceptar)
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -3f && x < 521f && y > -12f && y < 112f && !stateAnimator) {
                            btnAnimation(backgroundBTNAceptar, shadowBTNAceptar)
                            val saveTimerSeconds: Int
                            saveTimerSeconds = timerSeconds
                            resetTimer()
                            handler.removeCallbacks(runnable)
                            timerSeconds = 0
                            timerSeconds = contSegundos + contMinutos + contHoras
                            Log.i("cont_temp", "$timerSeconds")
                            if (timerSeconds != 0) {
                                val hours2 = timerSeconds / 3600
                                val minutes2 = (timerSeconds % 3600) / 60
                                val seconds2 = timerSeconds % 60

                                if (hours2 == 0) {
                                    if (minutes2 == 0) {
                                        binding.tvSeconds.isVisible = true
                                        binding.tvMinutes.isVisible = false
                                        binding.tvHours.isVisible = false
                                        binding.tvSeconds.text = secondsFormatted(seconds2)
                                        binding.tvUnitTypes.text = getString(R.string.seconds)
                                    } else {
                                        val time = String.format("%02d:%02d", minutes2, seconds2)
                                        binding.tvSeconds.isVisible = false
                                        binding.tvMinutes.isVisible = true
                                        binding.tvHours.isVisible = false
                                        binding.tvMinutes.text = time
                                        binding.tvUnitTypes.text = getString(R.string.minutes)
                                    }
                                }
                                if (hours2 > 0) {
                                    val time2 =
                                        String.format("%02d:%02d:%02d", hours2, minutes2, seconds2)
                                    binding.tvHours.isVisible = true
                                    binding.tvMinutes.isVisible = false
                                    binding.tvSeconds.isVisible = false
                                    binding.tvHours.text = time2
                                    binding.tvUnitTypes.text = getString(R.string.hours)
                                }
                            } else {
                                timerSeconds = saveTimerSeconds
                            }
                            contSegundos = 0
                            contHoras = 0
                            contMinutos = 0
                            dialogo.hide()
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        dialogo.show()
    }

    private fun playSound(sonido: Int) {
//        if (mMediaPlayer == null) {

        mMediaPlayer = MediaPlayer.create(this, sonido)
        mMediaPlayer!!.isLooping = false
        mMediaPlayer!!.start()
//        } else mMediaPlayer!!.start()
    }

    fun stopSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    private fun initSpinnerPlaySonido(spinner: Spinner, sonidos: Array<String>) {

        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sonidos)
            adapter.setDropDownViewResource(R.layout.spinner_list)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    if (sonidos[position] != "Ninguno") {
                        track = when (sonidos[position]) {
                            "Analog Warm Pluck" -> R.raw.analog_warm_pluck
                            "Big Impact" -> R.raw.big_impact
                            "Blaster 2" -> R.raw.blaster_2
                            "Brass Fail 1 c" -> R.raw.brass_fail_1_c
                            "Cartoon Jump" -> R.raw.cartoon_jump
                            "Copper Bell Ding 3" -> R.raw.copper_bell_ding_3
                            "Cute Level Up 3" -> R.raw.cute_level_up_3
                            "Digital Alarm Clock" -> R.raw.digital_alarm_clock
                            "Epic Extreme Hit" -> R.raw.epic_extreme_hit
                            "Epic Hybrid Logo" -> R.raw.epic_hybrid_logo
                            "Epic Logo" -> R.raw.epic_logo
                            "Error 2" -> R.raw.error_2
                            "Error When Entering The Game Menu" -> R.raw.error_when_entering_the_game_menu
                            "Fail" -> R.raw.fail
                            "Failure 1" -> R.raw.failure_1
                            "Fight Deep Voice" -> R.raw.fight_deep_voice
                            "Friend Request" -> R.raw.friend_request
                            "Game Bonus" -> R.raw.game_bonus
                            "Game Level Complete" -> R.raw.game_level_complete
                            "Game Over Arcade" -> R.raw.game_over_arcade
                            "Game Start" -> R.raw.game_start
                            "Going To The Next Level" -> R.raw.going_to_the_next_level
                            "Grand Final Orchestral Tutti" -> R.raw.grand_final_orchestral_tutti
                            "Growling Zombie" -> R.raw.growling_zombie
                            "Interface Welcome" -> R.raw.interface_welcome
                            "Invalid Selection" -> R.raw.invalid_selection
                            "Level Up Bonus Sequence 2" -> R.raw.level_up_bonus_sequence_2
                            "Level Up Bonus Sequence 3" -> R.raw.level_up_bonus_sequence_3
                            "Level Up Enhancement 8 Bit Retro Sound Effect" -> R.raw.level_up_enhancement_8_bit_retro_sound_effect
                            "Level Win" -> R.raw.level_win
                            "Lose Funny Retro Video Game" -> R.raw.lose_funny_retro_video_game
                            "Message Incoming" -> R.raw.message_incoming
                            "Music Box For Suspenseful Stories" -> R.raw.music_box_for_suspenseful_stories
                            "New Level" -> R.raw.new_level
                            "Next Level" -> R.raw.next_level
                            "Notification For Game Scenes" -> R.raw.notification_for_game_scenes
                            "90s Game UI 12" -> R.raw.noventas_game_ui_12
                            "90s Game UI 6" -> R.raw.noventas_game_ui_6
                            "8 Bit Game 1" -> R.raw.ocho_bit_game_1
                            "8 Bit Game 3" -> R.raw.ocho_bit_game_3
                            "8 Bit Game 6" -> R.raw.ocho_bit_game_6
                            "8 Bit Game 7" -> R.raw.ocho_bit_game_7
                            "8 Bit Moonlight Sonata Music Loop" -> R.raw.ocho_bit_moonlight_sonata_music_loop
                            "8 Bit Power Up" -> R.raw.ocho_bit_powerup
                            "8 Bit Video Game Fail Version 2" -> R.raw.ocho_bit_video_game_fail_version_2
                            "Open The Can" -> R.raw.open_the_can
                            "Pig Level Win 2" -> R.raw.pig_level_win_2
                            "Playful Casino Slot Machine Jackpot 3" -> R.raw.playful_casino_slot_machine_jackpot_3
                            "Ready Fight" -> R.raw.ready_fight
                            "Robot Saying 1" -> R.raw.robot_saying_1
                            "Robot Saying 2" -> R.raw.robot_saying_2
                            "Robot Saying 3" -> R.raw.robot_saying_3
                            "Scary Swoosh" -> R.raw.scary_swoosh
                            "Sinus Bomb" -> R.raw.sinus_bomb
                            "Soft Bells" -> R.raw.soft_bells
                            "Space Line" -> R.raw.space_line
                            "Success Fanfare Trumpets" -> R.raw.success_fanfare_trumpets
                            "Suddenly" -> R.raw.suddenly
                            "Three Two One Fight Deep Voice" -> R.raw.three_two_one_fight_deep_voice
                            "Videogame Death Sound" -> R.raw.videogame_death_sound
                            "Violin Lose 1" -> R.raw.violin_lose_1
                            "Violin Lose 4" -> R.raw.violin_lose_4
                            "Violin Lose 5" -> R.raw.violin_lose_5
                            "Violin Win 5" -> R.raw.violin_win_5
                            "WinFantasia" -> R.raw.winfantasia
                            "WingGrandPiano" -> R.raw.wingrandpiano
                            "Winsquare" -> R.raw.winsquare
                            "Wrong Answer" -> R.raw.wrong_answer
                            "Wrong Buzzer" -> R.raw.wrong_buzzer
                            "Wrong Place" -> R.raw.wrong_place
                            else -> R.raw.analog_warm_pluck
                        }
                    } else {
                        track = -1
                    }
                    savePosition = position
                    CoroutineScope(Dispatchers.IO).launch {
                        guardarIntPreferencias(TRACK_TEMPORIZADOR, track)
                        guardarIntPreferencias(SONIDO_TEMPORIZADOR, savePosition)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        spinner.setSelection(savePosition)
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

    private fun initSpinner(spinner: Spinner, arrayString: Array<String>, state: Int) {
        var contador: Int
        var segundos: Int = 0

        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayString)
            adapter.setDropDownViewResource(R.layout.spinner_list)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    if (arrayString[position] != "Horas" && arrayString[position] != "Minutos" && arrayString[position] != "Segundos") {
                        contador = arrayString[position].toInt()
                        when (state) {
                            0 -> {
                                segundos += contador
                                contSegundos = segundos
                                segundos = 0
                            }

                            1 -> {
                                segundos += contador * 60
                                contMinutos = segundos
                                segundos = 0
                            }

                            2 -> {
                                segundos += contador * 3600
                                contHoras = segundos
                                segundos = 0
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    private fun secondsFormatted(time: Int): String {
        val timeFormatted = String.format("%02d", time)
        return timeFormatted
    }

    private fun appearCronometroAnim(view: View) {
        val appear = AnimatorInflater.loadAnimator(
            view.context,
            R.animator.cronometro_appear
        ) as AnimatorSet
        appear.apply {
            setTarget(view)
            doOnStart {
                view.isVisible = true
            }
            start()
        }
    }

    private fun dissappearCronometroAnim(view: View) {
        val dissappear = AnimatorInflater.loadAnimator(
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

    private fun btnAjustesUP(view: View) {
        val increaseAnimationX = ObjectAnimator.ofFloat(view, "scaleX", 0.9f, 1.0f)
        val increaseAnimationY = ObjectAnimator.ofFloat(view, "scaleY", 0.9f, 1.0f)


        val animatorSet = AnimatorSet()
        animatorSet.apply {
            duration = 300
            interpolator = BounceInterpolator()
            playTogether(increaseAnimationX, increaseAnimationY)
            start()
        }
    }

    private fun btnAjustesPressed(view: View) {
        val decreaseAnimationX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.9f)

        val decreaseAnimationY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.9f)


        val animatorSet = AnimatorSet()
        animatorSet.apply {
            duration = 300
            interpolator = BounceInterpolator()
            playTogether(decreaseAnimationX, decreaseAnimationY)
            start()
        }
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

    private fun getSettings(): Flow<SettingsModel> {
        return dataStore.data.map { Preferencias ->
            SettingsModel(
                Preferencias[intPreferencesKey(SONIDO_TEMPORIZADOR)] ?: 0,
                Preferencias[booleanPreferencesKey(VIBRACION_TEMPORIZADOR)] ?: false,
                Preferencias[intPreferencesKey(TRACK_TEMPORIZADOR)] ?: -1,
                Preferencias[intPreferencesKey(COUNT_TEMP_ADS)] ?: 2
            )
        }
    }
}