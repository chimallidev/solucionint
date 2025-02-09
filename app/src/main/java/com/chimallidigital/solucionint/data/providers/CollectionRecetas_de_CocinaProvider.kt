package com.chimallidigital.solucionint.data.providers

import com.chimallidigital.solucionint.domain.model.ArticlesSelector.CollectionArticles
import com.chimallidigital.solucionint.domain.model.ArticlesSelector.CollectionArticles.*
import javax.inject.Inject

class CollectionRecetas_de_CocinaProvider @Inject constructor() {

    fun getRecetasDeCocinaArticlesProvider(): List<CollectionArticles>{
        return listOf(
            Art090,
            Art089,
            Art088,
            Art087,
            Art086,
            Art085,
            Art084,
            Art083,
            Art082,
            Art081,
            Art075,
            Art074,
            Art073,
            Art072,
            Art071,
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