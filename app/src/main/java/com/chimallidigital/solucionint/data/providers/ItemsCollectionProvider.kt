package com.chimallidigital.solucionint.data.providers

import com.chimallidigital.solucionint.domain.model.TabataTimer.ItemsCollection
import javax.inject.Inject

class ItemsCollectionProvider @Inject constructor(){
    fun getItems(): List<ItemsCollection>{
        return listOf(

        )
    }
}