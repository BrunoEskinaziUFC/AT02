package com.example.constraintlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta: EditText
    private var ttsSucess: Boolean = false;

    private lateinit var textPeople: EditText
    private var qntPeople: Int = 1
    private var price: Double = 0.0

    private lateinit var textValue: TextView

    private lateinit var btnShare: Button
    private lateinit var btnSpeech: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize TTS engine
        tts = TextToSpeech(this, this)
        textValue = findViewById(R.id.textValue)


        textPeople = findViewById<EditText>(R.id.textPeople)
        textPeople.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("PDM24","Antes de mudar qntPeople")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("PDM24","qntPeople: $qntPeople")
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()){
                    qntPeople = s.toString().toInt()
                    if (qntPeople < 1) {
                        qntPeople = 1
                    }
                    Log.d("PDM24", "Mudando qntPeople para: $qntPeople")

                    //textValue.text = "AOBA"
                    textValue.text = divide(price,qntPeople).toString()

                    }
                }

        })

        edtConta = findViewById<EditText>(R.id.edtConta)
        edtConta.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("PDM24","Antes de mudar qntPeople")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("PDM24","qntPeople: $price")
            }
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()){
                    price = s.toString().toDouble()
                    if (price < 0.0) {
                        price = 0.0
                    }
                    Log.d("PDM24", "Mudando price para: $price")

                    //textValue.text = "AOBA"
                    textValue.text = divide(price,qntPeople).toString()

                }
            }
        })


        btnShare = findViewById(R.id.buttonShare)
        btnShare.setOnClickListener{
            //Log.d("PDM24", "AOBA")
            val texto = textValue.text
            Log.d("PDM24", "olha a mensagem $texto")
            val cota = divide(price, qntPeople)
            val sendIntent: Intent = Intent().apply{
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "A cota fica: $$cota reais")
                type = "text/plain"

            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)


        }

        btnSpeech = findViewById(R.id.btFalar)
        btnSpeech.setOnClickListener{
            clickFalar(textValue)
            //Log.d("PDM24", "AOBAAAA")
        }

    }


    fun divide(value: Double, people: Int) : Float{
        val div = value.toFloat()/people
        Log.d("PDM24", "Dividido: $div")
        textValue.text = div.toString()
        return div
    }

    fun clickFalar(v: TextView){
        if (tts.isSpeaking) {
            tts.stop()
        }
        if(ttsSucess) {
            Log.d ("PDM24", tts.language.toString())
            //tts.speak("Oi Sumido", TextToSpeech.QUEUE_FLUSH, null, null)
            val valor = v.text
            tts.speak("A cota fica: $valor reais", TextToSpeech.QUEUE_FLUSH, null, null)
        }

    }
    override fun onDestroy() {
            // Release TTS engine resources
            tts.stop()
            tts.shutdown()
            super.onDestroy()
        }

    override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                // TTS engine is initialized successfully
                tts.language = Locale.getDefault()
                ttsSucess=true
                Log.d("PDM23","Sucesso na Inicialização")
            } else {
                // TTS engine failed to initialize
                Log.e("PDM23", "Failed to initialize TTS engine.")
                ttsSucess=false
            }
        }


}

