package com.chimallidigital.solucionint.ui.solucionint_web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivitySolucionintWebBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SolucionintWebActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySolucionintWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySolucionintWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}