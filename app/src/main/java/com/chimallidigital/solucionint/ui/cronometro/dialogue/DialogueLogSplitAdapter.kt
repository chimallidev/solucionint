package com.chimallidigital.solucionint.ui.cronometro.dialogue

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.domain.model.Cronometro.Splits

class DialogueLogSplitAdapter(
    private var splitList: MutableList<Splits> = mutableListOf()
) :
    RecyclerView.Adapter<DialogueLogSplitViewHolder>() {
    fun updateList(list: MutableList<Splits>) {
        splitList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogueLogSplitViewHolder {
        return DialogueLogSplitViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_dialogue_log_split, parent, false)
        )
    }

    override fun getItemCount() = splitList.size

    override fun onBindViewHolder(holder: DialogueLogSplitViewHolder, position: Int) {
        holder.render(splitList[position])
        holder.itemDialogueLogSplit.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.rv_dialogue_log_split_anim)
    }
}