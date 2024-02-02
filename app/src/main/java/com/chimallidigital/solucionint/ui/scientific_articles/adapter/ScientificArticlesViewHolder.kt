package com.chimallidigital.solucionint.ui.scientific_articles.adapter

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ItemScientificArticlesBinding
import com.chimallidigital.solucionint.domain.model.scientific_articles.ScientificArticlesCategories

class ScientificArticlesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemScientificArticlesBinding.bind(view)
    val itemScientific = binding.itemScientificArticles

    fun render(
        scientificArticlesCategories: ScientificArticlesCategories,
        itemOnSelected: (ScientificArticlesCategories) -> Unit
    ) {
        val context = binding.tvRvScientificArticles.context

        binding.ivRvScientificArticles.setImageResource(scientificArticlesCategories.img)
        binding.tvRvScientificArticles.text =
            context.getString(scientificArticlesCategories.category)
        textAnimation(binding.tvRvScientificArticles)

        binding.itemScientificArticles.setOnClickListener {
            zoomOutAnimation(it, newLambda = { itemOnSelected(scientificArticlesCategories) })
        }
    }

    private fun textAnimation(view: View) {
        val textAnimation = AnimatorInflater.loadAnimator(
            view.context,
            R.animator.appear_dissapear
        ) as AnimatorSet

        textAnimation.apply {
            setTarget(view)
            doOnEnd { textAnimation(view) }
            start()
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