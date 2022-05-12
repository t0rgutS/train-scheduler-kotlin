package ru.mirea.trainscheduler.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Station {
    var id: Long? = null
    var codes: Map<String, String>? = HashMap()
    var transportType: String? = null
    var stationType: String? = null
    var title: String? = null
    var country: String? = null
    var settlement: String? = null
    private var departure: String? = null
    private var arrival: String? = null

    fun setDeparture(departure: String?) {
        if(!departure.isNullOrEmpty())
        this.departure = LocalDateTime.parse(departure, DateTimeFormatter.ISO_DATE_TIME)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }

    fun setArrival(arrival: String?) {
        if(!arrival.isNullOrEmpty())
        this.arrival = LocalDateTime.parse(arrival, DateTimeFormatter.ISO_DATE_TIME)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }

    fun getDeparture(): String? {
        return departure
    }

    fun getArrival(): String? {
        return arrival
    }

    fun getDefaultCode(): String? {
        var code = codes?.values?.first()
        if (code != null && !code.startsWith("s"))
            code = "s$code"
        return code
    }

    fun getLocation(): String {
        if (!country.isNullOrEmpty() && !settlement.isNullOrEmpty())
            return "$settlement, $country"
        return "${if (!country.isNullOrEmpty()) country else settlement}"
    }

    override fun toString(): String {
        return title + " (${getLocation()})"
    }
}