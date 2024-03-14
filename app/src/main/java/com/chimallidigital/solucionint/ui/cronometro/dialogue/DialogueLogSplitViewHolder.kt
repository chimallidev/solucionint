package com.chimallidigital.solucionint.ui.cronometro.dialogue

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chimallidigital.solucionint.databinding.ItemDialogueLogSplitBinding
import com.chimallidigital.solucionint.domain.model.Cronometro.Splits

class DialogueLogSplitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemDialogueLogSplitBinding.bind(view)
    val itemDialogueLogSplit = binding.itemDialogueLogSplit

    fun render(splits: Splits) {
        binding.tvCountSplit.text = splits.countSplit.toString()
        binding.tvElapsedTime.text = splits.elapsedTime
        binding.tvTotalTime.text = splits.totalTime
    }
}