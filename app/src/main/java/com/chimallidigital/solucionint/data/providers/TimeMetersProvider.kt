package com.chimallidigital.solucionint.data.providers

import com.chimallidigital.solucionint.domain.model.time_meters.TimeMetersCategories
import com.chimallidigital.solucionint.domain.model.time_meters.TimeMetersCategories.*
import javax.inject.Inject

class TimeMetersProvider @Inject constructor(){
    fun getTimeMeters(): List<TimeMetersCategories>{
        return listOf(
            cronometro,
            temporizador,
            tabata_timer
        )
    }
}