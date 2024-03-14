package com.chimallidigital.solucionint.ui.time_meters.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.domain.model.time_meters.TimeMetersCategories

class TimeMetersAdapter(
    private var timeMetersCategoriesList: List<TimeMetersCategories> = emptyList(),
    private var itemOnSelected: (TimeMetersCategories) -> Unit
) :
    RecyclerView.Adapter<TimeMetersViewHolder>() {
    fun updateList(list: List<TimeMetersCategories>) {
        timeMetersCategoriesList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeMetersViewHolder {
        return TimeMetersViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_time_meters, parent, false)
        )
    }

    override fun getItemCount() = timeMetersCategoriesList.size

    override fun onBindViewHolder(holder: TimeMetersViewHolder, position: Int) {
        holder.render(timeMetersCategoriesList[position], itemOnSelected)
        holder.item.animation= AnimationUtils.loadAnimation(holder.item.context, R.anim.item_rv_left_appear)
    }
}