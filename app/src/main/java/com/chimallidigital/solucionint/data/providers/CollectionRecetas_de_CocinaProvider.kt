package com.chimallidigital.solucionint.data.providers

import com.chimallidigital.solucionint.domain.model.ArticlesSelector.CollectionArticles
import com.chimallidigital.solucionint.domain.model.ArticlesSelector.CollectionArticles.*
import javax.inject.Inject

class CollectionRecetas_de_CocinaProvider @Inject constructor() {

    fun getRecetasDeCocinaArticlesProvider(): List<CollectionArticles>{
        return listOf(
            Art069,
            Art066,
            Art059,
            Art058,
            Art057,
            Art055,
            Art054,
            Art051,
            Art049,
            Art048,
            Art047,
            Art046,
            Art044,
            Art039,
            Art037,
            Art036,
            Art035,
            Art034,
            Art032,
            Art031,
            Art030,
            Art029,
            Art026,
            Art025,
            Art023,
            Art020,
            Art019,
            Art016,
            Art015,
            Art014,
            Art013,
            Art012,
            Art011
        )
    }
}