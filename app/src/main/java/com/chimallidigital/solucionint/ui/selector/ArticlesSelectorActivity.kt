package com.chimallidigital.solucionint.ui.selector

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.BounceInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivityArticlesSelectorBinding
import com.chimallidigital.solucionint.domain.model.ArticlesSelector.CollectionArticles
import com.chimallidigital.solucionint.domain.model.scientific_articles.ScientificArticlesCategoriesModel.*
import com.chimallidigital.solucionint.ui.selector.dialogue.DialogueArticlesSelectorAdapter
import com.chimallidigital.solucionint.ui.solucionint_web.SolucionintWebActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticlesSelectorActivity : AppCompatActivity() {
    companion object {
        val URL = "URL"
    }

    private lateinit var binding: ActivityArticlesSelectorBinding
    private val scientificArticlesSelectorViewModel: ArticlesSelectorViewModel by viewModels()
    private var itemCount: Int = 0
    private var stateSelectorAnimation: Boolean = false
    private var stateDialogAceptarSelector: Boolean = false
    private lateinit var dialogueArticlesSelectorAdapter: DialogueArticlesSelectorAdapter

    private lateinit var navController: NavController
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
        initImageActiveSelector()
    }

    private fun initImageActiveSelector() {
        var maxItems: Int
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                when (args.type) {
                    Soluciones_Inteligentes -> {
                        scientificArticlesSelectorViewModel.articles.collect {
                            maxItems = it.size
                            runOnUiThread { printInitImageActiveSelector(it, maxItems) }
                        }
                    }

                    Ponte_en_Forma -> {
                        scientificArticlesSelectorViewModel.articlesPonteEnForma.collect {
                            maxItems = it.size
                            runOnUiThread { printInitImageActiveSelector(it, maxItems) }
                        }
                    }

                    Recetas_de_Cocina -> {
                        scientificArticlesSelectorViewModel.articlesRecetasDeCocina.collect {
                            maxItems = it.size
                            runOnUiThread { printInitImageActiveSelector(it, maxItems) }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccesability")
    private fun initListeners() {
        dialogueArticlesSelectorAdapter =
            DialogueArticlesSelectorAdapter(itemOnSelected = { articulo ->
                var maxItems: Int
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        when (args.type) {
                            Soluciones_Inteligentes -> {
                                scientificArticlesSelectorViewModel.articles.collect {
                                    maxItems = it.size
                                    itemCount = it.indexOf(articulo) + 1
                                    runOnUiThread {
                                        binding.tvSelectorMaximNumber.text =
                                            countFormatted(maxItems)
                                        binding.tvArticlesSelectorContador.text =
                                            countFormatted(itemCount)
                                    }
                                }
                            }

                            Ponte_en_Forma -> {
                                scientificArticlesSelectorViewModel.articlesPonteEnForma.collect {
                                    maxItems = it.size
                                    itemCount = it.indexOf(articulo) + 1
                                    runOnUiThread {
                                        binding.tvSelectorMaximNumber.text =
                                            countFormatted(maxItems)
                                        binding.tvArticlesSelectorContador.text =
                                            countFormatted(itemCount)
                                    }
                                }
                            }

                            Recetas_de_Cocina -> {
                                scientificArticlesSelectorViewModel.articlesRecetasDeCocina.collect {
                                    maxItems = it.size
                                    itemCount = it.indexOf(articulo) + 1
                                    runOnUiThread {
                                        binding.tvSelectorMaximNumber.text =
                                            countFormatted(maxItems)
                                        binding.tvArticlesSelectorContador.text =
                                            countFormatted(itemCount)
                                    }
                                }
                            }
                        }
                    }
                }
                binding.ivArticlesSelector.setImageResource(articulo.img)
                binding.tvArticlesSelectorArticlesTitle.text =
                    getString(articulo.title)
            }, newLambda = { val lambda: () -> Unit })
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
                        if (x > -11f && x < 130f && y > -9 && y < 139f) {
                        } else {
                            btnAnimation(
                                binding.btnLeftArticlesSelector,
                                binding.shadowBtnLeftArticlesSelector
                            )
                            stateSelectorAnimation = true
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -11 && x < 130f && y > -9f && y < 139f && !stateSelectorAnimation) {
                            animationsBtnLeft()
                            btnAnimation(
                                binding.btnLeftArticlesSelector,
                                binding.shadowBtnLeftArticlesSelector
                            )
                        } else {
                            btnAnimation(
                                binding.btnLeftArticlesSelector,
                                binding.shadowBtnLeftArticlesSelector
                            )
                            stateSelectorAnimation = false
                        }

                    }
                }
                return true
            }
        })
        binding.btnRightArticlesSelector.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(
                            binding.btnRightArticlesSelector,
                            binding.shadowBtnRightArticlesSelector
                        )
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("x", "X= $x")
                        Log.i("y", "Y= $y")
                        if (x > -11f && x < 130f && y > -9 && y < 139f) {
                        } else {
                            btnAnimation(
                                binding.btnRightArticlesSelector,
                                binding.shadowBtnRightArticlesSelector
                            )
                            stateSelectorAnimation = true
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -11 && x < 130f && y > -9f && y < 139f && !stateSelectorAnimation) {
                            animationsBtnRight()
                            btnAnimation(
                                binding.btnRightArticlesSelector,
                                binding.shadowBtnRightArticlesSelector
                            )
                        } else {
                            btnAnimation(
                                binding.btnRightArticlesSelector,
                                binding.shadowBtnRightArticlesSelector
                            )
                            stateSelectorAnimation = false
                        }

                    }
                }
                return true
            }
        })
        binding.constraintVidrioContador.setOnClickListener { showDialogueContador() }
        binding.btnSearchArticlesSelector.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(
                            binding.btnSearchArticlesSelector,
                            binding.shadowBtnSearchArticlesSelector
                        )
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -1f && x < 364f && y > -9 && y < 139f) {
                        } else {
                            btnAnimation(
                                binding.btnSearchArticlesSelector,
                                binding.shadowBtnSearchArticlesSelector
                            )
                            stateSelectorAnimation = true
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -1f && x < 364f && y > -9f && y < 139f && !stateSelectorAnimation) {
                            showDialogueSearch()
                            btnAnimation(
                                binding.btnSearchArticlesSelector,
                                binding.shadowBtnSearchArticlesSelector
                            )
                        } else {
                            btnAnimation(
                                binding.btnSearchArticlesSelector,
                                binding.shadowBtnSearchArticlesSelector
                            )
                            stateSelectorAnimation = false
                        }

                    }
                }
                return true
            }
        })
        binding.btnEntrarArticlesSelector.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(
                            binding.btnEntrarArticlesSelector,
                            binding.shadowBtnEntrarArticlesSelector
                        )
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("nonna", "X= $x")
                        Log.i("nonna", "Y= $y")
                        if (x > -5f && x < 917f && y > 3f && y < 138f) {
                        } else {
                            btnAnimation(
                                binding.btnEntrarArticlesSelector,
                                binding.shadowBtnEntrarArticlesSelector
                            )
                            stateSelectorAnimation = true
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -5f && x < 917f && y > 3f && y < 138f && !stateSelectorAnimation) {
                            btnNavigate()
                            btnAnimation(
                                binding.btnEntrarArticlesSelector,
                                binding.shadowBtnEntrarArticlesSelector
                            )
                        } else {
                            btnAnimation(
                                binding.btnEntrarArticlesSelector,
                                binding.shadowBtnEntrarArticlesSelector
                            )
                            stateSelectorAnimation = false
                        }
                    }
                }
                return true
            }
        })
    }

    private fun btnNavigate() {
        var url: String
        val intent = Intent(this, SolucionintWebActivity::class.java)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                when (args.type) {
                    Soluciones_Inteligentes -> {
                        scientificArticlesSelectorViewModel.articles.collect {
                            url = getString(it.get(itemCount).url)
                            runOnUiThread {
                                intent.putExtra(URL, url)
                            }
                        }
                    }

                    Ponte_en_Forma -> {
                        scientificArticlesSelectorViewModel.articlesPonteEnForma.collect {
                            url = getString(it.get(itemCount).url)
                            runOnUiThread {
                                intent.putExtra(URL, url)
                            }
                        }
                    }

                    Recetas_de_Cocina -> {
                        scientificArticlesSelectorViewModel.articlesRecetasDeCocina.collect {
                            url = getString(it.get(itemCount).url)
                            runOnUiThread {
                                intent.putExtra(URL, url)
                            }
                        }
                    }
                }
            }
        }
        startActivity(intent)
    }

    private fun showDialogueSearch() {
        val dialogo = Dialog(this)
        dialogo.setContentView(R.layout.dialogue_search_articles_selector)


        val ivClosed: ImageView = dialogo.findViewById(R.id.ivClosed)
        val svArticles: androidx.appcompat.widget.SearchView = dialogo.findViewById(R.id.svArticles)
        val editText: EditText = svArticles.findViewById(androidx.appcompat.R.id.search_src_text)
        editText.setTextColor(ContextCompat.getColor(editText.context, R.color.secundario))
        editText.setHintTextColor(ContextCompat.getColor(editText.context, R.color.secundario))
        editText.setBackgroundColor(ContextCompat.getColor(editText.context, R.color.accent))
        editText.setLinkTextColor(ContextCompat.getColor(editText.context, R.color.gray))
        val searchIcon: ImageView = svArticles.findViewById(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setColorFilter(ContextCompat.getColor(editText.context, R.color.accent))
        val searchClose: ImageView =
            svArticles.findViewById(androidx.appcompat.R.id.search_close_btn)
        searchClose.setColorFilter(ContextCompat.getColor(editText.context, R.color.accent))
        val pbArticlesDialogue: ProgressBar = dialogo.findViewById(R.id.pbArticlesDialogue)
        val rvArticlesDialogue: RecyclerView = dialogo.findViewById(R.id.rvArticlesDialogue)
        val noEncontradoDialogue: CardView = dialogo.findViewById(R.id.cvNoEncontradoDialogue)
        val lambdaHide: () -> Unit = {
            dialogueArticlesSelectorAdapter.updateSelectorList(emptyList())
            dialogo.hide()
        }

        rvArticlesDialogue.apply {
            layoutManager = LinearLayoutManager(rvArticlesDialogue.context)
            adapter = dialogueArticlesSelectorAdapter
        }

        svArticles.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchForTitle(
                    query.orEmpty(),
                    noEncontradoDialogue,
                    rvArticlesDialogue,
                    pbArticlesDialogue,
                    lambdaHide
                )
                return false
            }

            override fun onQueryTextChange(newText: String?) = false

        })

        ivClosed.setOnClickListener {
            dialogueArticlesSelectorAdapter.updateSelectorList(emptyList())
            dialogo.hide()
        }
        dialogo.show()
    }

    private fun searchForTitle(
        query: String?,
        view: View,
        view2: View,
        view3: View,
        lambdaHide: () -> Unit
    ) {
        view3.isVisible = true
        var newList: List<CollectionArticles>
        CoroutineScope(Dispatchers.IO).launch {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    when (args.type) {
                        Soluciones_Inteligentes -> {
                            scientificArticlesSelectorViewModel.articles.collect {
                                newList = it.filter {
                                    getString(it.title).lowercase()
                                        .contains(query.toString().lowercase())
                                }
                                printrvDialogueArticlesSelector(
                                    newList,
                                    view,
                                    view2,
                                    view3,
                                    lambdaHide
                                )

                            }
                        }

                        Ponte_en_Forma -> {
                            scientificArticlesSelectorViewModel.articlesPonteEnForma.collect {
                                newList = it.filter {
                                    getString(it.title).lowercase()
                                        .contains(query.toString().lowercase())
                                }
                                printrvDialogueArticlesSelector(
                                    newList,
                                    view,
                                    view2,
                                    view3,
                                    lambdaHide
                                )
                            }
                        }

                        Recetas_de_Cocina -> {
                            scientificArticlesSelectorViewModel.articlesRecetasDeCocina.collect {
                                newList = it.filter {
                                    getString(it.title).lowercase()
                                        .contains(query.toString().lowercase())
                                }
                                printrvDialogueArticlesSelector(
                                    newList,
                                    view,
                                    view2,
                                    view3,
                                    lambdaHide
                                )
                            }
                        }
                    }
                }
            }
        }

    }

    private fun printrvDialogueArticlesSelector(
        newList: List<CollectionArticles>,
        view: View,
        view2: View,
        view3: View,
        lambdaHide: () -> Unit
    ) {
        if (newList.isNotEmpty()) {
            view2.isVisible = true
            view.isVisible = false
            view3.isVisible = false
            dialogueArticlesSelectorAdapter.updateSelectorList(newList)
            dialogueArticlesSelectorAdapter.initLambda(lambdaHide)
        } else {
            view2.isVisible = false
            view.isVisible = true
            view3.isVisible = false
        }
    }

    @SuppressLint("ClickableViewAccesability")
    private fun showDialogueContador() {
        val dialogo = Dialog(this)
        dialogo.setContentView(R.layout.dialogue_count_number_articles_selector)


        val etDialogueCountSelector: EditText = dialogo.findViewById(R.id.etDialogueCountSelector)
        val btnDialogueArticlesSelector: CardView =
            dialogo.findViewById(R.id.btnDialogueArticlesSelector)
        val shadowBtnDialogueArticlesSelector: CardView =
            dialogo.findViewById(R.id.shadowBtnDialogueArticlesSelector)
        val btnCloseArticlesSelector: ImageView =
            dialogo.findViewById(R.id.btnCloseArticlesSelector)

        btnCloseArticlesSelector.setOnClickListener { dialogo.hide() }
        btnDialogueArticlesSelector.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(
                            btnDialogueArticlesSelector,
                            shadowBtnDialogueArticlesSelector
                        )
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("Katyusha", "X= $x")
                        Log.i("Katyusha", "Y= $y")
                        if (x > -10f && x < 394f && y > -5f && y < 90f) {
                        } else {
                            btnAnimation(
                                btnDialogueArticlesSelector,
                                shadowBtnDialogueArticlesSelector
                            )
                            stateDialogAceptarSelector = true
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -10f && x < 394f && y > -5f && y < 90f && !stateDialogAceptarSelector) {

                            val actualCountString = etDialogueCountSelector.text.toString()

                            if (actualCountString.isNotEmpty()) {
                                val actualCount = actualCountString.toInt()

                                itemCount = actualCount
                                var maxItems: Int
                                lifecycleScope.launch {
                                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                                        when (args.type) {
                                            Soluciones_Inteligentes -> {
                                                scientificArticlesSelectorViewModel.articles.collect {
                                                    maxItems = it.size
                                                    if (actualCount in 1..maxItems) {
                                                        runOnUiThread {
                                                            printInitImageActiveSelector(
                                                                it,
                                                                maxItems
                                                            )
                                                            binding.tvArticlesSelectorContador.text =
                                                                countFormatted(itemCount)
                                                        }
                                                    } else {
                                                        runOnUiThread {
                                                            Toast.makeText(
                                                                v?.context,
                                                                "Escribe un numero entre 1 y $maxItems",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
                                                    }

                                                }
                                            }

                                            Ponte_en_Forma -> {
                                                scientificArticlesSelectorViewModel.articlesPonteEnForma.collect {
                                                    maxItems = it.size
                                                    if (actualCount in 1..maxItems) {
                                                        runOnUiThread {
                                                            printInitImageActiveSelector(
                                                                it,
                                                                maxItems
                                                            )
                                                            binding.tvArticlesSelectorContador.text =
                                                                countFormatted(itemCount)
                                                        }
                                                    } else {
                                                        runOnUiThread {
                                                            Toast.makeText(
                                                                v?.context,
                                                                "Escribe un numero entre 1 y $maxItems",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
                                                    }
                                                }
                                            }

                                            Recetas_de_Cocina -> {
                                                scientificArticlesSelectorViewModel.articlesRecetasDeCocina.collect {
                                                    maxItems = it.size
                                                    if (actualCount in 1..maxItems) {
                                                        runOnUiThread {
                                                            printInitImageActiveSelector(
                                                                it,
                                                                maxItems
                                                            )
                                                            binding.tvArticlesSelectorContador.text =
                                                                countFormatted(itemCount)
                                                        }
                                                    } else {
                                                        runOnUiThread {
                                                            Toast.makeText(
                                                                v?.context,
                                                                "Escribe un numero entre 1 y $maxItems",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    v?.context,
                                    "Escribe un numero valido",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            dialogo.hide()

                            btnAnimation(
                                btnDialogueArticlesSelector,
                                shadowBtnDialogueArticlesSelector
                            )
                        } else {
                            btnAnimation(
                                btnDialogueArticlesSelector,
                                shadowBtnDialogueArticlesSelector
                            )
                            stateDialogAceptarSelector = false
                        }
                    }
                }
                return true
            }
        })
        dialogo.show()
    }

    private fun increaseCount(increaseCount: Int) {
        var newItemRightCount: Int
        var maxItemCount: Int
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                when (args.type) {
                    Soluciones_Inteligentes -> {
                        scientificArticlesSelectorViewModel.articles.collect {
                            maxItemCount = it.size
                            newItemRightCount = increaseCountMechanism(it, increaseCount)
                            runOnUiThread {
                                printIncreaseDecrease(
                                    it,
                                    newItemRightCount,
                                    maxItemCount
                                )
                            }
                        }
                    }

                    Ponte_en_Forma -> {
                        scientificArticlesSelectorViewModel.articlesPonteEnForma.collect {
                            maxItemCount = it.size
                            newItemRightCount = increaseCountMechanism(it, increaseCount)
                            runOnUiThread {
                                printIncreaseDecrease(
                                    it,
                                    newItemRightCount,
                                    maxItemCount
                                )
                            }
                        }
                    }

                    Recetas_de_Cocina -> {
                        scientificArticlesSelectorViewModel.articlesRecetasDeCocina.collect {
                            maxItemCount = it.size
                            newItemRightCount = increaseCountMechanism(it, increaseCount)
                            runOnUiThread {
                                printIncreaseDecrease(
                                    it,
                                    newItemRightCount,
                                    maxItemCount
                                )
                            }
                        }
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

    private fun animationsBtnRight() {
        val imageSelector = binding.frameIvArticlesSelector

        val rotationRightAnimation = AnimatorInflater.loadAnimator(
            imageSelector.context,
            R.animator.right_rotation
        ) as AnimatorSet
        val appearRightAnimation = AnimatorInflater.loadAnimator(
            imageSelector.context,
            R.animator.appear_image_animation
        ) as AnimatorSet

        rotationRightAnimation.setTarget(imageSelector)
        appearRightAnimation.setTarget(imageSelector)

        rotationRightAnimation.apply {
            doOnStart { textTitleAnimation() }
            doOnEnd { increaseCount(itemCount) }
        }

        AnimatorSet().apply {
            playSequentially(rotationRightAnimation, appearRightAnimation)
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
        var maxItemCount: Int
        var newItemCount: Int
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                when (args.type) {
                    Soluciones_Inteligentes -> {
                        scientificArticlesSelectorViewModel.articles.collect {
                            maxItemCount = it.size
                            newItemCount = decreaseCountMechanism(it, contadorDecrease)
                            runOnUiThread { printIncreaseDecrease(it, newItemCount, maxItemCount) }
                        }
                    }

                    Ponte_en_Forma -> {
                        scientificArticlesSelectorViewModel.articlesPonteEnForma.collect {
                            maxItemCount = it.size
                            newItemCount = decreaseCountMechanism(it, contadorDecrease)
                            runOnUiThread { printIncreaseDecrease(it, newItemCount, maxItemCount) }
                        }
                    }

                    Recetas_de_Cocina -> {
                        scientificArticlesSelectorViewModel.articlesRecetasDeCocina.collect {
                            maxItemCount = it.size
                            newItemCount = decreaseCountMechanism(it, contadorDecrease)
                            runOnUiThread { printIncreaseDecrease(it, newItemCount, maxItemCount) }
                        }
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

    private fun decreaseCountMechanism(
        lista: List<CollectionArticles>,
        contadorDecrease: Int
    ): Int {
        val articlesList = lista
        val maxItemCount = articlesList.size
        val newItemCount: Int

        newItemCount = if (contadorDecrease > 1) {
            contadorDecrease - 1
        } else {
            maxItemCount
        }
        return newItemCount
    }

    private fun increaseCountMechanism(lista: List<CollectionArticles>, increaseCount: Int): Int {
        val articlesList = lista
        val maxItemCount = articlesList.size
        var newItemRightCount: Int
        newItemRightCount = when (increaseCount) {
            0 -> 2
            in 1..maxItemCount - 1 -> increaseCount + 1
            else -> 1
        }
        return newItemRightCount
    }

    private fun printInitImageActiveSelector(it: List<CollectionArticles>, maxItems: Int) {
        binding.ivArticlesSelector.setImageResource(it.get(itemCount).img)
        binding.tvArticlesSelectorArticlesTitle.text =
            getString(it.get(itemCount).title)
        binding.tvSelectorMaximNumber.text = countFormatted(maxItems)
    }

    private fun printIncreaseDecrease(
        lista: List<CollectionArticles>,
        newItemCount: Int,
        maxItemCount: Int
    ) {
        val articlesList = lista
        itemCount = newItemCount
        binding.ivArticlesSelector.setImageResource(articlesList.get(itemCount - 1).img)
        binding.tvArticlesSelectorArticlesTitle.text =
            getString(articlesList.get(itemCount - 1).title)
        binding.tvArticlesSelectorContador.text = countFormatted(itemCount)
        binding.tvSelectorMaximNumber.text = countFormatted(maxItemCount)
    }
}