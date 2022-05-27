package ru.mirea.trainscheduler.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Station {
    companion object {
        const val TARGET_DATE_FORMAT = "dd.MM.yyyy HH:mm:ss"
        const val INITIAL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

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
            this.departure = LocalDateTime.parse(departure, DateTimeFormatter.ofPattern(
                INITIAL_DATE_FORMAT)).format(DateTimeFormatter.ofPattern(TARGET_DATE_FORMAT))
    }

    fun setArrival(arrival: String?) {
        if(!arrival.isNullOrEmpty())
            this.arrival = LocalDateTime.parse(arrival, DateTimeFormatter.ofPattern(
                INITIAL_DATE_FORMAT)).format(DateTimeFormatter.ofPattern(TARGET_DATE_FORMAT))
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

    private fun resolveStationType(): String {
        return when(stationType) {
            "station" -> "станция"
            "platform" -> "платформа"
            "stop" -> "остановочный пункт"
            "checkpoint" -> "блок-пост"
            "post" -> "пост"
            "crossing" -> "разъезд"
            "overtaking_point" -> "обгонный пункт"
            "train_station" -> "вокзал"
            "bus_station" -> "автовокзал"
            "bus_stop" -> "автобусная остановка"
            else -> ""
        }
    }

    fun getFullName(): String {
        val resolvedStationType = resolveStationType()
        if(resolvedStationType != "" && title?.contains(resolvedStationType) == false)
            return "$resolvedStationType $title"
        return title!!
    }
}