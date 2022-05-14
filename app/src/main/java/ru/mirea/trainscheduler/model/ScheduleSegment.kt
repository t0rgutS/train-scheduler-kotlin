package ru.mirea.trainscheduler.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class ScheduleSegment {
    companion object {
        const val TARGET_DATE_FORMAT = "dd.MM.yyyy HH:mm:ss"
    }

    var from: Station? = null
    var to: Station? = null
    var threadUID: String? = null
    private var departure: String? = null
    private var arrival: String? = null
    var tickets: MutableList<Ticket> = mutableListOf()

    fun setDeparture(departure: String?) {
        if (!departure.isNullOrEmpty())
            this.departure = LocalDateTime.parse(departure, DateTimeFormatter.ISO_DATE_TIME)
                .format(DateTimeFormatter.ofPattern(TARGET_DATE_FORMAT))
    }

    fun setArrival(arrival: String?) {
        if (!arrival.isNullOrEmpty())
            this.arrival = LocalDateTime.parse(arrival, DateTimeFormatter.ISO_DATE_TIME)
                .format(DateTimeFormatter.ofPattern(TARGET_DATE_FORMAT))
    }

    fun getDeparture(): String? {
        return departure
    }

    fun getArrival(): String? {
        return arrival
    }

    fun getTravelTimeInSeconds(): Long {
        val departureDateTime =
            LocalDateTime.parse(departure, DateTimeFormatter.ofPattern(TARGET_DATE_FORMAT))
        val arrivalDateTime =
            LocalDateTime.parse(arrival, DateTimeFormatter.ofPattern(TARGET_DATE_FORMAT))
        return ChronoUnit.SECONDS.between(departureDateTime, arrivalDateTime)
    }

    fun canBuyElectronic(): Boolean {
        return tickets.stream().anyMatch { ticket -> ticket.canBeElectronic == true }
    }
}