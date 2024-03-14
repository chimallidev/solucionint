package com.chimallidigital.solucionint.ui.tabata_timer.adapter

import android.R
import android.media.MediaPlayer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Spinner
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.chimallidigital.solucionint.databinding.ItemSetTabataTimerBinding
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection.Ciclos
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection.Conjuntos
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection.Descanso
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection.Descanso_entre_conjuntos
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection.Ejercicio
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection.Preparacion

class SetTabataTimerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemSetTabataTimerBinding.bind(view)

    private var contSegundos = 0
    private var contMinutos = 0
    var contHoras = 0
    private var track = -1
    private var savePosition = 0
    private var savePositionPreparacionHoras = 0
    private var savePositionPreparacionMinutos = 0
    private var savePositionPreparacionSegundos = 0
    private var savePositionEjercicioHoras = 0
    private var savePositionEjercicioMinutos = 0
    private var savePositionEjercicioSegundos = 0
    private var savePositionDescansoHoras = 0
    private var savePositionDescansoMinutos = 0
    private var savePositionDescansoSegundos = 0
    private var savePositionDescansoEntreHoras = 0
    private var savePositionDescansoEntreMinutos = 0
    private var savePositionDescansoEntreSegundos = 0
    private var stateAnimator = false
    var countTabataTimer = 1
    var vibrationState = false

    var mMediaPlayer: MediaPlayer? = null

    val contextHours = binding.spinnerHoras.context
    val contextMinutos = binding.spinnerMinutos.context
    val contextSegundos = binding.spinnerSegundos.context
    val contextSonidos = binding.spinnerSonido.context

    val hours = contextHours.resources.getStringArray(com.chimallidigital.solucionint.R.array.Hours)
    val minutes =
        contextMinutos.resources.getStringArray(com.chimallidigital.solucionint.R.array.Minutos)
    val seconds =
        contextSegundos.resources.getStringArray(com.chimallidigital.solucionint.R.array.Segundos)
    val sonidos =
        contextSonidos.resources.getStringArray(com.chimallidigital.solucionint.R.array.Sonidos)

    fun render(
        setItemsCollection: SetItemsCollection,
        itemOnSelected: (SetItemsCollection, hora: Int, minutos: Int, segundos: Int) -> Unit,
        count: (SetItemsCollection, count: Int) -> Unit,
        trackSave: (SetItemsCollection, track: Int) -> Unit,
        vibration: (SetItemsCollection, vibrationState: Boolean) -> Unit,
        timePosition: (SetItemsCollection, state: Int, position: Int) -> Unit,
        trackPosition: (SetItemsCollection, numberTrackPosition: Int) -> Unit,
        preparacionHoras: Int,
        preparacionMinutos: Int,
        preparacionSegundos: Int,
        ejercicioHoras: Int,
        ejercicioMinutos: Int,
        ejercicioSegundos: Int,
        descansoHoras: Int,
        descansoMinutos: Int,
        descansoSegundos: Int,
        descansoEntreHoras: Int,
        descansoEntreMinutos: Int,
        descansoEntreSegundos: Int,
        countCiclos: Int,
        countConjuntos: Int,
        trackEjercicio: Int,
        trackDescanso: Int,
        trackDescansoEntre: Int,
        vibrationStateEjercicio: Boolean,
        vibrationStateDescanso: Boolean,
        vibrationStateDescansoEntre: Boolean,
        positionTrackDescanso: Int,
        positionTrackDescansoEntre: Int,
        positionTrackEjercicio: Int,
        positionPrepHoras: Int,
        positionPrepMinutos: Int,
        positionPrepSegundos: Int,
        positionDescHoras: Int,
        positionDescMinutos: Int,
        positionDescSegundos: Int,
        positionDescEntreHoras: Int,
        positionDescEntreMinutos: Int,
        positionDescEntreSegundos: Int,
        positionEjHoras: Int,
        positionEjMinutos: Int,
        positionEjSegundos: Int
    ) {

        val contexTitulo = binding.tvTitulo.context

        binding.tvTitulo.text = contexTitulo.getString(setItemsCollection.title)
        binding.ivItem.setImageResource(setItemsCollection.img)
        if (setItemsCollection.stateSetTiempo == 1) {
            binding.constEditText.isVisible = false
            binding.tvTiempo.isVisible = true
            binding.constSpinnersTiempo.isVisible = true
            savePositionPreparacionHoras = positionPrepHoras
            savePositionPreparacionMinutos = positionPrepMinutos
            savePositionPreparacionSegundos = positionPrepSegundos
            savePositionEjercicioHoras = positionEjHoras
            savePositionEjercicioMinutos = positionEjMinutos
            savePositionEjercicioSegundos = positionEjSegundos
            savePositionDescansoHoras = positionDescHoras
            savePositionDescansoMinutos = positionDescMinutos
            savePositionDescansoSegundos = positionDescSegundos
            savePositionDescansoEntreHoras = positionDescEntreHoras
            savePositionDescansoEntreMinutos = positionDescEntreMinutos
            savePositionDescansoEntreSegundos = positionDescEntreSegundos
            initSpinner(
                binding.spinnerHoras,
                hours,
                2,
                itemOnSelected,
                setItemsCollection,
                timePosition
            )
            initSpinner(
                binding.spinnerMinutos,
                minutes,
                1,
                itemOnSelected,
                setItemsCollection,
                timePosition
            )
            initSpinner(
                binding.spinnerSegundos,
                seconds,
                0,
                itemOnSelected,
                setItemsCollection,
                timePosition
            )
            if (setItemsCollection.stateSonido == 1) {
                binding.constSpinersSonido.isVisible = true
                when (setItemsCollection) {
                    Ciclos -> {}
                    Conjuntos -> {}
                    Descanso -> {
                        savePosition = positionTrackDescanso
                        track = trackDescanso
                    }

                    Descanso_entre_conjuntos -> {
                        savePosition = positionTrackDescansoEntre
                        track = trackDescansoEntre
                    }

                    Ejercicio -> {
                        savePosition = positionTrackEjercicio
                        track = trackEjercicio
                    }

                    Preparacion -> {}
                }
                initSpinnerPlaySonido(
                    binding.spinnerSonido,
                    sonidos,
                    trackSave,
                    setItemsCollection,
                    trackPosition
                )
                binding.BTNPlaySonido.setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                        when (event?.action) {
                            MotionEvent.ACTION_DOWN -> {
                                btnPressed(
                                    binding.backgroundBTNPlaySonido,
                                    binding.shadowBTNPlaySonido
                                )
                            }

                            MotionEvent.ACTION_MOVE -> {
                                val x = event.getX()
                                val y = event.getY()
                                Log.i("tabata_play_sonido", "X= $x")
                                Log.i("tabata_play_sonido", "Y= $y")
                            }

                            MotionEvent.ACTION_CANCEL -> {
                                btnAnimation(
                                    binding.backgroundBTNPlaySonido, binding.shadowBTNPlaySonido
                                )
                            }

                            MotionEvent.ACTION_UP -> {
                                val x = event.getX()
                                val y = event.getY()

                                btnAnimation(
                                    binding.backgroundBTNPlaySonido,
                                    binding.shadowBTNPlaySonido
                                )
                                if (track != -1) {
                                    stopSound()
                                    playSound(track)
                                }
                            }
                        }
                        return true
                    }
                })
            } else {
                binding.constSpinersSonido.isVisible = false
            }
            if (setItemsCollection.stateVibration == 1) {
                binding.constVibration.isVisible = true
                when (setItemsCollection) {
                    Ciclos -> {}
                    Conjuntos -> {}
                    Descanso -> {
                        vibrationState = vibrationStateDescanso
                    }

                    Descanso_entre_conjuntos -> {
                        vibrationState = vibrationStateDescansoEntre
                    }

                    Ejercicio -> {
                        vibrationState = vibrationStateEjercicio
                    }

                    Preparacion -> {}
                }

                if (vibrationState) {
                    binding.switchVibration.isChecked = true
                } else {
                    binding.switchVibration.isChecked = false
                }
                binding.switchVibration.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                    if (b) {
                        vibrationState = true
                        vibration(setItemsCollection, vibrationState)
                    } else {
                        vibrationState = false
                        vibration(setItemsCollection, vibrationState)
                    }
                }
            } else {
                binding.constVibration.isVisible = false
            }

        } else {
            binding.constEditText.isVisible = true
            binding.tvTiempo.isVisible = false
            binding.constSpinnersTiempo.isVisible = false
            binding.constSpinersSonido.isVisible = false
            binding.constVibration.isVisible = false

            if (setItemsCollection == Ciclos) {
                countTabataTimer = countCiclos
                binding.tvCountTabataTimer.text = formatZeroZero(countTabataTimer)
            } else {
                countTabataTimer = countConjuntos
                binding.tvCountTabataTimer.text = formatZeroZero(countTabataTimer)
            }
            binding.BTNLess.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            btnPressed(
                                binding.backgroundBTNLess,
                                binding.shadowBTNLess
                            )
                        }

                        MotionEvent.ACTION_MOVE -> {
                            val x = event.getX()
                            val y = event.getY()
                            Log.i("tabata_btnless", "X= $x")
                            Log.i("tabata_btnless", "Y= $y")
                        }

                        MotionEvent.ACTION_CANCEL -> {
                            btnAnimation(
                                binding.backgroundBTNLess,
                                binding.shadowBTNLess
                            )
                        }

                        MotionEvent.ACTION_UP -> {
                            val x = event.getX()
                            val y = event.getY()

                            btnAnimation(
                                binding.backgroundBTNLess,
                                binding.shadowBTNLess
                            )
                            if (countTabataTimer != 1) {
                                countTabataTimer--
                                binding.tvCountTabataTimer.text = formatZeroZero(countTabataTimer)
                                count(setItemsCollection, countTabataTimer)
                            }
                        }
                    }
                    return true
                }
            })
            binding.BTNPlus.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            btnPressed(
                                binding.backgroundBTNPlus,
                                binding.shadowBTNPlus
                            )
                        }

                        MotionEvent.ACTION_MOVE -> {
                            val x = event.getX()
                            val y = event.getY()
                            Log.i("tabata_btnless", "X= $x")
                            Log.i("tabata_btnless", "Y= $y")
                        }

                        MotionEvent.ACTION_CANCEL -> {
                            btnAnimation(
                                binding.backgroundBTNPlus,
                                binding.shadowBTNPlus
                            )
                        }

                        MotionEvent.ACTION_UP -> {
                            val x = event.getX()
                            val y = event.getY()

                            btnAnimation(
                                binding.backgroundBTNPlus,
                                binding.shadowBTNPlus
                            )
                            if (countTabataTimer != 99) {
                                countTabataTimer++
                                binding.tvCountTabataTimer.text = formatZeroZero(countTabataTimer)
                                count(setItemsCollection, countTabataTimer)
                            }
                        }
                    }
                    return true
                }
            })
        }
    }

    fun initSpinner(
        spinner: Spinner,
        arrayString: Array<String>,
        state: Int,
        itemOnSelected: (SetItemsCollection, hora: Int, minutos: Int, segundos: Int) -> Unit,
        setItemsCollection: SetItemsCollection,
        timePosition: (SetItemsCollection, state: Int, position: Int) -> Unit
    ) {
        var contador: Int
        var segundos: Int = 0

        if (spinner != null) {
            val adapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, arrayString)
            adapter.setDropDownViewResource(com.chimallidigital.solucionint.R.layout.spinner_list_tabata_timer)
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
                                itemOnSelected(
                                    setItemsCollection,
                                    contHoras,
                                    contMinutos,
                                    contSegundos
                                )
                                segundos = 0
                            }

                            1 -> {
                                segundos += contador * 60
                                contMinutos = segundos
                                itemOnSelected(
                                    setItemsCollection,
                                    contHoras,
                                    contMinutos,
                                    contSegundos
                                )
                                segundos = 0
                            }

                            2 -> {
                                segundos += contador * 3600
                                contHoras = segundos
                                itemOnSelected(
                                    setItemsCollection,
                                    contHoras,
                                    contMinutos,
                                    contSegundos
                                )
                                segundos = 0
                            }
                        }
                    } else {
                        when (state) {
                            0 -> {
                                contSegundos = 0
                                itemOnSelected(
                                    setItemsCollection,
                                    contHoras,
                                    contMinutos,
                                    contSegundos
                                )
                            }

                            1 -> {
                                contMinutos = 0
                                itemOnSelected(
                                    setItemsCollection,
                                    contHoras,
                                    contMinutos,
                                    contSegundos
                                )
                            }

                            2 -> {
                                contHoras = 0
                                itemOnSelected(
                                    setItemsCollection,
                                    contHoras,
                                    contMinutos,
                                    contSegundos
                                )
                            }
                        }
                    }
                    when (setItemsCollection) {
                        Ciclos -> {}
                        Conjuntos -> {}
                        Descanso -> {
                            when (state) {
                                0 -> {
                                    savePositionDescansoSegundos = position
                                    timePosition(setItemsCollection, state, position)

                                }

                                1 -> {
                                    savePositionDescansoMinutos = position
                                    timePosition(setItemsCollection, state, position)
                                }

                                else -> {
                                    savePositionDescansoHoras = position
                                    timePosition(setItemsCollection, state, position)
                                }
                            }
                        }

                        Descanso_entre_conjuntos -> {
                            when (state) {
                                0 -> {
                                    savePositionDescansoEntreSegundos = position
                                    timePosition(setItemsCollection, state, position)
                                }

                                1 -> {
                                    savePositionDescansoEntreMinutos = position
                                    timePosition(setItemsCollection, state, position)
                                }

                                else -> {
                                    savePositionDescansoEntreHoras = position
                                    timePosition(setItemsCollection, state, position)
                                }
                            }
                        }

                        Ejercicio -> {
                            when (state) {
                                0 -> {
                                    savePositionEjercicioSegundos = position
                                    timePosition(setItemsCollection, state, position)
                                }

                                1 -> {
                                    savePositionEjercicioMinutos = position
                                    timePosition(setItemsCollection, state, position)
                                }

                                else -> {
                                    savePositionEjercicioHoras = position
                                    timePosition(setItemsCollection, state, position)
                                }
                            }
                        }

                        Preparacion -> {
                            when (state) {
                                0 -> {
                                    savePositionPreparacionSegundos = position
                                    timePosition(setItemsCollection, state, position)
                                }

                                1 -> {
                                    savePositionPreparacionMinutos = position
                                    timePosition(setItemsCollection, state, position)
                                }

                                else -> {
                                    savePositionPreparacionHoras = position
                                    timePosition(setItemsCollection, state, position)
                                }
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        when (setItemsCollection) {
            Ciclos -> {}
            Conjuntos -> {}
            Descanso -> {
                when (state) {
                    0 -> {
                        spinner.setSelection(savePositionDescansoSegundos)
                    }

                    1 -> {
                        spinner.setSelection(savePositionDescansoMinutos)
                    }

                    else -> {
                        spinner.setSelection(savePositionDescansoHoras)
                    }
                }
            }

            Descanso_entre_conjuntos -> {
                when (state) {
                    0 -> {
                        spinner.setSelection(savePositionDescansoEntreSegundos)
                    }

                    1 -> {
                        spinner.setSelection(savePositionDescansoEntreMinutos)
                    }

                    else -> {
                        spinner.setSelection(savePositionDescansoEntreHoras)
                    }
                }
            }

            Ejercicio -> {
                when (state) {
                    0 -> {
                        spinner.setSelection(savePositionEjercicioSegundos)
                    }

                    1 -> {
                        spinner.setSelection(savePositionEjercicioMinutos)
                    }

                    else -> {
                        spinner.setSelection(savePositionEjercicioHoras)
                    }
                }
            }

            Preparacion -> {
                when (state) {
                    0 -> {
                        spinner.setSelection(savePositionPreparacionSegundos)
                    }

                    1 -> {
                        spinner.setSelection(savePositionPreparacionMinutos)
                    }

                    else -> {
                        spinner.setSelection(savePositionPreparacionHoras)
                    }
                }
            }
        }
    }

    fun initSpinnerPlaySonido(
        spinner: Spinner,
        sonidos: Array<String>,
        trackSave: (SetItemsCollection, track: Int) -> Unit,
        setItemsCollection: SetItemsCollection,
        trackPosition: (SetItemsCollection, numberTrackPosition: Int) -> Unit
    ) {

        if (spinner != null) {
            val adapter =
                ArrayAdapter(spinner.context, android.R.layout.simple_spinner_item, sonidos)
            adapter.setDropDownViewResource(com.chimallidigital.solucionint.R.layout.spinner_list_tabata_timer)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    if (sonidos[position] != "Ninguno") {
                        track = when (sonidos[position]) {
                            "Analog Warm Pluck" -> com.chimallidigital.solucionint.R.raw.analog_warm_pluck
                            "Big Impact" -> com.chimallidigital.solucionint.R.raw.big_impact
                            "Blaster 2" -> com.chimallidigital.solucionint.R.raw.blaster_2
                            "Brass Fail 1 c" -> com.chimallidigital.solucionint.R.raw.brass_fail_1_c
                            "Cartoon Jump" -> com.chimallidigital.solucionint.R.raw.cartoon_jump
                            "Copper Bell Ding 3" -> com.chimallidigital.solucionint.R.raw.copper_bell_ding_3
                            "Cute Level Up 3" -> com.chimallidigital.solucionint.R.raw.cute_level_up_3
                            "Digital Alarm Clock" -> com.chimallidigital.solucionint.R.raw.digital_alarm_clock
                            "Epic Extreme Hit" -> com.chimallidigital.solucionint.R.raw.epic_extreme_hit
                            "Epic Hybrid Logo" -> com.chimallidigital.solucionint.R.raw.epic_hybrid_logo
                            "Epic Logo" -> com.chimallidigital.solucionint.R.raw.epic_logo
                            "Error 2" -> com.chimallidigital.solucionint.R.raw.error_2
                            "Error When Entering The Game Menu" -> com.chimallidigital.solucionint.R.raw.error_when_entering_the_game_menu
                            "Fail" -> com.chimallidigital.solucionint.R.raw.fail
                            "Failure 1" -> com.chimallidigital.solucionint.R.raw.failure_1
                            "Fight Deep Voice" -> com.chimallidigital.solucionint.R.raw.fight_deep_voice
                            "Friend Request" -> com.chimallidigital.solucionint.R.raw.friend_request
                            "Game Bonus" -> com.chimallidigital.solucionint.R.raw.game_bonus
                            "Game Level Complete" -> com.chimallidigital.solucionint.R.raw.game_level_complete
                            "Game Over Arcade" -> com.chimallidigital.solucionint.R.raw.game_over_arcade
                            "Game Start" -> com.chimallidigital.solucionint.R.raw.game_start
                            "Going To The Next Level" -> com.chimallidigital.solucionint.R.raw.going_to_the_next_level
                            "Grand Final Orchestral Tutti" -> com.chimallidigital.solucionint.R.raw.grand_final_orchestral_tutti
                            "Growling Zombie" -> com.chimallidigital.solucionint.R.raw.growling_zombie
                            "Interface Welcome" -> com.chimallidigital.solucionint.R.raw.interface_welcome
                            "Invalid Selection" -> com.chimallidigital.solucionint.R.raw.invalid_selection
                            "Level Up Bonus Sequence 2" -> com.chimallidigital.solucionint.R.raw.level_up_bonus_sequence_2
                            "Level Up Bonus Sequence 3" -> com.chimallidigital.solucionint.R.raw.level_up_bonus_sequence_3
                            "Level Up Enhancement 8 Bit Retro Sound Effect" -> com.chimallidigital.solucionint.R.raw.level_up_enhancement_8_bit_retro_sound_effect
                            "Level Win" -> com.chimallidigital.solucionint.R.raw.level_win
                            "Lose Funny Retro Video Game" -> com.chimallidigital.solucionint.R.raw.lose_funny_retro_video_game
                            "Message Incoming" -> com.chimallidigital.solucionint.R.raw.message_incoming
                            "Music Box For Suspenseful Stories" -> com.chimallidigital.solucionint.R.raw.music_box_for_suspenseful_stories
                            "New Level" -> com.chimallidigital.solucionint.R.raw.new_level
                            "Next Level" -> com.chimallidigital.solucionint.R.raw.next_level
                            "Notification For Game Scenes" -> com.chimallidigital.solucionint.R.raw.notification_for_game_scenes
                            "90s Game UI 12" -> com.chimallidigital.solucionint.R.raw.noventas_game_ui_12
                            "90s Game UI 6" -> com.chimallidigital.solucionint.R.raw.noventas_game_ui_6
                            "8 Bit Game 1" -> com.chimallidigital.solucionint.R.raw.ocho_bit_game_1
                            "8 Bit Game 3" -> com.chimallidigital.solucionint.R.raw.ocho_bit_game_3
                            "8 Bit Game 6" -> com.chimallidigital.solucionint.R.raw.ocho_bit_game_6
                            "8 Bit Game 7" -> com.chimallidigital.solucionint.R.raw.ocho_bit_game_7
                            "8 Bit Moonlight Sonata Music Loop" -> com.chimallidigital.solucionint.R.raw.ocho_bit_moonlight_sonata_music_loop
                            "8 Bit Power Up" -> com.chimallidigital.solucionint.R.raw.ocho_bit_powerup
                            "8 Bit Video Game Fail Version 2" -> com.chimallidigital.solucionint.R.raw.ocho_bit_video_game_fail_version_2
                            "Open The Can" -> com.chimallidigital.solucionint.R.raw.open_the_can
                            "Pig Level Win 2" -> com.chimallidigital.solucionint.R.raw.pig_level_win_2
                            "Playful Casino Slot Machine Jackpot 3" -> com.chimallidigital.solucionint.R.raw.playful_casino_slot_machine_jackpot_3
                            "Ready Fight" -> com.chimallidigital.solucionint.R.raw.ready_fight
                            "Robot Saying 1" -> com.chimallidigital.solucionint.R.raw.robot_saying_1
                            "Robot Saying 2" -> com.chimallidigital.solucionint.R.raw.robot_saying_2
                            "Robot Saying 3" -> com.chimallidigital.solucionint.R.raw.robot_saying_3
                            "Scary Swoosh" -> com.chimallidigital.solucionint.R.raw.scary_swoosh
                            "Sinus Bomb" -> com.chimallidigital.solucionint.R.raw.sinus_bomb
                            "Soft Bells" -> com.chimallidigital.solucionint.R.raw.soft_bells
                            "Space Line" -> com.chimallidigital.solucionint.R.raw.space_line
                            "Success Fanfare Trumpets" -> com.chimallidigital.solucionint.R.raw.success_fanfare_trumpets
                            "Suddenly" -> com.chimallidigital.solucionint.R.raw.suddenly
                            "Three Two One Fight Deep Voice" -> com.chimallidigital.solucionint.R.raw.three_two_one_fight_deep_voice
                            "Videogame Death Sound" -> com.chimallidigital.solucionint.R.raw.videogame_death_sound
                            "Violin Lose 1" -> com.chimallidigital.solucionint.R.raw.violin_lose_1
                            "Violin Lose 4" -> com.chimallidigital.solucionint.R.raw.violin_lose_4
                            "Violin Lose 5" -> com.chimallidigital.solucionint.R.raw.violin_lose_5
                            "Violin Win 5" -> com.chimallidigital.solucionint.R.raw.violin_win_5
                            "WinFantasia" -> com.chimallidigital.solucionint.R.raw.winfantasia
                            "WingGrandPiano" -> com.chimallidigital.solucionint.R.raw.wingrandpiano
                            "Winsquare" -> com.chimallidigital.solucionint.R.raw.winsquare
                            "Wrong Answer" -> com.chimallidigital.solucionint.R.raw.wrong_answer
                            "Wrong Buzzer" -> com.chimallidigital.solucionint.R.raw.wrong_buzzer
                            "Wrong Place" -> com.chimallidigital.solucionint.R.raw.wrong_place
                            else -> com.chimallidigital.solucionint.R.raw.analog_warm_pluck
                        }

                    } else {
                        track = -1
                    }
                    savePosition = position
                    trackSave(setItemsCollection, track)
                    trackPosition(setItemsCollection, position)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        spinner.setSelection(savePosition)
    }

    private fun playSound(sonido: Int) {
//        if (mMediaPlayer == null) {

        mMediaPlayer = MediaPlayer.create(binding.itemSetTabataTimer.context, sonido)
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

    fun formatZeroZero(value: Int): String {
        val tiempo = String.format("%02d", value)
        return tiempo
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