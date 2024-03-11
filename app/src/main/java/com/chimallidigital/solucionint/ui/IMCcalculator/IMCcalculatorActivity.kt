package com.chimallidigital.solucionint.ui.IMCcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivityImccalculatorBinding

class IMCcalculatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImccalculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityImccalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}