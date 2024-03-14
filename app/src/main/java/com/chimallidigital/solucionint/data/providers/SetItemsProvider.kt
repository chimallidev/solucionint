package com.chimallidigital.solucionint.data.providers

import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection.*
import javax.inject.Inject

class SetItemsProvider @Inject constructor() {
    fun getSetItems(): List<SetItemsCollection>{
        return listOf(
            Preparacion,
            Ejercicio,
            Descanso,
            Ciclos,
            Conjuntos,
            Descanso_entre_conjuntos
        )
    }
}