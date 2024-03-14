package com.chimallidigital.solucionint.ui.IMCcalculator

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import com.chimallidigital.solucionint.R
import com.chimallidigital.solucionint.databinding.ActivityImccalculatorBinding
import com.chimallidigital.solucionint.domain.StringsCollection.Companion.RESULTADOS_IMC
import com.chimallidigital.solucionint.ui.ResultadoIMC.ResultadosIMCActivity
import com.chimallidigital.solucionint.ui.tabata_timer_playing.FinishTabataTimerActivity
import java.text.DecimalFormat

class IMCcalculatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImccalculatorBinding

    private var peso: Float=50f
    private var altura=140
    private var stateAnimator= false
    private var IMC: Float= 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityImccalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
        binding.tvAlturaCount.text= altura.toString()
        binding.tvPesoCount.text=formatDecimal(peso)
        binding.BTNLessAltura.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.backgroundBTNLessAltura, binding.shadowBTNLessAltura)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("calculator_IMC_BTN_LESS", "X= $x")
                        Log.i("calculator_IMC_BTN_LESS", "Y= $y")
                        if (x > -4f && x < 183f && y > -7f && y < 255f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(
                                    binding.backgroundBTNLessAltura, binding.shadowBTNLessAltura
                                )
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -4f && x < 183f && y > -7f && y < 255f && !stateAnimator) {
                            btnAnimation(binding.backgroundBTNLessAltura, binding.shadowBTNLessAltura)
                            if (altura!=0 && altura>0){
                                altura--
                                binding.tvAlturaCount.text= altura.toString()
                            }
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        binding.BTNPlusAltura.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.backgroundBTNPlusAltura, binding.shadowBTNPlusAltura)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("calculator_IMC_BTN_PLUS", "X= $x")
                        Log.i("calculator_IMC_BTN_PLUS", "Y= $y")
                        if (x > -5f && x < 160f && y > -7f && y < 255f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(
                                    binding.backgroundBTNPlusAltura, binding.shadowBTNPlusAltura
                                )
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -4f && x < 183f && y > -7f && y < 255f && !stateAnimator) {
                            btnAnimation(binding.backgroundBTNPlusAltura, binding.shadowBTNPlusAltura)
                            if (altura<999){
                                altura++
                                binding.tvAlturaCount.text= altura.toString()
                            }
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        binding.BTNLessPeso.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.backgroundBTNLessPeso, binding.shadowBTNLessPeso)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("calculator_IMC_BTN_LESS", "X= $x")
                        Log.i("calculator_IMC_BTN_LESS", "Y= $y")
                        if (x > -5f && x < 160f && y > -7f && y < 255f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(
                                    binding.backgroundBTNLessPeso, binding.shadowBTNLessPeso
                                )
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -4f && x < 183f && y > -7f && y < 255f && !stateAnimator) {
                            btnAnimation(binding.backgroundBTNLessPeso, binding.shadowBTNLessPeso)
                            if (peso.toInt()!=0 && peso>0){
                                peso--
                                binding.tvPesoCount.text= formatDecimal(peso)
                            }
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        binding.BTNPlusPeso.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.backgroundBTNPlusPeso, binding.shadowBTNPlusPeso)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("calculator_IMC_BTN_PLUS", "X= $x")
                        Log.i("calculator_IMC_BTN_PLUS", "Y= $y")
                        if (x > -5f && x < 160f && y > -7f && y < 255f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(
                                    binding.backgroundBTNPlusPeso, binding.shadowBTNPlusPeso
                                )
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -4f && x < 183f && y > -7f && y < 255f && !stateAnimator) {
                            btnAnimation(binding.backgroundBTNPlusPeso, binding.shadowBTNPlusPeso)
                            if (peso<999){
                                peso++
                                binding.tvPesoCount.text= formatDecimal(peso)
                            }
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        binding.BTNCalcular.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.backgroundBTNCalcular, binding.shadowBTNCalcular)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("BTN_calcular_IMC", "X= $x")
                        Log.i("BTN_calcular_IMC", "Y= $y")
                        if (x > -4f && x < 655f && y > -5f && y < 161f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(
                                    binding.backgroundBTNCalcular, binding.shadowBTNCalcular
                                )
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -4f && x < 655f && y > -5f && y < 161f && !stateAnimator) {
                            btnAnimation(binding.backgroundBTNCalcular, binding.shadowBTNCalcular)
                            IMC= calcularIMC(altura, peso)
                            navigateResultados(IMC)
                            Log.i("calc_IMC", "altura: $altura peso: $peso")
                            Log.i("calc_IMC", "IMC= $IMC")
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        binding.BTNBack.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(binding.backgroundBTNBack, binding.shadowBTNBack)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("BTN_calcular_Back", "X= $x")
                        Log.i("BTN_calcular_Back", "Y= $y")
                        if (x > -4f && x < 159f && y > -5f && y < 161f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(
                                    binding.backgroundBTNBack, binding.shadowBTNBack
                                )
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -4f && x < 159f && y > -5f && y < 161f && !stateAnimator) {
                            btnAnimation(binding.backgroundBTNBack, binding.shadowBTNBack)
                            onBackPressedDispatcher.onBackPressed()
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        binding.cvCountAltura.setOnClickListener { showDialogueAltura() }
        binding.cvCountPeso.setOnClickListener { showDialoguePeso() }
    }
    private fun navigateResultados(imc: Float){
        val intent= Intent(this, ResultadosIMCActivity::class.java)
        intent.putExtra(RESULTADOS_IMC, imc)
        startActivity(intent)
    }
    private fun showDialoguePeso(){
        val dialogo= Dialog(this)
        dialogo.setContentView(R.layout.dialogue_calculator_peso)

        val BTNX: ImageView= dialogo.findViewById(R.id.BTNX)
        val etPeso: EditText= dialogo.findViewById(R.id.etPeso)
        val BTNAceptar: ConstraintLayout= dialogo.findViewById(R.id.BTNAceptar)
        val backgroundBTNAceptar: ConstraintLayout= dialogo.findViewById(R.id.backgroundBTNAceptar)
        val shadowBTNAceptar: ConstraintLayout= dialogo.findViewById(R.id.shadowBTNAceptar)

        BTNX.setOnClickListener { dialogo.hide() }
        BTNAceptar.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(backgroundBTNAceptar, shadowBTNAceptar)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("dial_calculator_aceptar", "X= $x")
                        Log.i("dial_calculator_aceptar", "Y= $y")
                        if (x > -2f && x < 443f && y > -3f && y < 103f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(
                                    backgroundBTNAceptar, shadowBTNAceptar
                                )
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -2f && x < 443f && y > -3f && y < 103f && !stateAnimator) {
                            btnAnimation(backgroundBTNAceptar, shadowBTNAceptar)
                            val pesoString= etPeso.text.toString()

                            if (pesoString.isNotEmpty()){
                                val pesoInt= pesoString.toFloat()
                                if (pesoInt>0 && pesoInt<1000){
                                    peso= pesoInt
                                    binding.tvPesoCount.text= formatDecimal(peso)
                                    Log.i("altura_dial_calc", "altura: $altura")
                                }else{
                                    Toast.makeText(BTNAceptar.context, "Peso no valido", Toast.LENGTH_LONG).show()
                                }
                            }
                            dialogo.hide()
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })
        dialogo.show()
    }
    private fun calcularIMC(altura: Int, peso: Float): Float{
        var alturaMetros: Float= altura.toFloat()/100
        var pesoRound= formatDecimal(peso).toFloat()
        Log.i("calc_IMC", "pesoRound= $pesoRound alturaMetros= $alturaMetros")
        var resIMC= (pesoRound)/(alturaMetros*alturaMetros)
        val numDec= DecimalFormat("#0.00")
        val numero= numDec.format(resIMC).toFloat()
        return numero
    }
    private fun showDialogueAltura(){
        val dialogo= Dialog(this)
        dialogo.setContentView(R.layout.dialogue_calculator_altura)

        val BTNX: ImageView= dialogo.findViewById(R.id.BTNX)
        val etAltura: EditText= dialogo.findViewById(R.id.etAltura)
        val BTNAceptar: ConstraintLayout= dialogo.findViewById(R.id.BTNAceptar)
        val backgroundBTNAceptar: ConstraintLayout= dialogo.findViewById(R.id.backgroundBTNAceptar)
        val shadowBTNAceptar: ConstraintLayout= dialogo.findViewById(R.id.shadowBTNAceptar)

        BTNAceptar.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnPressed(backgroundBTNAceptar, shadowBTNAceptar)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val x = event.getX()
                        val y = event.getY()
                        Log.i("dial_calculator_aceptar", "X= $x")
                        Log.i("dial_calculator_aceptar", "Y= $y")
                        if (x > -2f && x < 443f && y > -3f && y < 103f) {
                        } else {
                            if (!stateAnimator) {
                                btnAnimation(
                                    backgroundBTNAceptar, shadowBTNAceptar
                                )
                                stateAnimator = true
                            } else {
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        val x = event.getX()
                        val y = event.getY()

                        if (x > -2f && x < 443f && y > -3f && y < 103f && !stateAnimator) {
                            btnAnimation(backgroundBTNAceptar, shadowBTNAceptar)
                            val alturaString= etAltura.text.toString()

                            if (alturaString.isNotEmpty()){
                                val alturaInt= alturaString.toInt()
                                if (alturaInt>0 && alturaInt<1000){
                                    altura= alturaInt
                                    binding.tvAlturaCount.text= altura.toString()
                                    Log.i("altura_dial_calc", "altura: $altura")
                                }else{
                                    Toast.makeText(BTNAceptar.context, "Altura no valida", Toast.LENGTH_LONG).show()
                                }
                            }
                            dialogo.hide()
                        } else {
                            stateAnimator = false
                        }
                    }
                }
                return true
            }
        })

        BTNX.setOnClickListener { dialogo.hide() }

        dialogo.show()
    }
    private fun formatDecimal(num: Float): String {
        val numDec= DecimalFormat("##0.0")
        val numero= numDec.format(num)
        return numero
    }
    private fun selectState(view: View, view2: View, state: Boolean){
        if (state){
            view.setBackgroundColor(getColor(R.color.accent))
            view2.setBackgroundColor(getColor(R.color.accent))
        }else{
            view.setBackgroundColor(getColor(R.color.gray))
            view2.setBackgroundColor(getColor(R.color.gray))
        }
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