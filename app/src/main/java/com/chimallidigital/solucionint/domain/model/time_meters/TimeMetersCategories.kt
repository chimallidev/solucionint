package com.chimallidigital.solucionint.domain.model.time_meters

import com.chimallidigital.solucionint.R

sealed class TimeMetersCategories(val titulo: Int, val img: Int) {
    data object cronometro: TimeMetersCategories(R.string.cronometro, R.drawable.cronometro)
    data object temporizador: TimeMetersCategories(R.string.temporizador, R.drawable.temporizador)
    data object tabata_timer: TimeMetersCategories(R.string.tabata_timer, R.drawable.tabata_timer)
}