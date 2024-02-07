package com.chimallidigital.solucionint.ui.cronometro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivityCronometroBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CronometroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCronometroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCronometroBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}