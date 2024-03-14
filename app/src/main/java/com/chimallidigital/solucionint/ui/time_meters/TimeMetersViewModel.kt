package com.chimallidigital.solucionint.ui.time_meters

import androidx.lifecycle.ViewModel
import com.chimallidigital.solucionint.data.providers.TimeMetersProvider
import com.chimallidigital.solucionint.domain.model.time_meters.TimeMetersCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TimeMetersViewModel @Inject constructor(timeMetersProvider: TimeMetersProvider): ViewModel() {
    private var _categoriesTimeMeters= MutableStateFlow<List<TimeMetersCategories>>(emptyList())
    val categoriesTimeMeters: StateFlow<List<TimeMetersCategories>> = _categoriesTimeMeters

    init {
        _categoriesTimeMeters.value= timeMetersProvider.getTimeMeters()
    }
}