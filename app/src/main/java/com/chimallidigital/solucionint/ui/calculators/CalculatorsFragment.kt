package com.chimallidigital.solucionint.ui.calculators

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.FragmentCalculatorsBinding
import com.chimallidigital.solucionint.ui.IMCcalculator.IMCcalculatorActivity
import java.text.DecimalFormat
import java.util.Random


class CalculatorsFragment : Fragment() {
    private var _binding: FragmentCalculatorsBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
        textAnimation(binding.tvIMC)
        initRotation(binding.ivIMC)
        binding.ivIMC.setOnClickListener {navigationAnimation(it)}
    }

    private fun navigateTOIMCCalculator(){
        findNavController().navigate(CalculatorsFragmentDirections.actionCalculatorsFragmentToIMCcalculatorActivity())
    }
    private fun navigationAnimation(view: View){
        val random= Random()
        val degrees= random.nextInt(99)

        when(degrees){
            0->{animationZero(view)}
            in 1.. 20->{animationLeftNavigate(view)}
            in 21.. 99->{rightRotation(view)}
        }
    }
    private fun rightRotation(view: View){
        val random= Random()
        val degrees= random.nextInt(360000000)+360
        val rotation= ObjectAnimator.ofFloat(view,"rotation",0f, degrees.toFloat()).apply {
            duration=1000
            interpolator= AccelerateInterpolator()
            start()
        }
        val rotationFinish= ObjectAnimator.ofFloat(view,"rotation",0f)

        val animatorSet= AnimatorSet()
        animatorSet.playSequentially(rotation, rotationFinish)
        animatorSet.apply {
            doOnEnd { navigateTOIMCCalculator() }
        }
        animatorSet.start()
    }
    private fun animationLeftNavigate(view: View){
        val random= Random()
        val degrees= random.nextInt(360000000)+360
        val degrees2= -(degrees).toFloat()
        val rotation= ObjectAnimator.ofFloat(view, "rotation",0f,degrees2).apply {
            duration=1000
        }
        val rightRecoveryTranslation= AnimatorInflater.loadAnimator(
            view.context,
            R.animator.translate_right_recovery
        ) as AnimatorSet
        rightRecoveryTranslation.apply {
            setTarget(view)
        }
        val leftTranslation= AnimatorInflater.loadAnimator(
            view.context,
            R.animator.calculator_left_translation_navigate
        ) as AnimatorSet
        leftTranslation.apply {
            setTarget(view)
        }
        val rotationFinish= ObjectAnimator.ofFloat(view, "rotation", 0f)
        val animatorSet2= AnimatorSet()
        animatorSet2.play(rightRecoveryTranslation).with(rotationFinish)
        val animatorSet= AnimatorSet()
        animatorSet.play(rotation).with(leftTranslation)
        animatorSet.apply {
            doOnEnd {
                animatorSet2.start()
                navigateTOIMCCalculator()
            }
        }
        animatorSet.start()
    }
    private fun animationZero(view: View){
        val random= Random()
        val degrees= random.nextInt(360000000)+3600
        val degrees2= -(degrees).toFloat()
        val rotation= ObjectAnimator.ofFloat(view, "rotation",0f,degrees2).apply {
            duration=1000
        }
        val zoomOut= AnimatorInflater.loadAnimator(
            view.context,
            R.animator.zoom_out_item
        ) as AnimatorSet
        val zoomOutRecovery= AnimatorInflater.loadAnimator(
            view.context,
            R.animator.calculator_zoom_out_recovery
        ) as AnimatorSet
        val rotationFinish= ObjectAnimator.ofFloat(view,"rotation",0f)
        zoomOutRecovery.apply {
            setTarget(view)
            doOnEnd { navigateTOIMCCalculator() }
        }
        zoomOut.apply {
            setTarget(view)
        }

        val animatorSet= AnimatorSet()
        animatorSet.play(rotation).with(zoomOut)
        animatorSet.apply {
            interpolator= AccelerateInterpolator()
        }
        val animatorSet3= AnimatorSet()
        animatorSet3.playTogether(zoomOutRecovery, rotationFinish)
        val animatorSet2= AnimatorSet()
        animatorSet2.playSequentially(animatorSet, animatorSet3)
        animatorSet2.start()
    }
    private fun initRotation(view: View){
        val random= Random()
        val degrees= random.nextInt(7560)+360
        val degrees2= -(degrees).toFloat()
        val rotation= ObjectAnimator.ofFloat(view, "rotation",0f,degrees2).apply {
            duration= 1000
        }
        val rotationFinish= ObjectAnimator.ofFloat(view, "rotation", 0f)
        val translateX= AnimatorInflater.loadAnimator(
            view.context,
            R.animator.calculator_left_translation
        ) as AnimatorSet
        translateX.apply {
            setTarget(view)
        }
        val animatorSet= AnimatorSet()
        animatorSet.play(rotation).with(translateX)

        val animatorSet2= AnimatorSet()
        animatorSet2.apply {
            interpolator= DecelerateInterpolator()
        }
        animatorSet2.playSequentially(animatorSet, rotationFinish)
        animatorSet2.start()
    }
    private fun textAnimation(view: View) {
        val textAnimation = AnimatorInflater.loadAnimator(
            view.context,
            R.animator.appear_dissapear
        ) as AnimatorSet

        textAnimation.apply {
            setTarget(view)
            doOnEnd { textAnimation(view) }
            start()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculatorsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}