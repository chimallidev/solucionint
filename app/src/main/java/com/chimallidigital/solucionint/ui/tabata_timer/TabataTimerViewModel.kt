package com.chimallidigital.solucionint.ui.tabata_timer

import androidx.lifecycle.ViewModel
import com.chimallidigital.solucionint.data.providers.ItemsCollectionProvider
import com.chimallidigital.solucionint.data.providers.SetItemsProvider
import com.chimallidigital.solucionint.domain.model.TabataTimer.ItemsCollection
import com.chimallidigital.solucionint.domain.model.TabataTimer.SetItemsCollection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TabataTimerViewModel @Inject constructor(
    setItemsProvider: SetItemsProvider,
    itemsCollectionProvider: ItemsCollectionProvider
): ViewModel(){
    private var _items= MutableStateFlow<List<SetItemsCollection>>(emptyList())
    val items: StateFlow<List<SetItemsCollection>> = _items
    private var _tabataItems= MutableStateFlow<List<ItemsCollection>>(emptyList())
    val tabataItems: StateFlow<List<ItemsCollection>> = _tabataItems

    init {
        _items.value= setItemsProvider.getSetItems()
        _tabataItems.value= itemsCollectionProvider.getItems()
    }
}