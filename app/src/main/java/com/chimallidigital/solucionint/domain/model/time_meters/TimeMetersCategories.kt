package com.chimallidigital.solucionint.domain.model.time_meters

import com.chimallidigital.solucionint.R

sealed class TimeMetersCategories(val titulo: Int) {
    data object cronometro: TimeMetersCategories(R.string.cronometro)
    data object temporizador: TimeMetersCategories(R.string.temporizador)
    data object tabata_timer: TimeMetersCategories(R.string.tabata_timer)
}