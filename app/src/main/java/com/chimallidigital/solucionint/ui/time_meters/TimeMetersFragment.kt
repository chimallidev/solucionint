package com.chimallidigital.solucionint.ui.time_meters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.FragmentTimeMetersBinding


class TimeMetersFragment : Fragment() {
   private var _binding: FragmentTimeMetersBinding?= null
    private val binding get()= _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentTimeMetersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}