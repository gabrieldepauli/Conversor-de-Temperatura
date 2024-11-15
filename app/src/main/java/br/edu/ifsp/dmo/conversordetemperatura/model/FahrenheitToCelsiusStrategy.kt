package br.edu.ifsp.dmo.conversordetemperatura.model

object FahrenheitToCelsiusStrategy: TemperatureConverter {

    override fun converter(temperature: Double): Double {
        return (temperature - 32) / 1.8
    }

    override fun getScale(): String {
        return "ºC"
    }

}