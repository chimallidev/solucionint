package com.chimallidigital.solucionint.ui.cronometro

import androidx.lifecycle.ViewModel
import com.chimallidigital.solucionint.data.providers.SplitsProvider
import com.chimallidigital.solucionint.domain.model.Cronometro.Splits
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CronometroViewModel @Inject constructor(splitsProvider: SplitsProvider): ViewModel(){
    private var _splits=MutableStateFlow<List<Splits>>(emptyList())
    val splits: StateFlow<List<Splits>> = _splits

    init {
        _splits.value= splitsProvider.getSplits()
    }
}