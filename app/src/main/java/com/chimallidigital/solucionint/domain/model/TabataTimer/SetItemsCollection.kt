package com.chimallidigital.solucionint.domain.model.TabataTimer

import com.chimallidigital.solucionint.R

sealed class SetItemsCollection(val title: Int, val img: Int, val stateSetTiempo: Int, val stateSonido: Int, val stateVibration: Int) {
    data object Preparacion: SetItemsCollection(R.string.preparacion, R.drawable.preparacion, 1,0, 0 )
    data object Ejercicio: SetItemsCollection(R.string.ejercicio, R.drawable.ejercicio, 1,1,1)
    data object Descanso: SetItemsCollection(R.string.descanso,  R.drawable.descanso, 1,1, 1)
    data object Ciclos: SetItemsCollection(R.string.ciclos,  R.drawable.ciclos, 0,1, 1)
    data object Conjuntos: SetItemsCollection(R.string.conjuntos,  R.drawable.conjuntos, 0,1, 1)
    data object Descanso_entre_conjuntos: SetItemsCollection(R.string.descanso_entre_conjuntos, R.drawable.descanso_entre_conjuntos,1,1,1)
}