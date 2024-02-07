package com.chimallidigital.solucionint.ui.time_meters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.chimallidigital.solucionint.databinding.FragmentTimeMetersBinding
import com.chimallidigital.solucionint.domain.model.time_meters.TimeMetersCategories.cronometro
import com.chimallidigital.solucionint.domain.model.time_meters.TimeMetersCategories.tabata_timer
import com.chimallidigital.solucionint.domain.model.time_meters.TimeMetersCategories.temporizador
import com.chimallidigital.solucionint.ui.time_meters.adapter.TimeMetersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TimeMetersFragment : Fragment() {
    private val timeMetersViewModel: TimeMetersViewModel by viewModels()
    private var _binding: FragmentTimeMetersBinding? = null
    private val binding get() = _binding!!

    private lateinit var timeMetersAdapter: TimeMetersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIState()
        initUI()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                timeMetersViewModel.categoriesTimeMeters.collect {
                    timeMetersAdapter.updateList(it)
                }
            }
        }
    }

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
        timeMetersAdapter = TimeMetersAdapter(itemOnSelected = {
            when (it) {
                cronometro -> {
                    findNavController().navigate(TimeMetersFragmentDirections.actionTimeMetersFragmentToCronometroActivity())
                }

                tabata_timer -> {}
                temporizador -> {}
            }
        })
        binding.rvTimeMeters.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = timeMetersAdapter
        }
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvTimeMeters)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimeMetersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}