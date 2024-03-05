package com.chimallidigital.solucionint.domain.model.TabataTimer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemsCollection(
    val tipo: String,
    val tiempo: Int,
    val vibration: Boolean,
    val track: Int
) : Parcelable