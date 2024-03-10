package com.chimallidigital.solucionint.ui.tabata_timer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivityTabataTimerBinding
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.CICLOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.CONJUNTOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.ITEMS_LIST
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TIEMPO_TOTAL
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_COUNT_CICLOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_COUNT_CONJUNTOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_DESCANSO_ENTRE_CONJUNTOS_HORAS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_DESCANSO_ENTRE_CONJUNTOS_MINUTOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_DESCANSO_ENTRE_CONJUNTOS_SEGUNDOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_DESCANSO_ENTRE_TIEMPO
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_DESCANSO_HORAS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_DESCANSO_MINUTOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_DESCANSO_SEGUNDOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_DESCANSO_TIEMPO
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_EJERCICIO_HORAS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_EJERCICIO_MINUTOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_EJERCICIO_SEGUNDOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_EJERCICIO_TIEMPO
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_POSITION_DESCANSO_ENTRE_HORAS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_POSITION_DESCANSO_ENTRE_MINUTOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_POSITION_DESCANSO_ENTRE_SEGUNDOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_POSITION_DESCANSO_HORAS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_POSITION_DESCANSO_MINUTOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_POSITION_DESCANSO_SEGUNDOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_POSITION_EJERCICIO_HORAS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_POSITION_EJERCICIO_MINUTOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_POSITION_EJERCICIO_SEGUNDOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_POSITION_PREPARACION_HORAS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_POSITION_PREPARACION_MINUTOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_POSITION_PREPARACION_SEGUNDOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_POSITION_TRACK_DESCANSO
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_POSITION_TRACK_DESCANSO_ENTRE
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_POSITION_TRACK_EJERCICIO
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_PREPARACION_HORAS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_PREPARACION_MINUTOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_PREPARACION_SEGUNDOS
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_PREPARACION_TIEMPO
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_TIEMPOTOTAL
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_TIEMPO_TOTAL
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_TRACK_DESCANSO
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_TRACK_DESCANSO_ENTRE
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_TRACK_EJERCICIO
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_VIBRATION_STATE_DESCANSO
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_VIBRATION_STATE_DESCANSO_ENTRE
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.TT_VIBRATION_STATE_EJERCICIO
import com.chimallidigital.solucionint.domain.model.Settings.SettingsTabataTimerModel
import com.chimallidigital.solucionint.domain.model.TabataTimer.ItemsCollection
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection.Ciclos
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection.Conjuntos
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection.Descanso
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection.Descanso_entre_conjuntos
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection.Ejercicio
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection.Preparacion
import com.chimallidigital.solucionint.ui.home.MainActivity
import com.chimallidigital.solucionint.ui.tabata_timer.adapter.SetTabataTimerAdapter
import com.chimallidigital.solucionint.ui.tabata_timer_playing.TabataTimerPlayingActivity
import com.chimallidigital.solucionint.ui.time_meters.TimeMetersFragment

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "SettingsTabataTimer")

@AndroidEntryPoint
class TabataTimerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTabataTimerBinding
    private lateinit var setTabataTimerAdapter: SetTabataTimerAdapter

    private val tabataTimerViewModel: TabataTimerViewModel by viewModels()

    private var preparacionHoras = 0
    private var preparacionMinutos = 0
    private var preparacionSegundos = 0
    private var ejercicioHoras = 0
    private var ejercicioMinutos = 0
    private var ejercicioSegundos = 0
    private var descansoHoras = 0
    private var descansoMinutos = 0
    private var descansoSegundos = 0
    private var descansoEntreHoras = 0
    private var descansoEntreMinutos = 0
    private var descansoEntreSegundos = 0
    private var countCiclos = 1
    private var countConjuntos = 1
    var tiempoTotal = 0
    private var preparacionTiempo = 0
    private var ejercicioTiempo = 0
    private var descansoTiempo = 0
    private var descansoEntreTiempo = 0
    private var trackEjercicio = -1
    private var trackDescanso = -1
    private var trackDescansoEntre = -1
    private var vibrationStateEjercicio = false
    private var vibrationStateDescanso = false
    private var vibrationStateDescansoEntre = false
    private var newList: MutableList<ItemsCollection> = mutableListOf()
    private var firstTime = true
    private var positionPreparacionHoras = 0
    private var positionPreparacionMinutos = 0
    private var positionPreparacionSegundos = 0
    private var positionEjercicioHoras = 0
    private var positionEjercicioMinutos = 0
    private var positionEjercicioSegundos = 0
    private var positionDescansoHoras = 0
    private var positionDescansoMinutos = 0
    private var positionDescansoSegundos = 0
    private var positionDescansoEntreHoras = 0
    private var positionDescansoEntreMinutos = 0
    private var positionDescansoEntreSegundos = 0
    private var positionTrackEjercicio = 0
    private var positionTrackDescanso = 0
    private var positionTrackDescansoEntre = 0
    private var maxlimit = false
    private var stateAnimator = false
    private var stateNOSetEjComenzar = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTabataTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        stateSettings()
        initUI()
        stateSettingsTime()
        Log.i("faro", "El tiempo total despues de settingsState: $tiempoTotal")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Your logic here
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun initUI() {
        initListeners()
        initSetRecyclerView()
    }

    private fun initSetRecyclerView() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                tabataTimerViewModel.items.collect {
                    setTabataTimerAdapter.UpdateList(it)
                }
            }
        }
    }

    private fun initListeners() {
        setTabataTimerAdapter =
            SetTabataTimerAdapter(itemOnSelected = { item: SetItemsCollection, hora: Int, minutos: Int, segundos: Int ->

                when (item) {
                    Ciclos -> {
                        Toast.makeText(this, "$item", Toast.LENGTH_LONG).show()
                    }

                    Conjuntos -> {
                        Toast.makeText(this, "$item", Toast.LENGTH_LONG).show()
                    }

                    Descanso -> {
                        descansoHoras = hora
                        descansoMinutos = minutos
                        descansoSegundos = segundos
                        descansoTiempo = descansoHoras + descansoMinutos + descansoSegundos
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarIntPreferencias(TT_DESCANSO_HORAS, descansoHoras)
                            guardarIntPreferencias(TT_DESCANSO_MINUTOS, descansoMinutos)
                            guardarIntPreferencias(TT_DESCANSO_SEGUNDOS, descansoSegundos)
                            guardarIntPreferencias(TT_DESCANSO_TIEMPO, descansoTiempo)
                        }
                        if (countConjuntos > 1) {
                            if (countCiclos > 1) {
                                tiempoTotal =
                                    preparacionTiempo + ((((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo + descansoEntreTiempo) * (countConjuntos - 1)) +
                                            (((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo)
                            } else {
                                tiempoTotal =
                                    preparacionTiempo + ((ejercicioTiempo + descansoEntreTiempo) * (countConjuntos - 1)) + ejercicioTiempo
                            }
                        } else {
                            if (countCiclos > 1) {
                                tiempoTotal =
                                    preparacionTiempo + ((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo
                            } else {
                                tiempoTotal = preparacionTiempo + ejercicioTiempo
                            }
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarIntPreferencias(TT_TIEMPOTOTAL, tiempoTotal)
                        }
                        if (tiempoTotal <= 360000) {
                            formatTotalTime(tiempoTotal)
                            maxlimit = false
                        }
                        if (tiempoTotal > 360000 && !maxlimit) {
                            binding.tvTiempoTotal.text = "99:59:59"
                            Toast.makeText(
                                this,
                                "Has superado el tiempo maximo total de 99:59:59",
                                Toast.LENGTH_LONG
                            ).show()
                            maxlimit = true
                        }
                    }

                    Descanso_entre_conjuntos -> {
                        descansoEntreHoras = hora
                        descansoEntreMinutos = minutos
                        descansoEntreSegundos = segundos
                        descansoEntreTiempo =
                            descansoEntreHoras + descansoEntreMinutos + descansoEntreSegundos
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarIntPreferencias(
                                TT_DESCANSO_ENTRE_CONJUNTOS_HORAS,
                                descansoEntreHoras
                            )
                            guardarIntPreferencias(
                                TT_DESCANSO_ENTRE_CONJUNTOS_MINUTOS,
                                descansoEntreMinutos
                            )
                            guardarIntPreferencias(
                                TT_DESCANSO_ENTRE_CONJUNTOS_SEGUNDOS,
                                descansoEntreSegundos
                            )
                            guardarIntPreferencias(TT_DESCANSO_ENTRE_TIEMPO, descansoEntreTiempo)
                        }
                        if (countConjuntos > 1) {
                            if (countCiclos > 1) {
                                tiempoTotal =
                                    preparacionTiempo + ((((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo + descansoEntreTiempo) * (countConjuntos - 1)) +
                                            (((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo)
                            } else {
                                tiempoTotal =
                                    preparacionTiempo + ((ejercicioTiempo + descansoEntreTiempo) * (countConjuntos - 1)) + ejercicioTiempo
                            }
                        } else {
                            if (countCiclos > 1) {
                                tiempoTotal =
                                    preparacionTiempo + ((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo
                            } else {
                                tiempoTotal = preparacionTiempo + ejercicioTiempo
                            }
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarIntPreferencias(TT_TIEMPOTOTAL, tiempoTotal)
                        }
                        if (tiempoTotal <= 360000) {
                            formatTotalTime(tiempoTotal)
                            maxlimit = false
                        }
                        if (tiempoTotal > 360000 && !maxlimit) {
                            binding.tvTiempoTotal.text = "99:59:59"
                            Toast.makeText(
                                this,
                                "Has superado el tiempo maximo total de 99:59:59",
                                Toast.LENGTH_LONG
                            ).show()
                            maxlimit = true
                        }
                    }

                    Ejercicio -> {
                        ejercicioHoras = hora
                        ejercicioMinutos = minutos
                        ejercicioSegundos = segundos
                        ejercicioTiempo = ejercicioHoras + ejercicioMinutos + ejercicioSegundos
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarIntPreferencias(TT_EJERCICIO_HORAS, ejercicioHoras)
                            guardarIntPreferencias(TT_EJERCICIO_MINUTOS, ejercicioMinutos)
                            guardarIntPreferencias(TT_EJERCICIO_SEGUNDOS, ejercicioSegundos)
                            guardarIntPreferencias(TT_EJERCICIO_TIEMPO, ejercicioTiempo)
                        }
                        if (countConjuntos > 1) {
                            if (countCiclos > 1) {
                                tiempoTotal =
                                    preparacionTiempo + ((((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo + descansoEntreTiempo) * (countConjuntos - 1)) +
                                            (((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo)
                            } else {
                                tiempoTotal =
                                    preparacionTiempo + ((ejercicioTiempo + descansoEntreTiempo) * (countConjuntos - 1)) + ejercicioTiempo
                            }
                        } else {
                            if (countCiclos > 1) {
                                tiempoTotal =
                                    preparacionTiempo + ((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo
                            } else {
                                tiempoTotal = preparacionTiempo + ejercicioTiempo
                            }
                        }
                        Log.i(
                            "faro",
                            "tiempo total: $tiempoTotal, ciclos: $countCiclos, conjuntos: $countConjuntos, descansoMinutos: $descansoMinutos"
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarIntPreferencias(TT_TIEMPOTOTAL, tiempoTotal)
                        }
                        if (tiempoTotal <= 360000) {
                            formatTotalTime(tiempoTotal)
                            maxlimit = false
                        }
                        if (tiempoTotal > 360000 && !maxlimit) {
                            binding.tvTiempoTotal.text = "99:59:59"
                            Toast.makeText(
                                this,
                                "Has superado el tiempo maximo total de 99:59:59",
                                Toast.LENGTH_LONG
                            ).show()
                            maxlimit = true
                        }
                    }

                    Preparacion -> {
                        preparacionHoras = hora
                        preparacionMinutos = minutos
                        preparacionSegundos = segundos
                        preparacionTiempo =
                            preparacionHoras + preparacionMinutos + preparacionSegundos
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarIntPreferencias(TT_PREPARACION_HORAS, preparacionHoras)
                            guardarIntPreferencias(TT_PREPARACION_MINUTOS, preparacionMinutos)
                            guardarIntPreferencias(TT_PREPARACION_SEGUNDOS, preparacionSegundos)
                            guardarIntPreferencias(TT_PREPARACION_TIEMPO, preparacionTiempo)
                        }
                        if (countConjuntos > 1) {
                            if (countCiclos > 1) {
                                tiempoTotal =
                                    preparacionTiempo + ((((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo + descansoEntreTiempo) * (countConjuntos - 1)) +
                                            (((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo)
                            } else {
                                tiempoTotal =
                                    preparacionTiempo + ((ejercicioTiempo + descansoEntreTiempo) * (countConjuntos - 1)) + ejercicioTiempo
                            }
                        } else {
                            if (countCiclos > 1) {
                                tiempoTotal =
                                    preparacionTiempo + ((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo
                            } else {
                                tiempoTotal = preparacionTiempo + ejercicioTiempo
                            }
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarIntPreferencias(TT_TIEMPOTOTAL, tiempoTotal)
                        }
                        if (tiempoTotal <= 360000) {
                            formatTotalTime(tiempoTotal)
                            maxlimit = false
                        }
                        if (tiempoTotal > 360000 && !maxlimit) {
                            binding.tvTiempoTotal.text = "99:59:59"
                            Toast.makeText(
                                this,
                                "Has superado el tiempo maximo total de 99:59:59",
                                Toast.LENGTH_LONG
                            ).show()
                            maxlimit = true
                        }
                    }
                }
            }, count = { setItemsCollection, count ->
                if (setItemsCollection == Ciclos) {
                    countCiclos = count
                    binding.tvIntervalos.text = formatZeroZero(countCiclos)
                    CoroutineScope(Dispatchers.IO).launch {
                        guardarIntPreferencias(TT_COUNT_CICLOS, countCiclos)
                    }
                    if (countConjuntos > 1) {
                        if (countCiclos > 1) {
                            tiempoTotal =
                                preparacionTiempo + ((((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo + descansoEntreTiempo) * (countConjuntos - 1)) +
                                        (((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo)
                        } else {
                            tiempoTotal =
                                preparacionTiempo + ((ejercicioTiempo + descansoEntreTiempo) * (countConjuntos - 1)) + ejercicioTiempo
                        }
                    } else {
                        if (countCiclos > 1) {
                            tiempoTotal =
                                preparacionTiempo + ((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo
                        } else {
                            tiempoTotal = preparacionTiempo + ejercicioTiempo
                        }
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        guardarIntPreferencias(TT_TIEMPOTOTAL, tiempoTotal)
                    }
                    if (tiempoTotal <= 360000) {
                        formatTotalTime(tiempoTotal)
                        maxlimit = false
                    }
                    if (tiempoTotal > 360000 && !maxlimit) {
                        binding.tvTiempoTotal.text = "99:59:59"
                        Toast.makeText(
                            this,
                            "Has superado el tiempo maximo total de 99:59:59",
                            Toast.LENGTH_LONG
                        ).show()
                        maxlimit = true
                    }
                } else {
                    countConjuntos = count
                    binding.tvConjuntos.text = formatZeroZero(countConjuntos)
                    CoroutineScope(Dispatchers.IO).launch {
                        guardarIntPreferencias(TT_COUNT_CONJUNTOS, countConjuntos)
                    }
                    if (countConjuntos > 1) {
                        if (countCiclos > 1) {
                            tiempoTotal =
                                preparacionTiempo + ((((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo + descansoEntreTiempo) * (countConjuntos - 1)) +
                                        (((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo)
                        } else {
                            tiempoTotal =
                                preparacionTiempo + ((ejercicioTiempo + descansoEntreTiempo) * (countConjuntos - 1)) + ejercicioTiempo
                        }
                    } else {
                        if (countCiclos > 1) {
                            tiempoTotal =
                                preparacionTiempo + ((ejercicioTiempo + descansoTiempo) * (countCiclos - 1)) + ejercicioTiempo
                        } else {
                            tiempoTotal = preparacionTiempo + ejercicioTiempo
                        }
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        guardarIntPreferencias(TT_TIEMPOTOTAL, tiempoTotal)
                    }
                    if (tiempoTotal <= 360000) {
                        formatTotalTime(tiempoTotal)
                        maxlimit = false
                    }
                    if (tiempoTotal > 360000 && !maxlimit) {
                        binding.tvTiempoTotal.text = "99:59:59"
                        Toast.makeText(
                            this,
                            "Has superado el tiempo maximo total de 99:59:59",
                            Toast.LENGTH_LONG
                        ).show()
                        maxlimit = true
                    }
                }
            }, trackSave = { item, track ->
                when (item) {
                    Ciclos -> {}
                    Conjuntos -> {}
                    Descanso -> {
                        trackDescanso = track
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarIntPreferencias(TT_TRACK_DESCANSO, trackDescanso)
                        }
                    }

                    Descanso_entre_conjuntos -> {
                        trackDescansoEntre = track
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarIntPreferencias(TT_TRACK_DESCANSO_ENTRE, trackDescansoEntre)
                        }
                    }

                    Ejercicio -> {
                        trackEjercicio = track
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarIntPreferencias(TT_TRACK_EJERCICIO, trackEjercicio)
                        }
                    }

                    Preparacion -> {}
                }
            }, vibration = { item, state ->
                when (item) {
                    Ciclos -> {}
                    Conjuntos -> {}
                    Descanso -> {
                        vibrationStateDescanso = state
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarPreferencias(TT_VIBRATION_STATE_DESCANSO, vibrationStateDescanso)
                        }
                    }

                    Descanso_entre_conjuntos -> {
                        vibrationStateDescansoEntre = state
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarPreferencias(
                                TT_VIBRATION_STATE_DESCANSO_ENTRE,
                                vibrationStateDescansoEntre
                            )
                        }
                    }

                    Ejercicio -> {
                        vibrationStateEjercicio = state
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarPreferencias(
                                TT_VIBRATION_STATE_EJERCICIO,
                                vibrationStateEjercicio
                            )
                        }
                    }

                    Preparacion -> {}
                }
            }, timePosition = { item, state, position ->
                when (item) {
                    Ciclos -> {}
                    Conjuntos -> {}
                    Descanso -> {
                        when (state) {
                            0 -> {
                                positionDescansoSegundos = position
                                CoroutineScope(Dispatchers.IO).launch {
                                    guardarIntPreferencias(
                                        TT_POSITION_DESCANSO_SEGUNDOS,
                                        positionDescansoSegundos
                                    )
                                }
                            }

                            1 -> {
                                positionDescansoMinutos = position
                                CoroutineScope(Dispatchers.IO).launch {
                                    guardarIntPreferencias(
                                        TT_POSITION_DESCANSO_MINUTOS,
                                        positionDescansoMinutos
                                    )
                                }
                            }

                            else -> {
                                positionDescansoHoras = position
                                CoroutineScope(Dispatchers.IO).launch {
                                    guardarIntPreferencias(
                                        TT_POSITION_DESCANSO_HORAS,
                                        positionDescansoHoras
                                    )
                                }
                            }
                        }
                    }

                    Descanso_entre_conjuntos -> {
                        when (state) {
                            0 -> {
                                positionDescansoEntreSegundos = position
                                CoroutineScope(Dispatchers.IO).launch {
                                    guardarIntPreferencias(
                                        TT_POSITION_DESCANSO_ENTRE_SEGUNDOS,
                                        positionDescansoEntreSegundos
                                    )
                                }
                            }

                            1 -> {
                                positionDescansoEntreMinutos = position
                                CoroutineScope(Dispatchers.IO).launch {
                                    guardarIntPreferencias(
                                        TT_POSITION_DESCANSO_ENTRE_MINUTOS,
                                        positionDescansoEntreMinutos
                                    )
                                }
                            }

                            else -> {
                                positionDescansoEntreHoras = position
                                CoroutineScope(Dispatchers.IO).launch {
                                    guardarIntPreferencias(
                                        TT_POSITION_DESCANSO_ENTRE_HORAS,
                                        positionDescansoEntreHoras
                                    )
                                }
                            }
                        }
                    }

                    Ejercicio -> {
                        when (state) {
                            0 -> {
                                positionEjercicioSegundos = position
                                CoroutineScope(Dispatchers.IO).launch {
                                    guardarIntPreferencias(
                                        TT_POSITION_EJERCICIO_SEGUNDOS,
                                        positionEjercicioSegundos
                                    )
                                }
                            }

                            1 -> {
                                positionEjercicioMinutos = position
                                CoroutineScope(Dispatchers.IO).launch {
                                    guardarIntPreferencias(
                                        TT_POSITION_EJERCICIO_MINUTOS,
                                        positionEjercicioMinutos
                                    )
                                }
                            }

                            else -> {
                                positionEjercicioHoras = position
                                CoroutineScope(Dispatchers.IO).launch {
                                    guardarIntPreferencias(
                                        TT_POSITION_EJERCICIO_HORAS,
                                        positionEjercicioHoras
                                    )
                                }
                            }
                        }
                    }

                    Preparacion -> {
                        when (state) {
                            0 -> {
                                positionPreparacionSegundos = position
                                CoroutineScope(Dispatchers.IO).launch {
                                    guardarIntPreferencias(
                                        TT_POSITION_PREPARACION_SEGUNDOS,
                                        positionPreparacionSegundos
                                    )
                                }
                            }

                            1 -> {
                                positionPreparacionMinutos = position
                                CoroutineScope(Dispatchers.IO).launch {
                                    guardarIntPreferencias(
                                        TT_POSITION_PREPARACION_MINUTOS,
                                        positionPreparacionMinutos
                                    )
                                }
                            }

                            else -> {
                                positionPreparacionHoras = position
                                CoroutineScope(Dispatchers.IO).launch {
                                    guardarIntPreferencias(
                                        TT_POSITION_PREPARACION_HORAS,
                                        positionPreparacionHoras
                                    )
                                }
                            }
                        }
                    }
                }
            }, trackPosition = { item, position ->
                when (item) {
                    Ciclos -> {}
                    Conjuntos -> {}
                    Descanso -> {
                        positionTrackDescanso = position
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarIntPreferencias(
                                TT_POSITION_TRACK_DESCANSO,
                                positionTrackDescanso
                            )
                        }
                    }

                    Descanso_entre_conjuntos -> {
                        positionTrackDescansoEntre = position
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarIntPreferencias(
                                TT_POSITION_TRACK_DESCANSO_ENTRE,
                                positionTrackDescansoEntre
                            )
                        }
                    }

                    Ejercicio -> {
                        positionTrackEjercicio = position
                        CoroutineScope(Dispatchers.IO).launch {
                            guardarIntPreferencias(
                                TT_POSITION_TRACK_EJERCICIO,
                                positionTrackEjercicio
                            )
                        }
                    }

                    Preparacion -> {}
                }
            })

        binding.rvSetTimers.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = setTabataTimerAdapter
        }
        binding.BTNBack.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.backgroundBTNBack, binding.shadowBTNBack)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("tabata_back", "X= $x")
                        Log.i("tabata_back", "Y= $y")
                        if (x > -1f && x < 130f && y > -3f && y < 132f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(binding.backgroundBTNBack, binding.shadowBTNBack)
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -1f && x < 130f && y > -3f && y < 132f && !stateAnimator) {
                            btnAnimation(binding.backgroundBTNBack, binding.shadowBTNBack)
                            val intentBack =
                                Intent(binding.BTNBack.context, MainActivity::class.java)
                            startActivity(intentBack)
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        binding.BTNSETRestart.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.backgroundBTNSETRestart, binding.shadowBTNSETRestart)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("tabata_setrestart", "X= $x")
                        Log.i("tabata_setrestart", "Y= $y")
                        if (x > -1f && x < 130f && y > -3f && y < 132f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(
                                    binding.backgroundBTNSETRestart,
                                    binding.shadowBTNSETRestart
                                )
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -1f && x < 130f && y > -3f && y < 132f && !stateAnimator) {
                            btnAnimation(
                                binding.backgroundBTNSETRestart,
                                binding.shadowBTNSETRestart
                            )
                            repeat(65){
                                resetSettings()
                            }
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        binding.BTNComenzar.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.backgroundBTNComenzar, binding.shadowBTNComenzar)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("tabata_comenzar", "X= $x")
                        Log.i("tabata_comenzar", "Y= $y")
                        if (x > -3f && x < 549f && y > -3f && y < 132f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(
                                    binding.backgroundBTNComenzar,
                                    binding.shadowBTNComenzar
                                )
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -3f && x < 549f && y > -3f && y < 132f && !stateAnimator) {
                            btnAnimation(binding.backgroundBTNComenzar, binding.shadowBTNComenzar)
                            if (tiempoTotal <= 360000) {
                                newList = mutableListOf()
                                createTabataList()
                                Log.i("katyushaTabataList", "Tamaño de la lista: ${newList.size}")
                                Log.i("katyushaTabataList", "Lista: $newList")
                                maxlimit = false
                            }
                            if (tiempoTotal > 360000 && !maxlimit) {
                                binding.tvTiempoTotal.text = "99:59:59"
                                Toast.makeText(
                                    binding.BTNComenzar.context,
                                    "Has superado el tiempo maximo total de 99:59:59",
                                    Toast.LENGTH_LONG
                                ).show()
                                maxlimit = true
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

    private fun navigateToTabataTimerPlaying(newList: MutableList<ItemsCollection>) {
        val intent = Intent(this, TabataTimerPlayingActivity::class.java)
        intent.putParcelableArrayListExtra(ITEMS_LIST, newList as ArrayList<out Parcelable>)
        intent.putExtra(CICLOS, countCiclos)
        intent.putExtra(CONJUNTOS, countConjuntos)
        intent.putExtra(TIEMPO_TOTAL, tiempoTotal)
        startActivity(intent)
    }

    private fun formatZeroZero(value: Int): String {
        val tiempo = String.format("%02d", value)
        return tiempo
    }

    private fun formatTotalTime(tiempo: Int) {
        val hours = tiempo / 3600
        val minutes = (tiempo % 3600) / 60
        val seconds = tiempo % 60

        val time = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        CoroutineScope(Dispatchers.IO).launch {
            guardarStringPreferencias(TT_TIEMPO_TOTAL, time)
        }
        binding.tvTiempoTotal.text = time
    }

    private fun createTabataList() {
        newList = mutableListOf()
        if (tiempoTotal <= 360000) {
            if (ejercicioTiempo > 0) {
                if (preparacionTiempo > 0) {
                    newList.add(ItemsCollection("preparación", preparacionTiempo, false, -1))
                }
                if (countConjuntos > 1) {
                    if (countCiclos > 1) {
                        repeat(countConjuntos - 1) {
                            repeat(countCiclos - 1) {
                                newList.add(
                                    ItemsCollection(
                                        "ejercicio",
                                        ejercicioTiempo,
                                        vibrationStateEjercicio,
                                        trackEjercicio
                                    )
                                )
                                if (descansoTiempo > 0) {
                                    newList.add(
                                        ItemsCollection(
                                            "descanso",
                                            descansoTiempo,
                                            vibrationStateDescanso,
                                            trackDescanso
                                        )
                                    )
                                }
                            }
                            newList.add(
                                ItemsCollection(
                                    "ejercicio",
                                    ejercicioTiempo,
                                    vibrationStateEjercicio,
                                    trackEjercicio
                                )
                            )
                            if (descansoEntreTiempo > 0) {
                                newList.add(
                                    ItemsCollection(
                                        "descanso entre conjuntos",
                                        descansoEntreTiempo,
                                        vibrationStateDescansoEntre,
                                        trackDescansoEntre
                                    )
                                )
                            }
                        }
                        repeat(countCiclos - 1) {
                            newList.add(
                                ItemsCollection(
                                    "ejercicio",
                                    ejercicioTiempo,
                                    vibrationStateEjercicio,
                                    trackEjercicio
                                )
                            )
                            if (descansoTiempo > 0) {
                                newList.add(
                                    ItemsCollection(
                                        "descanso",
                                        descansoTiempo,
                                        vibrationStateDescanso,
                                        trackDescanso
                                    )
                                )
                            }
                        }
                        newList.add(
                            ItemsCollection(
                                "ejercicio",
                                ejercicioTiempo,
                                vibrationStateEjercicio,
                                trackEjercicio
                            )
                        )

                    } else {
                        repeat(countConjuntos - 1) {
                            newList.add(
                                ItemsCollection(
                                    "ejercicio",
                                    ejercicioTiempo,
                                    vibrationStateEjercicio,
                                    trackEjercicio
                                )
                            )
                            if (descansoEntreTiempo > 0) {
                                newList.add(
                                    ItemsCollection(
                                        "descanso entre conjuntos",
                                        descansoEntreTiempo,
                                        vibrationStateDescansoEntre,
                                        trackDescansoEntre
                                    )
                                )
                            }
                        }
                        newList.add(
                            ItemsCollection(
                                "ejercicio",
                                ejercicioTiempo,
                                vibrationStateEjercicio,
                                trackEjercicio
                            )
                        )
                    }
                } else {
                    if (countCiclos != 1) {
                        repeat(countCiclos - 1) {
                            newList.add(
                                ItemsCollection(
                                    "ejercicio",
                                    ejercicioTiempo,
                                    vibrationStateEjercicio,
                                    trackEjercicio
                                )
                            )
                            if (descansoTiempo > 0) {
                                newList.add(
                                    ItemsCollection(
                                        "descanso",
                                        descansoTiempo,
                                        vibrationStateDescanso,
                                        trackDescanso
                                    )
                                )
                            }
                        }
                        newList.add(
                            ItemsCollection(
                                "ejercicio",
                                ejercicioTiempo,
                                vibrationStateEjercicio,
                                trackEjercicio
                            )
                        )
                    } else {
                        newList.add(
                            ItemsCollection(
                                "ejercicio",
                                ejercicioTiempo,
                                vibrationStateEjercicio,
                                trackEjercicio
                            )
                        )
                    }
                }
                newList.add(ItemsCollection("Fin", 0, true, R.raw.success_fanfare_trumpets))
                stateNOSetEjComenzar = false
                Log.i("tabata_newList", "Lista: $newList")
                navigateToTabataTimerPlaying(newList)
            } else {
                if (!stateNOSetEjComenzar) {
                    Toast.makeText(this, "Ajusta la duración del ejercicio", Toast.LENGTH_LONG)
                        .show()
                    stateNOSetEjComenzar = true
                }
            }

        } else {
            Toast.makeText(
                this,
                "Has superado el tiempo maximo total de 99 horas 59 minutos 59 segundos",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getSettings(): Flow<SettingsTabataTimerModel> {
        return dataStore.data.map { Preferencias ->
            SettingsTabataTimerModel(

                Preferencias[intPreferencesKey(TT_PREPARACION_HORAS)] ?: 0,
                Preferencias[intPreferencesKey(TT_PREPARACION_MINUTOS)] ?: 0,
                Preferencias[intPreferencesKey(TT_PREPARACION_SEGUNDOS)] ?: 0,
                Preferencias[intPreferencesKey(TT_EJERCICIO_HORAS)] ?: 0,
                Preferencias[intPreferencesKey(TT_EJERCICIO_MINUTOS)] ?: 0,
                Preferencias[intPreferencesKey(TT_EJERCICIO_SEGUNDOS)] ?: 0,
                Preferencias[intPreferencesKey(TT_DESCANSO_HORAS)] ?: 0,
                Preferencias[intPreferencesKey(TT_DESCANSO_MINUTOS)] ?: 0,
                Preferencias[intPreferencesKey(TT_DESCANSO_SEGUNDOS)] ?: 0,
                Preferencias[intPreferencesKey(TT_DESCANSO_ENTRE_CONJUNTOS_HORAS)]
                    ?: 0,
                Preferencias[intPreferencesKey(TT_DESCANSO_ENTRE_CONJUNTOS_MINUTOS)]
                    ?: 0,
                Preferencias[intPreferencesKey(TT_DESCANSO_ENTRE_CONJUNTOS_SEGUNDOS)]
                    ?: 0,
                Preferencias[intPreferencesKey(TT_COUNT_CICLOS)] ?: 1,
                Preferencias[intPreferencesKey(TT_COUNT_CONJUNTOS)] ?: 1,
                Preferencias[intPreferencesKey(TT_TIEMPOTOTAL)] ?: 0,
                Preferencias[intPreferencesKey(TT_PREPARACION_TIEMPO)] ?: 0,
                Preferencias[intPreferencesKey(TT_EJERCICIO_TIEMPO)] ?: 0,
                Preferencias[intPreferencesKey(TT_DESCANSO_TIEMPO)] ?: 0,
                Preferencias[intPreferencesKey(TT_DESCANSO_ENTRE_TIEMPO)] ?: 0,
                Preferencias[intPreferencesKey(TT_TRACK_EJERCICIO)] ?: -1,
                Preferencias[intPreferencesKey(TT_TRACK_DESCANSO)] ?: -1,
                Preferencias[intPreferencesKey(TT_TRACK_DESCANSO_ENTRE)] ?: -1,
                Preferencias[booleanPreferencesKey(TT_VIBRATION_STATE_EJERCICIO)]
                    ?: false,
                Preferencias[booleanPreferencesKey(TT_VIBRATION_STATE_DESCANSO)]
                    ?: false,
                Preferencias[booleanPreferencesKey(TT_VIBRATION_STATE_DESCANSO_ENTRE)]
                    ?: false,
                Preferencias[intPreferencesKey(TT_POSITION_DESCANSO_HORAS)] ?: 0,
                Preferencias[intPreferencesKey(TT_POSITION_DESCANSO_MINUTOS)] ?: 0,
                Preferencias[intPreferencesKey(TT_POSITION_DESCANSO_SEGUNDOS)] ?: 0,
                Preferencias[intPreferencesKey(TT_POSITION_DESCANSO_ENTRE_HORAS)] ?: 0,
                Preferencias[intPreferencesKey(TT_POSITION_DESCANSO_ENTRE_MINUTOS)] ?: 0,
                Preferencias[intPreferencesKey(TT_POSITION_DESCANSO_ENTRE_SEGUNDOS)] ?: 0,
                Preferencias[intPreferencesKey(TT_POSITION_EJERCICIO_HORAS)] ?: 0,
                Preferencias[intPreferencesKey(TT_POSITION_EJERCICIO_MINUTOS)] ?: 0,
                Preferencias[intPreferencesKey(TT_POSITION_EJERCICIO_SEGUNDOS)] ?: 0,
                Preferencias[intPreferencesKey(TT_POSITION_PREPARACION_HORAS)] ?: 0,
                Preferencias[intPreferencesKey(TT_POSITION_PREPARACION_MINUTOS)] ?: 0,
                Preferencias[intPreferencesKey(TT_POSITION_PREPARACION_SEGUNDOS)] ?: 0,
                Preferencias[intPreferencesKey(TT_POSITION_TRACK_DESCANSO)] ?: 0,
                Preferencias[intPreferencesKey(TT_POSITION_TRACK_DESCANSO_ENTRE)] ?: 0,
                Preferencias[intPreferencesKey(TT_POSITION_TRACK_EJERCICIO)] ?: 0
            )
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

    private suspend fun guardarStringPreferencias(key: String, value: String) {
        dataStore.edit { Preferencias ->
            Preferencias[stringPreferencesKey(key)] = value
        }
    }

    private fun stateSettings() {
        CoroutineScope(Dispatchers.IO).launch {
            getSettings().filter { firstTime }.collectLatest { settings ->
                if (settings != null) {
                    withContext(Dispatchers.Main) {
                        Log.i("faro", "el tiempo total WithContext: $tiempoTotal")
                        preparacionHoras = settings.preparacion_horas
                        preparacionMinutos = settings.preparacion_minutos
                        preparacionSegundos = settings.preparacion_segundos
                        ejercicioHoras = settings.ejercicios_horas
                        ejercicioMinutos = settings.ejercicios_minutos
                        ejercicioSegundos = settings.ejercicios_segundos
                        descansoHoras = settings.descanso_horas
                        descansoMinutos = settings.descanso_minutos
                        descansoSegundos = settings.descanso_segundos
                        descansoEntreHoras = settings.descanso_entre_conjuntos_horas
                        descansoEntreMinutos = settings.descanso_entre_conjuntos_minutos
                        descansoEntreSegundos = settings.descanso_entre_conjuntos_segundos
                        countCiclos = settings.count_ciclos
                        countConjuntos = settings.count_conjuntos
                        tiempoTotal = settings.tiempoTotal
                        preparacionTiempo = settings.preparacion_tiempo
                        ejercicioTiempo = settings.ejercicio_tiempo
                        descansoTiempo = settings.descanso_tiempo
                        descansoEntreTiempo = settings.descanso_entre_tiempo
                        trackEjercicio = settings.track_ejercicio
                        trackDescanso = settings.track_descanso
                        trackDescansoEntre = settings.track_descanso_entre
                        vibrationStateEjercicio = settings.vibration_state_ejercicio
                        vibrationStateDescanso = settings.vibration_state_descanso
                        vibrationStateDescansoEntre = settings.vibration_state_descanso_entre
                        positionDescansoHoras = settings.positionDescansoHoras
                        positionDescansoMinutos = settings.positionDescansoMinutos
                        positionDescansoSegundos = settings.positionDescansoSegundos
                        positionDescansoEntreHoras = settings.positionDescansoEntreHoras
                        positionDescansoEntreMinutos = settings.positionDescansoEntreMinutos
                        positionDescansoEntreSegundos = settings.positionDescansoEntreSegundos
                        positionEjercicioHoras = settings.positionEjercicioHoras
                        positionEjercicioMinutos = settings.positionEjercicioMinutos
                        positionEjercicioSegundos = settings.positionEjercicioSegundos
                        positionPreparacionHoras = settings.positionPreparacionHoras
                        positionPreparacionMinutos = settings.positionPreparacionMinutos
                        positionPreparacionSegundos = settings.positionPreparacionSegundos
                        positionTrackDescanso = settings.positionTrackDescanso
                        positionTrackDescansoEntre = settings.positionTrackDescansoEntre
                        positionTrackEjercicio = settings.positionTrackEjercicio
                        firstTime = !firstTime
                        if (tiempoTotal <= 360000) {
                            formatTotalTime(tiempoTotal)
                            maxlimit = false
                        }
                        if (tiempoTotal > 360000 && !maxlimit) {
                            binding.tvTiempoTotal.text = "99:59:59"
                            maxlimit = true
                        }
                        settingsRV()
                        binding.tvIntervalos.text = formatZeroZero(countCiclos)
                        binding.tvConjuntos.text = formatZeroZero(countConjuntos)
                    }
                }
            }
        }
    }

    private fun stateSettingsTime() {
        CoroutineScope(Dispatchers.IO).launch {
            getSettings().collect { settings ->
                if (settings != null) {
                    withContext(Dispatchers.Main) {
                        positionDescansoHoras = settings.positionDescansoHoras
                        positionDescansoMinutos = settings.positionDescansoMinutos
                        positionDescansoSegundos = settings.positionDescansoSegundos
                        positionDescansoEntreHoras = settings.positionDescansoEntreHoras
                        positionDescansoEntreMinutos = settings.positionDescansoEntreMinutos
                        positionDescansoEntreSegundos = settings.positionDescansoEntreSegundos
                        positionEjercicioHoras = settings.positionEjercicioHoras
                        positionEjercicioMinutos = settings.positionEjercicioMinutos
                        positionEjercicioSegundos = settings.positionEjercicioSegundos
                        positionPreparacionHoras = settings.positionPreparacionHoras
                        positionPreparacionMinutos = settings.positionPreparacionMinutos
                        positionPreparacionSegundos = settings.positionPreparacionSegundos
                        positionTrackDescanso = settings.positionTrackDescanso
                        positionTrackDescansoEntre = settings.positionTrackDescansoEntre
                        positionTrackEjercicio = settings.positionTrackEjercicio
                        settingsTimeRV()
                    }
                }
            }
        }
    }

    private fun settingsRV() {
        setTabataTimerAdapter.initSettingsRV(
            preparacionHoras,
            preparacionMinutos,
            preparacionMinutos,
            ejercicioHoras,
            ejercicioMinutos,
            ejercicioSegundos,
            descansoHoras,
            descansoMinutos,
            descansoSegundos,
            descansoEntreHoras,
            descansoEntreMinutos,
            descansoEntreSegundos,
            countCiclos,
            countConjuntos,
            trackEjercicio,
            trackDescanso,
            trackDescansoEntre,
            vibrationStateEjercicio,
            vibrationStateDescanso,
            vibrationStateDescansoEntre,
            positionTrackDescanso,
            positionTrackDescansoEntre,
            positionTrackEjercicio
        )
    }

    private fun settingsTimeRV() {
        setTabataTimerAdapter.initSettingsTime(
            positionPreparacionHoras,
            positionPreparacionMinutos,
            positionPreparacionSegundos,
            positionEjercicioHoras,
            positionEjercicioMinutos,
            positionEjercicioSegundos,
            positionDescansoHoras,
            positionDescansoMinutos,
            positionDescansoSegundos,
            positionDescansoEntreHoras,
            positionDescansoEntreMinutos,
            positionDescansoEntreSegundos
        )
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

    private fun resetSettings() {
        preparacionHoras = 0
        preparacionMinutos = 0
        preparacionSegundos = 0
        ejercicioHoras = 0
        ejercicioMinutos = 0
        ejercicioSegundos = 0
        descansoHoras = 0
        descansoMinutos = 0
        descansoSegundos = 0
        descansoEntreHoras = 0
        descansoEntreMinutos = 0
        descansoEntreSegundos = 0
        countCiclos = 1
        countConjuntos = 1
        tiempoTotal = 0
        preparacionTiempo = 0
        ejercicioTiempo = 0
        descansoTiempo = 0
        descansoEntreTiempo = 0
        trackEjercicio = -1
        trackDescanso = -1
        trackDescansoEntre = -1
        vibrationStateEjercicio = false
        vibrationStateDescanso = false
        vibrationStateDescansoEntre = false
        positionDescansoHoras = 0
        positionDescansoMinutos = 0
        positionDescansoSegundos = 0
        positionDescansoEntreHoras = 0
        positionDescansoEntreMinutos = 0
        positionDescansoEntreSegundos = 0
        positionEjercicioHoras = 0
        positionEjercicioMinutos = 0
        positionEjercicioSegundos = 0
        positionPreparacionHoras = 0
        positionPreparacionMinutos = 0
        positionPreparacionSegundos = 0
        positionTrackDescanso = 0
        positionTrackDescansoEntre = 0
        positionTrackEjercicio = 0
        CoroutineScope(Dispatchers.IO).launch {
            guardarIntPreferencias(TT_PREPARACION_HORAS, preparacionHoras)
            guardarIntPreferencias(TT_PREPARACION_MINUTOS, preparacionMinutos)
            guardarIntPreferencias(TT_PREPARACION_SEGUNDOS, preparacionSegundos)
            guardarIntPreferencias(TT_EJERCICIO_HORAS, ejercicioHoras)
            guardarIntPreferencias(TT_EJERCICIO_MINUTOS, ejercicioMinutos)
            guardarIntPreferencias(TT_EJERCICIO_SEGUNDOS, ejercicioSegundos)
            guardarIntPreferencias(TT_DESCANSO_HORAS, descansoHoras)
            guardarIntPreferencias(TT_DESCANSO_MINUTOS, descansoMinutos)
            guardarIntPreferencias(TT_DESCANSO_SEGUNDOS, descansoSegundos)
            guardarIntPreferencias(TT_DESCANSO_ENTRE_CONJUNTOS_HORAS, descansoEntreHoras)
            guardarIntPreferencias(TT_DESCANSO_ENTRE_CONJUNTOS_MINUTOS, descansoEntreMinutos)
            guardarIntPreferencias(TT_DESCANSO_ENTRE_CONJUNTOS_SEGUNDOS, descansoEntreSegundos)
            guardarIntPreferencias(TT_COUNT_CICLOS, countCiclos)
            guardarIntPreferencias(TT_COUNT_CONJUNTOS, countConjuntos)
            guardarIntPreferencias(TT_TIEMPOTOTAL, tiempoTotal)
            guardarIntPreferencias(TT_PREPARACION_TIEMPO, preparacionTiempo)
            guardarIntPreferencias(TT_EJERCICIO_TIEMPO, ejercicioTiempo)
            guardarIntPreferencias(TT_DESCANSO_TIEMPO, descansoTiempo)
            guardarIntPreferencias(TT_DESCANSO_ENTRE_TIEMPO, descansoEntreTiempo)
            guardarIntPreferencias(TT_TRACK_EJERCICIO, trackEjercicio)
            guardarIntPreferencias(TT_TRACK_DESCANSO, trackDescanso)
            guardarIntPreferencias(TT_TRACK_DESCANSO_ENTRE, trackDescansoEntre)
            guardarPreferencias(TT_VIBRATION_STATE_EJERCICIO, vibrationStateEjercicio)
            guardarPreferencias(TT_VIBRATION_STATE_DESCANSO, vibrationStateDescanso)
            guardarPreferencias(TT_VIBRATION_STATE_DESCANSO_ENTRE, vibrationStateDescansoEntre)
            guardarIntPreferencias(TT_POSITION_DESCANSO_HORAS, positionDescansoHoras)
            guardarIntPreferencias(TT_POSITION_DESCANSO_MINUTOS, positionDescansoMinutos)
            guardarIntPreferencias(TT_POSITION_DESCANSO_SEGUNDOS, positionDescansoSegundos)
            guardarIntPreferencias(TT_POSITION_DESCANSO_ENTRE_HORAS, positionDescansoEntreHoras)
            guardarIntPreferencias(
                TT_POSITION_DESCANSO_ENTRE_MINUTOS,
                positionDescansoEntreMinutos
            )
            guardarIntPreferencias(
                TT_POSITION_DESCANSO_ENTRE_SEGUNDOS,
                positionDescansoEntreSegundos
            )
            guardarIntPreferencias(TT_POSITION_EJERCICIO_HORAS, positionEjercicioHoras)
            guardarIntPreferencias(TT_POSITION_EJERCICIO_MINUTOS, positionEjercicioMinutos)
            guardarIntPreferencias(TT_POSITION_EJERCICIO_SEGUNDOS, positionEjercicioSegundos)
            guardarIntPreferencias(TT_POSITION_PREPARACION_HORAS, positionPreparacionHoras)
            guardarIntPreferencias(TT_POSITION_PREPARACION_MINUTOS, positionPreparacionMinutos)
            guardarIntPreferencias(
                TT_POSITION_PREPARACION_SEGUNDOS,
                positionPreparacionSegundos
            )
            guardarIntPreferencias(TT_POSITION_TRACK_DESCANSO, positionTrackDescanso)
            guardarIntPreferencias(TT_POSITION_TRACK_DESCANSO_ENTRE, positionTrackDescansoEntre)
            guardarIntPreferencias(TT_POSITION_TRACK_EJERCICIO, positionTrackEjercicio)
        }
        settingsRV()
        settingsTimeRV()
        val intent = Intent(this, TabataTimerActivity::class.java)
        startActivity(intent)
        formatTotalTime(tiempoTotal)
        binding.tvIntervalos.text = formatZeroZero(countCiclos)
        binding.tvConjuntos.text = formatZeroZero(countConjuntos)
    }
}