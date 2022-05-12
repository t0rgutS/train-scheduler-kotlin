package ru.mirea.trainscheduler.model

class Ticket {
    var price: Double? = null
    var canBeElectronic: Boolean? = null
    var name: String? = null
    var currency: String? = null
    var displayCurrency: String? = null

    fun getPriceAsString(): String {
        if (currency != displayCurrency)
            return "$price $displayCurrency (оплата принимается в $currency)"
        return "$price $displayCurrency"
    }
}