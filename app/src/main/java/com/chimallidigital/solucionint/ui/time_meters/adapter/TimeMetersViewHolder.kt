package com.chimallidigital.solucionint.ui.time_meters.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.chimallidigital.solucionint.databinding.ItemTimeMetersBinding
import com.chimallidigital.solucionint.domain.model.time_meters.TimeMetersCategories
import com.chimallidigital.solucionint.domain.model.time_meters.TimeMetersCategories.*

class TimeMetersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemTimeMetersBinding.bind(view)
    val item = binding.itemTimeMeters

    @SuppressLint("ClickableViewAccesability")
    fun render(
        timeMetersCategories: TimeMetersCategories,
        itemOnSelected: (TimeMetersCategories) -> Unit
    ) {
        val context = binding.tvTituloTimeMeters.context

        binding.tvTituloTimeMeters.text = context.getString(timeMetersCategories.titulo)
        binding.ivTimeMeters.setImageResource(timeMetersCategories.img)
        binding.btnActivar.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(
                            binding.btnActivar,
                            binding.btnShadowActivar
                        )
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("nonnAdapter", "X= $x")
                        Log.i("nonnAdapter", "Y= $y")
                    }

                    MotionEvent.ACTION_CANCEL -> {
                        btnAnimation(
                            binding.btnActivar,
                            binding.btnShadowActivar
                        )
                    }

                    MotionEvent.ACTION_UP -> {
                        btnAnimation(
                            binding.btnActivar,
                            binding.btnShadowActivar
                        )
                        itemOnSelected(timeMetersCategories)
                    }
                }
                return true
            }
        })
    }

    private fun btnPressed(view: View, view2: View) {
        view.animate().apply {
            duration = 200
            translationY(10f)
            scaleX(0.9f)
            scaleY(0.9f)
            interpolator = BounceInterpolator()
            withStartAction {
                view2.isInvisible = true
            }
            start()
        }
    }

    private fun btnAnimation(view: View, view2: View) {
        view.animate().apply {
            duration = 200
            translationY(0f)
            scaleX(1.0f)
            scaleY(1.0f)
            interpolator = BounceInterpolator()
            withEndAction {
                view2.isInvisible = false
            }
            start()
        }
    }
}