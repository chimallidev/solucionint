package com.chimallidigital.solucionint.ui.scientific_articles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.FragmentScientificArticlesBinding


class ScientificArticlesFragment : Fragment() {
    private var _binding: FragmentScientificArticlesBinding?= null
    private val binding get()= _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentScientificArticlesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}