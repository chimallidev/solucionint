package com.chimallidigital.solucionint.ui.solucionint_web

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.isVisible
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivitySolucionintWebBinding
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.URL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SolucionintWebActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySolucionintWebBinding
    private var stateSolucionintAnimator= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySolucionintWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
            binding.webView.goBack()
            return true
        }
        // If it wasn't the Back key or there's no webpage history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event)
    }

    private fun initUI() {
        initListeners()
        initWebView()
    }

    private fun initWebView() {
        val myWebView: WebView = findViewById(R.id.webView)
        val url: String? = intent.getStringExtra(URL)

        myWebView.webViewClient = WebViewClient()
        myWebView.settings.javaScriptEnabled = true

        myWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, weburl: String) {
//                Toast.makeText(this@MainActivity, "Your WebView is Loaded....",
//                    Toast.LENGTH_LONG).show()
                val viewOne= binding.cardLoading
                val viewTwo= binding.webView
                changeScreenAnimation(viewOne, viewTwo)
            }
        }
        myWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
//                textView.text = "Page loading : $newProgress%"
//                if (newProgress == 100) {
//                    textView.text = "Page Loaded."
//                }
                binding.tvProgressBar.text = "Cargando $newProgress% ..."
                if (newProgress == 100) {
                    binding.tvProgressBar.text = "Carga completa"
                }
            }
        }
        myWebView.loadUrl(url!!)
    }
    @SuppressLint("ClickableViewAccesability")
    private fun initListeners() {
        binding.btnBack.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                 when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnBackPressed(binding.btnBack)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("kv", "X= $x")
                        Log.i("kv", "Y= $y")
                        if (x > -1f && x < 140f && y > -1f && y < 140f) {
                        } else {
                            if (!stateSolucionintAnimator){
                                btnBackUP(binding.btnBack)
                                stateSolucionintAnimator = true
                            }else{}
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -1f && x < 140f && y > -1f && y < 140f && !stateSolucionintAnimator) {
                            btnBackUP(binding.btnBack)
                            onBackPressedDispatcher.onBackPressed()
                        } else {
                            stateSolucionintAnimator = false
                        }
                    }
                }
                return true
            }
        })
    }
    private fun btnBackUP(view: View){
        val increaseAnimationX= ObjectAnimator.ofFloat(view, "scaleX", 0.9f, 1.0f)
        val increaseAnimationY= ObjectAnimator.ofFloat(view, "scaleY", 0.9f, 1.0f)


        val animatorSet= AnimatorSet()
        animatorSet.apply {
            duration= 300
            interpolator= BounceInterpolator()
            doOnStart { transitionColorEnd(view) }
            playTogether(increaseAnimationX, increaseAnimationY)
            start()
        }
    }
    private fun btnBackPressed(view: View){
        val decreaseAnimationX= ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.9f)

        val decreaseAnimationY= ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.9f)


        val animatorSet= AnimatorSet()
        animatorSet.apply {
            duration= 300
            interpolator= BounceInterpolator()
            doOnStart { transitionColorStart(view) }
            playTogether(decreaseAnimationX, decreaseAnimationY)
            start()
        }
    }
    private fun changeScreenAnimation(view: View, view2: View){
        val context1= view.context
        val context2= view2.context
        val transitionStart= AnimatorInflater.loadAnimator(
            context1,
            R.animator.transitions_start
        ) as AnimatorSet
        val transitionEnd= AnimatorInflater.loadAnimator(
            context2,
            R.animator.transitions_end
        ) as AnimatorSet
        transitionStart.apply {
            setTarget(view)
        }
        transitionEnd.apply {
            setTarget(view2)
        }
        val transitionsZoomOut= AnimatorInflater.loadAnimator(
            context1,
            R.animator.transitions_zoom_out
        ) as AnimatorSet
        val transitionsZoomIn= AnimatorInflater.loadAnimator(
            context2,
            R.animator.transitions_zoom_in
        ) as AnimatorSet
        transitionsZoomOut.apply {
            setTarget(view)
        }
        transitionsZoomIn.apply {
            setTarget(view2)
        }
        val animatorSet= AnimatorSet()
        animatorSet.apply {
            playTogether(transitionsZoomOut, transitionStart)
            doOnEnd { view.isVisible = false }
            interpolator= AnticipateOvershootInterpolator()
        }
        val animatorSet2= AnimatorSet()
        animatorSet2.apply {
            playTogether(transitionsZoomIn, transitionEnd)
            doOnStart { view2.isVisible= true }
            interpolator= AnticipateOvershootInterpolator()
        }
        val animatorSet3= AnimatorSet()
        animatorSet3.apply {
            playSequentially(animatorSet, animatorSet2)
            start()
        }
    }
    private fun transitionColorStart(view: View){
        val colors= arrayOf(ColorDrawable(getColor(R.color.gray_alpha)), ColorDrawable(getColor(R.color.gray)))
        val transition= TransitionDrawable(colors)
        view.background= transition
        transition.startTransition(300)
        view.setBackgroundColor(getColor(R.color.gray))
    }
    private fun transitionColorEnd(view: View){
        val colors= arrayOf(ColorDrawable(getColor(R.color.gray)), ColorDrawable(getColor(R.color.gray_alpha)))
        val transition= TransitionDrawable(colors)
        view.background= transition
        transition.startTransition(300)
        view.setBackgroundColor(getColor(R.color.gray_alpha))
    }
}