package com.chimallidigital.solucionint.ui.selector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
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
        binding= ActivityArticlesSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
        binding.tvTitularCategoriaSelector.text= getString(scientificArticlesSelectorViewModel.getCategoryName(args.type))
    }

}