package com.chimallidigital.solucionint.ui.scientific_articles.adapter

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ItemScientificArticlesBinding
import com.chimallidigital.solucionint.domain.model.scientific_articles.ScientificArticlesCategories

class ScientificArticlesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemScientificArticlesBinding.bind(view)

    fun render(
        scientificArticlesCategories: ScientificArticlesCategories,
        itemOnSelected: (ScientificArticlesCategories) -> Unit
    ) {
        val context = binding.tvRvScientificArticles.context

        binding.ivRvScientificArticles.setImageResource(scientificArticlesCategories.img)
        binding.tvRvScientificArticles.text =
            context.getString(scientificArticlesCategories.category)

        binding.itemScientificArticles.setOnClickListener {
            zoomOutAnimation(it, newLambda = { itemOnSelected(scientificArticlesCategories) })
        }
    }

    private fun zoomOutAnimation(view: View, newLambda: () -> Unit) {
        val zoomOutItemAnimation = AnimationUtils.loadAnimation(view.context, R.anim.zoom_out)
        zoomOutItemAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                newLambda()
            }

            override fun onAnimationRepeat(animation: Animation?) {}

        })
        view.startAnimation(zoomOutItemAnimation)
    }
}