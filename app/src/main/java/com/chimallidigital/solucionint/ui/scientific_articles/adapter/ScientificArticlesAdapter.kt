package com.chimallidigital.solucionint.ui.scientific_articles.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.domain.model.scientific_articles.ScientificArticlesCategories

class ScientificArticlesAdapter(
    private var scientificArticlesCategoriesList: List<ScientificArticlesCategories> = emptyList(),
    private val itemOnSelected: (ScientificArticlesCategories) -> Unit
) :
    RecyclerView.Adapter<ScientificArticlesViewHolder>() {

    fun updateList(list: List<ScientificArticlesCategories>) {
        scientificArticlesCategoriesList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScientificArticlesViewHolder {
        return ScientificArticlesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_scientific_articles, parent, false)
        )
    }

    override fun getItemCount() = scientificArticlesCategoriesList.size

    override fun onBindViewHolder(holder: ScientificArticlesViewHolder, position: Int) {
            holder.render(scientificArticlesCategoriesList[position], itemOnSelected)
        holder.itemScientific.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_rv_anim)
    }
}