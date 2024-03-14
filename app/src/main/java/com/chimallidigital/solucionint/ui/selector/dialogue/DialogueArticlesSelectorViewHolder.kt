package com.chimallidigital.solucionint.ui.selector.dialogue

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ItemDialogueArticlesSelectorBinding
import com.chimallidigital.solucionint.domain.model.ArticlesSelector.CollectionArticles
import java.util.Random

class DialogueArticlesSelectorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemDialogueArticlesSelectorBinding.bind(view)

    fun render(
        collectionArticles: CollectionArticles,
        itemOnSelected: (CollectionArticles) -> Unit,
        newLambda: () -> Unit
    ) {
        val context = binding.tvRvSearchArticles.context
        binding.ivRvSearchArticles.setImageResource(collectionArticles.img)
        binding.tvRvSearchArticles.text = context.getString(collectionArticles.title)
        binding.itemDialogueArticlesSelector.setOnClickListener {
//            zoomOutAnimation(it, lambda2 = {itemOnSelected(collectionArticles)}, newLambda)
            rotationAnimation(it, lambda2 = { itemOnSelected(collectionArticles) }, newLambda)
        }
    }

    private fun rotationAnimation(view: View, lambda2: () -> Unit, lambda3: () -> Unit) {
        val zoomOutItemAnimation = AnimatorInflater.loadAnimator(
            view.context,
            R.animator.zoom_out_item
        ) as AnimatorSet

        zoomOutItemAnimation.apply {
            setTarget(view)
            doOnEnd {
                lambda2()
                lambda3()
            }
        }
        val random = Random()
        val degrees = random.nextInt(2520) + 360
        val degrees2 = -(degrees).toFloat()
        val direction = random.nextInt(2) + 1
        val rotation = ObjectAnimator.ofFloat(view, "rotation", 0f, degrees.toFloat()).apply {
            duration = 700
        }
        val rotation2 = ObjectAnimator.ofFloat(view, "rotation", 0f, degrees2).apply {
            duration = 700
        }
        val animatorSet = AnimatorSet()
        if (direction == 1) {
            animatorSet.play(zoomOutItemAnimation).with(rotation)
            animatorSet.start()
        } else {
            animatorSet.play(zoomOutItemAnimation).with(rotation2)
            animatorSet.start()
        }
    }
}