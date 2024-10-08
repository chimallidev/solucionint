package com.chimallidigital.solucionint.data.providers

import com.chimallidigital.solucionint.domain.model.ArticlesSelector.CollectionArticles
import com.chimallidigital.solucionint.domain.model.ArticlesSelector.CollectionArticles.*
import javax.inject.Inject

class CollectionPonte_en_FormaProvider @Inject constructor(){

    fun getPonteEnFormaArticlesProvider(): List<CollectionArticles>{

        return listOf(
            Art091,
            Art080,
            Art079,
            Art078,
            Art077,
            Art076,
            Art070,
            Art068,
            Art067,
            Art065,
            Art064,
            Art063,
            Art062,
            Art061,
            Art060,
            Art056,
            Art053,
            Art052,
            Art050,
            Art045,
            Art043,
            Art042,
            Art041,
            Art040,
            Art038,
            Art033,
            Art028,
            Art027,
            Art024,
            Art022,
            Art021,
            Art018,
            Art017,
            Art010,
            Art009,
            Art008,
            Art007,
            Art006,
            Art005,
            Art004,
            Art003,
            Art002,
            Art001
        )

    }
}