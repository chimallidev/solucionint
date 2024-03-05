package com.chimallidigital.solucionint.ui.tabata_timer_playing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivityTabataTimerPlayingBinding
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.ITEMS_LIST
import com.chimallidigital.solucionint.domain.model.TabataTimer.ItemsCollection
import java.util.ArrayList

class TabataTimerPlayingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTabataTimerPlayingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTabataTimerPlayingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        recoveryItemsList()
    }

    private fun recoveryItemsList() {
        val list= intent.extras
        val itemsList: List<ItemsCollection> = list?.get(ITEMS_LIST) as ArrayList<ItemsCollection>
        Log.i("KatyushaTabataList", "Lista recuperada: $itemsList")
    }
}