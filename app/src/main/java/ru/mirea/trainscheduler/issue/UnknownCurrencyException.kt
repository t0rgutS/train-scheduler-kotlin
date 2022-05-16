package ru.mirea.trainscheduler.issue

class UnknownCurrencyException(private val unknownCode: String) :
    TrainSchedulerException() {
    override val message: String
        get() = "Неопознанная валюта: $unknownCode"

    override fun getLocalizedMessage(): String {
        return message
    }
}