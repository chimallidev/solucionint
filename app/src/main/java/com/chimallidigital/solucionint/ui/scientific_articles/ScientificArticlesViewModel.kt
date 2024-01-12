package com.chimallidigital.solucionint.ui.scientific_articles

import androidx.lifecycle.ViewModel
import com.chimallidigital.solucionint.data.providers.ScientificArticlesProvider
import com.chimallidigital.solucionint.domain.model.scientific_articles.ScientificArticlesCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ScientificArticlesViewModel @Inject constructor(scientificArticlesProvider: ScientificArticlesProvider): ViewModel(){
    private var _categoriesArticles= MutableStateFlow<List<ScientificArticlesCategories>>(emptyList())
    val categoriesArticles: StateFlow<List<ScientificArticlesCategories>> = _categoriesArticles

    init {
        _categoriesArticles.value= scientificArticlesProvider.getScientificArticlesCategories()
    }
}