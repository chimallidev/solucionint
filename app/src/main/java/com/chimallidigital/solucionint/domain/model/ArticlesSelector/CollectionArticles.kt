package com.chimallidigital.solucionint.domain.model.ArticlesSelector

import com.chimallidigital.solucionint.R

sealed class CollectionArticles(val img: Int, val title: Int, val url: Int) {
    data object Art001: CollectionArticles(R.drawable.art001, R.string.art001, R.string.art001_url)
}