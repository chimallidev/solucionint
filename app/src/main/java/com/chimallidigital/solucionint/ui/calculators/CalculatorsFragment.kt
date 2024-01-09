package com.chimallidigital.solucionint.ui.calculators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chimallidigital.solucionint.databinding.FragmentCalculatorsBinding


class CalculatorsFragment : Fragment() {
    private var _binding: FragmentCalculatorsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculatorsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}