package br.edu.ifsp.dmo.conversordetemperatura.model

object CelsiusToFahrenheitStrategy: TemperatureConverter {

    override fun converter(temperature: Double): Double {
        return (temperature * 1.8) + 32
    }

    override fun getScale(): String {
        return "ºF"
    }

}
