package com.chimallidigital.solucionint.ui.scientific_articles.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chimallidigital.solucionint.databinding.ItemScientificArticlesBinding
import com.chimallidigital.solucionint.domain.model.scientific_articles.ScientificArticlesCategories

class ScientificArticlesViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding= ItemScientificArticlesBinding.bind(view)

    fun render(scientificArticlesCategories: ScientificArticlesCategories){
        val context= binding.tvRvScientificArticles.context

        binding.ivRvScientificArticles.setImageResource(scientificArticlesCategories.img)
        binding.tvRvScientificArticles.text= context.getString(scientificArticlesCategories.category)
    }
}