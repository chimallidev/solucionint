package com.chimallidigital.solucionint.domain.model.scientific_articles

import com.chimallidigital.solucionint.R

sealed class ScientificArticlesCategories(val img: Int, val category: Int) {
    data object SolucionesInteligentes:
        ScientificArticlesCategories(R.drawable.soluciones_inteligentes, R.string.soluciones_inteligentes)
    data object Ponte_en_Forma:
        ScientificArticlesCategories(R.drawable.ponte_en_forma, R.string.ponte_en_forma)
    data object Recetas_de_Cocina:
        ScientificArticlesCategories(R.drawable.recetas_de_cocina, R.string.recetas_de_cocina)
}