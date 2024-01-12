package com.chimallidigital.solucionint.ui.scientific_articles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.FragmentScientificArticlesBinding
import com.chimallidigital.solucionint.domain.model.scientific_articles.ScientificArticlesCategories
import com.chimallidigital.solucionint.ui.scientific_articles.adapter.ScientificArticlesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScientificArticlesFragment : Fragment() {
    private val scientificArticlesViewModel: ScientificArticlesViewModel by viewModels()
    private lateinit var scientificArticlesAdapter: ScientificArticlesAdapter

    private var _binding: FragmentScientificArticlesBinding?= null
    private val binding get()= _binding!!
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
            repeatOnLifecycle(Lifecycle.State.STARTED){
                scientificArticlesViewModel.categoriesArticles.collect{
                    scientificArticlesAdapter.updateList(it)
                }
            }
        }
    }

    private fun initListener() {
        scientificArticlesAdapter= ScientificArticlesAdapter()
        binding.rvScientificArticlesFragment.apply {
            layoutManager= LinearLayoutManager(context)
            adapter= scientificArticlesAdapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentScientificArticlesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}