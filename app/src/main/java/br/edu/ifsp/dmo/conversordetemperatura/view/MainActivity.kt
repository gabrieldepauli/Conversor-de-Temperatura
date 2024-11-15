package br.edu.ifsp.dmo.conversordetemperatura.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo.conversordetemperatura.R
import br.edu.ifsp.dmo.conversordetemperatura.databinding.ActivityMainBinding
import br.edu.ifsp.dmo.conversordetemperatura.model.*
import kotlin.NumberFormatException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var converterStrategy: TemperatureConverter
    private var inputUnit: String = "Celsius"
    private var outputUnit: String = "Fahrenheit"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClickListener()
    }

    private fun setClickListener() {

        binding.btnCelsius.setOnClickListener {
            outputUnit = "Celsius"
            handleConversion()
        }

        binding.btnFahrenheit.setOnClickListener {
            outputUnit = "Fahrenheit"
            handleConversion()
        }

        binding.btnKelvin.setOnClickListener {
            outputUnit = "Kelvin"
            handleConversion()
        }

        binding.radiogroupUnits.setOnCheckedChangeListener { _, checkedId ->
            inputUnit = when (checkedId) {
                R.id.radio_celsius -> "Celsius"
                R.id.radio_fahrenheit -> "Fahrenheit"
                R.id.radio_kelvin -> "Kelvin"
                else -> "Celsius"
            }
        }

    }

    private fun getSelectedConversionStrategy(): TemperatureConverter {

        return when (inputUnit) {
            "Celsius" -> when (outputUnit) {
                "Fahrenheit" -> CelsiusToFahrenheitStrategy
                "Kelvin" -> CelsiusToKelvinStrategy
                else -> throw IllegalArgumentException("Unidade de saída inválida")
            }
            "Fahrenheit" -> when (outputUnit) {
                "Celsius" -> FahrenheitToCelsiusStrategy
                "Kelvin" -> FahrenheitToKelvinStrategy
                else -> throw IllegalArgumentException("Unidade de saída inválida")
            }
            "Kelvin" -> when (outputUnit) {
                "Celsius" -> KelvinToCelsiusStrategy
                "Fahrenheit" -> KelvinToFahrenheitStrategy
                else -> throw IllegalArgumentException("Unidade de saída inválida")
            }
            else -> throw IllegalArgumentException("Unidade de entrada inválida")
        }

    }

    private fun readTemperature(): Double {

        return try {
            binding.edittextTemperature.text.toString().toDouble()
        } catch (e: NumberFormatException) {
            throw NumberFormatException("Erro de entrada")
        }

    }

    private fun handleConversion() {

        try {
            val inputValue = readTemperature()
            val strategy = getSelectedConversionStrategy()

            binding.textviewResultNumber.text = String.format(
                "%.2f %s",
                strategy.converter(inputValue),
                strategy.getScale()
            )

            binding.textviewResultMessage.text = when (strategy) {
                is FahrenheitToCelsiusStrategy -> getString(R.string.msgFtoC)
                is CelsiusToFahrenheitStrategy -> getString(R.string.msgCtoF)
                is KelvinToCelsiusStrategy -> getString(R.string.msgKtoC)
                is FahrenheitToKelvinStrategy -> getString(R.string.msgFtoK)
                is CelsiusToKelvinStrategy -> getString(R.string.msgCtoK)
                is KelvinToFahrenheitStrategy -> getString(R.string.msgKtoF)
                else -> getString(R.string.error_popup_notify)
            }

        } catch (e: Exception) {
            Toast.makeText(
                this,
                getString(R.string.error_popup_notify),
                Toast.LENGTH_SHORT
            ).show()
            Log.e("APP_DMO", e.stackTraceToString())
        }

    }
}
