package com.chimallidigital.solucionint.ui.selector

import androidx.lifecycle.ViewModel
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.domain.model.scientific_articles.ScientificArticlesCategories
import com.chimallidigital.solucionint.domain.model.scientific_articles.ScientificArticlesCategoriesModel
import com.chimallidigital.solucionint.domain.model.scientific_articles.ScientificArticlesCategoriesModel.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticlesSelectorViewModel @Inject constructor(): ViewModel() {
    lateinit var name: ScientificArticlesCategoriesModel

    fun getCategoryName(category: ScientificArticlesCategoriesModel): Int {
        name = category

        val categoryName = when (name) {
            Soluciones_Inteligentes -> R.string.soluciones_inteligentes
            Ponte_en_Forma -> R.string.ponte_en_forma
            Recetas_de_Cocina -> R.string.recetas_de_cocina
        }
        return categoryName
    }
}