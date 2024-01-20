package com.chimallidigital.solucionint.ui.selector

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.BounceInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.isInvisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navArgs
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivityArticlesSelectorBinding
import com.chimallidigital.solucionint.domain.model.ArticlesSelector.CollectionArticles
import com.chimallidigital.solucionint.domain.model.scientific_articles.ScientificArticlesCategoriesModel.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticlesSelectorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticlesSelectorBinding
    private val scientificArticlesSelectorViewModel: ArticlesSelectorViewModel by viewModels()
    private var itemCount: Int = 0

    private val args: ArticlesSelectorActivityArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticlesSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initUIState()
        initListeners()

    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scientificArticlesSelectorViewModel.articles.collect {
                    initImageActiveSelector(it)
                }
            }
        }
    }

    private fun initImageActiveSelector(articles: List<CollectionArticles>) {
        val maxItems = articles.size
        binding.tvSelectorMaximNumber.text = countFormatted(maxItems)
        when (args.type) {
            Soluciones_Inteligentes -> {
                binding.ivArticlesSelector.setImageResource(articles.get(itemCount).img)
                binding.tvArticlesSelectorArticlesTitle.text =
                    getString(articles.get(itemCount).title)
            }

            Ponte_en_Forma -> {}
            Recetas_de_Cocina -> {}
        }
    }

    @SuppressLint("ClickableViewAccesability")
    private fun initListeners() {

        binding.tvTitularCategoriaSelector.text =
            getString(scientificArticlesSelectorViewModel.getCategoryName(args.type))
        binding.btnBackFromArticlesSelectorToArticlesFragment.setOnClickListener { backAnimation() }
        binding.tvArticlesSelectorContador.text = countFormatted(itemCount + 1)
        binding.btnLeftArticlesSelector.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(
                            binding.btnLeftArticlesSelector,
                            binding.shadowBtnLeftArticlesSelector
                        )
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("x", "X= $x")
                        Log.i("y", "Y= $y")
                        if (x > -2f && x < 124f && y > 1f && y < 118f) {
                        } else {
                            btnAnimation(
                                binding.btnLeftArticlesSelector,
                                binding.shadowBtnLeftArticlesSelector
                            )
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -2f && x < 124f && y > 1f && y < 118f) {
                            animationsBtnLeft()
                            btnAnimation(
                                binding.btnLeftArticlesSelector,
                                binding.shadowBtnLeftArticlesSelector
                            )
                        } else {
                        }

                    }
                }
                return true
            }
        })
        binding.btnRightArticlesSelector.setOnClickListener { increaseCount(itemCount) }
    }

    private fun increaseCount(increaseCount: Int) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scientificArticlesSelectorViewModel.articles.collect {
                    val maxItemCount = it.size
                    var newItemCount: Int
                    newItemCount = increaseCount
                    newItemCount = if (increaseCount < maxItemCount) {
                        increaseCount + 1
                    } else {
                        1
                    }
                    runOnUiThread {
                        itemCount = newItemCount
                        binding.ivArticlesSelector.setImageResource(it.get(itemCount - 1).img)
                        binding.tvArticlesSelectorArticlesTitle.text =
                            getString(it.get(itemCount - 1).title)
                        binding.tvArticlesSelectorContador.text = countFormatted(itemCount)
                        binding.tvSelectorMaximNumber.text = countFormatted(maxItemCount)
                    }

                }
            }
        }
    }

    private fun btnPressed(view: View, view2: View) {
        view.animate().apply {
            duration = 200
            translationY(10f)
            scaleX(0.9f)
            scaleY(0.9f)
            interpolator = BounceInterpolator()
            withStartAction {
                view2.isInvisible = true
            }
            start()
        }
    }

    private fun btnAnimation(view: View, view2: View) {
        view.animate().apply {
            duration = 200
            translationY(0f)
            scaleX(1.0f)
            scaleY(1.0f)
            interpolator = BounceInterpolator()
            withStartAction {
                view2.isInvisible = false
            }
            start()
        }
    }

    private fun textTitleAnimation() {
        val textTitle = binding.tvArticlesSelectorArticlesTitle

        val dissappearText = AnimatorInflater.loadAnimator(
            textTitle.context,
            R.animator.disappear_text
        ) as AnimatorSet

        val appearText = AnimatorInflater.loadAnimator(
            textTitle.context,
            R.animator.appear_text
        ) as AnimatorSet

        dissappearText.setTarget(textTitle)
        appearText.setTarget(textTitle)
        AnimatorSet().apply {
            playSequentially(dissappearText, appearText)
            start()
        }
    }

    private fun animationsBtnLeft() {
        val imageSelector = binding.frameIvArticlesSelector

        val rotationLeftAnimation = AnimatorInflater.loadAnimator(
            imageSelector.context,
            R.animator.left_rotation
        ) as AnimatorSet
        val appearLeftAnimation = AnimatorInflater.loadAnimator(
            imageSelector.context,
            R.animator.appear_image_animation
        ) as AnimatorSet

        rotationLeftAnimation.setTarget(imageSelector)
        appearLeftAnimation.setTarget(imageSelector)

        rotationLeftAnimation.apply {
            doOnStart { textTitleAnimation() }
            doOnEnd { decreaseCount(itemCount) }
        }

        AnimatorSet().apply {
            playSequentially(rotationLeftAnimation, appearLeftAnimation)
            start()
        }
    }

    private fun countFormatted(count: Int): CharSequence? {
        val itemCountFormatted = String.format("%03d", count)
        return itemCountFormatted
    }

    private fun decreaseCount(contadorDecrease: Int) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scientificArticlesSelectorViewModel.articles.collect {
                    val maxItemCount = it.size
                    var newItemCount: Int
                    newItemCount = contadorDecrease
                    newItemCount = if (contadorDecrease > 1) {
                        contadorDecrease - 1
                    } else {
                        maxItemCount
                    }
                    runOnUiThread {
                        itemCount = newItemCount

                        binding.ivArticlesSelector.setImageResource(it.get(itemCount - 1).img)
                        binding.tvArticlesSelectorArticlesTitle.text =
                            getString(it.get(itemCount - 1).title)
                        binding.tvArticlesSelectorContador.text = countFormatted(itemCount)
                        binding.tvSelectorMaximNumber.text = countFormatted(maxItemCount)
                    }

                }
            }
        }

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