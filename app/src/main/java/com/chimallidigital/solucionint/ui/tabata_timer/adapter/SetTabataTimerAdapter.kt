package com.chimallidigital.solucionint.ui.tabata_timer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection

class SetTabataTimerAdapter(
    private var setItemsList: List<SetItemsCollection> = emptyList(),
    private val itemOnSelected: (SetItemsCollection, hora: Int, minutos: Int, segundos: Int) -> Unit,
    private val count: (SetItemsCollection, count: Int) -> Unit,
    private val trackSave: (SetItemsCollection, track: Int) -> Unit,
    private val vibration: (SetItemsCollection, vibrationState: Boolean)-> Unit,
    private var preparacionHoras: Int=0,
    private var preparacionMinutos: Int=0,
    private var preparacionSegundos: Int=0,
    private var ejercicioHoras: Int=0,
    private var ejercicioMinutos: Int=0,
    private var ejercicioSegundos: Int=0,
    private var descansoHoras: Int=0,
    private var descansoMinutos: Int=0,
    private var descansoSegundos: Int=0,
    private var descansoEntreHoras: Int=0,
    private var descansoEntreMinutos: Int=0,
    private var descansoEntreSegundos: Int=0,
    private var countCiclos: Int=0,
    private var countConjuntos: Int=0,
    private var trackEjercicio: Int=0,
    private var trackDescanso: Int=0,
    private var trackDescansoEntre: Int=0,
    private var vibrationStateEjercicio: Boolean=false,
    private var vibrationStateDescanso: Boolean=false,
    private var vibrationStateDescansoEntre: Boolean=false,
    private var timePosition:(SetItemsCollection, state: Int, position:Int) -> Unit,
    private val trackPosition:(SetItemsCollection, numberTrackPosition: Int) ->Unit,
    private var positionTrackDescanso: Int=0,
    private var positionTrackDescansoEntre: Int=0,
    private var positionTrackEjercicio: Int=0,
    private var positionPrepHoras: Int=0,
    private var positionPrepMinutos: Int=0,
    private var positionPrepSegundos: Int=0,
    private var positionEjHoras: Int=0,
    private var positionEjMinutos: Int=0,
    private var positionEjSegundos: Int=0,
    private var positionDescHoras: Int=0,
    private var positionDescMinutos: Int=0,
    private var positionDescSegundos: Int=0,
    private var positionDescEntreHoras: Int=0,
    private var positionDescEntreMinutos: Int=0,
    private var positionDescEntreSegundos: Int=0
) :
    RecyclerView.Adapter<SetTabataTimerViewHolder>() {
        fun initSettingsTime(posPrepH: Int, posPrepM: Int, posPrepS: Int,
                               posEjH: Int, posEjM: Int, posEjS: Int, posDescH: Int, posDescM: Int, posDescS: Int, posDescEntreH: Int,
                               posDescEntreM: Int, posDescEntreS: Int){
            positionPrepHoras= posPrepH
            positionPrepMinutos= posPrepM
            positionPrepSegundos= posPrepS
            positionEjHoras= posEjH
            positionEjMinutos= posEjM
            positionEjSegundos= posEjS
            positionDescHoras= posDescH
            positionDescMinutos= posDescM
            positionDescSegundos= posDescS
            positionDescEntreHoras= posDescEntreH
            positionDescEntreMinutos= posDescEntreM
            positionDescEntreSegundos= posDescEntreS
        }
        fun initSettingsRV(prepH: Int, prepM:Int, prepS:Int, ejerH:Int, ejerM: Int, ejerS:Int, descH:Int, descM: Int, descS: Int,
                           descEnHoras: Int, descEnMinutos: Int, descEnSegundos: Int, countCic: Int, countCon: Int,
                           TrackEj: Int, TrackDes: Int, TrackDescEntre: Int, vibEjer: Boolean, vibDesc: Boolean, vibDescEntre: Boolean,
                           posTrackDesc: Int, posTrackDesEntre: Int, posTrackEj: Int){
            preparacionHoras= prepH
            preparacionMinutos= prepM
            preparacionSegundos= prepS
            ejercicioHoras= ejerH
            ejercicioMinutos= ejerM
            ejercicioSegundos= ejerS
            descansoHoras= descH
            descansoMinutos= descM
            descansoSegundos= descS
            descansoEntreHoras= descEnHoras
            descansoEntreMinutos= descEnMinutos
            descansoEntreSegundos= descEnSegundos
            countCiclos= countCic
            countConjuntos= countCon
            trackEjercicio= TrackEj
            trackDescanso= TrackDes
            trackDescansoEntre= TrackDescEntre
            vibrationStateEjercicio= vibEjer
            vibrationStateDescanso= vibDesc
            vibrationStateDescansoEntre= vibDescEntre
            positionTrackDescanso= posTrackDesc
            positionTrackDescansoEntre= posTrackDesEntre
            positionTrackEjercicio= posTrackEj
            notifyDataSetChanged()

        }


    fun UpdateList(list: List<SetItemsCollection>) {
        setItemsList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetTabataTimerViewHolder {
        return SetTabataTimerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_set_tabata_timer, parent, false)
        )
    }

    override fun getItemCount() = setItemsList.size

    override fun onBindViewHolder(holder: SetTabataTimerViewHolder, position: Int) {
        holder.render(setItemsList[position], itemOnSelected, count, trackSave, vibration,
            timePosition, trackPosition,
            preparacionHoras, preparacionMinutos, preparacionSegundos,
            ejercicioHoras, ejercicioMinutos, ejercicioSegundos,
            descansoHoras, descansoMinutos, descansoSegundos,
            descansoEntreHoras, descansoEntreMinutos, descansoEntreSegundos,
            countCiclos, countConjuntos, trackEjercicio, trackDescanso, trackDescansoEntre,
            vibrationStateEjercicio, vibrationStateDescanso, vibrationStateDescansoEntre, positionTrackDescanso,
            positionTrackDescansoEntre, positionTrackEjercicio, positionPrepHoras, positionPrepMinutos, positionPrepSegundos,
            positionDescHoras, positionDescMinutos, positionDescSegundos, positionDescEntreHoras, positionDescEntreMinutos, positionDescEntreSegundos,
            positionEjHoras, positionEjMinutos, positionEjSegundos
        )
    }
}