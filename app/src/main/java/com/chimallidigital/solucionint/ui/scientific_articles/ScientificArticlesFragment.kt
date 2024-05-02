package com.chimallidigital.solucionint.ui.scientific_articles

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chimallidigital.solucionint.databinding.FragmentScientificArticlesBinding
import com.chimallidigital.solucionint.domain.model.scientific_articles.ScientificArticlesCategories
import com.chimallidigital.solucionint.domain.model.scientific_articles.ScientificArticlesCategories.*
import com.chimallidigital.solucionint.domain.model.scientific_articles.ScientificArticlesCategoriesModel
import com.chimallidigital.solucionint.ui.scientific_articles.adapter.ScientificArticlesAdapter
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScientificArticlesFragment : Fragment() {
    private val scientificArticlesViewModel: ScientificArticlesViewModel by viewModels()
    private lateinit var scientificArticlesAdapter: ScientificArticlesAdapter

    private var _binding: FragmentScientificArticlesBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initUIState()
        initListener()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scientificArticlesViewModel.categoriesArticles.collect {
                    scientificArticlesAdapter.updateList(it)
                }
            }
        }
    }

    private fun initListener() {
        scientificArticlesAdapter = ScientificArticlesAdapter(itemOnSelected = {
           val type= when(it){
               Ponte_en_Forma -> ScientificArticlesCategoriesModel.Ponte_en_Forma
               Recetas_de_Cocina -> ScientificArticlesCategoriesModel.Recetas_de_Cocina
               SolucionesInteligentes -> ScientificArticlesCategoriesModel.Soluciones_Inteligentes
           }
            findNavController().navigate(
                ScientificArticlesFragmentDirections
                    .actionScientificArticlesFragmentToArticlesSelectorActivity(type))
        })
        binding.rvScientificArticlesFragment.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = scientificArticlesAdapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScientificArticlesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}