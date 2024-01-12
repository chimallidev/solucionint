package com.chimallidigital.solucionint.data.providers

import com.chimallidigital.solucionint.domain.model.scientific_articles.ScientificArticlesCategories
import com.chimallidigital.solucionint.domain.model.scientific_articles.ScientificArticlesCategories.*
import javax.inject.Inject

class ScientificArticlesProvider @Inject constructor() {
    fun getScientificArticlesCategories(): List<ScientificArticlesCategories>{
        return listOf(
            SolucionesInteligentes,
            Ponte_en_Forma,
            Recetas_de_Cocina
        )
    }
}