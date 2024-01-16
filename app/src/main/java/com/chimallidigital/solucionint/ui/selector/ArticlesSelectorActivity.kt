package com.chimallidigital.solucionint.ui.selector

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.animation.AlphaAnimation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.navigation.navArgs
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivityArticlesSelectorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticlesSelectorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticlesSelectorBinding
    private val scientificArticlesSelectorViewModel: ArticlesSelectorViewModel by viewModels()

    private val args: ArticlesSelectorActivityArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticlesSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
        binding.tvTitularCategoriaSelector.text =
            getString(scientificArticlesSelectorViewModel.getCategoryName(args.type))
        binding.btnBackFromArticlesSelectorToArticlesFragment.setOnClickListener { backAnimation() }
    }

    private fun backAnimation() {
        val view = binding.activityArticlesSelector
        val appearAnimation = AlphaAnimation(0.0f, 1.0f)
        appearAnimation.duration = 1000
        val viewBackAnimation = AnimatorInflater.loadAnimator(
            view.context,
            R.animator.back_zoom_in
        ) as AnimatorSet

        viewBackAnimation.apply {
            setTarget(view)
            doOnEnd { onBackPressedDispatcher.onBackPressed() }
            start()
        }
    }
}