package ru.mirea.trainscheduler.config

import ru.mirea.trainscheduler.model.Currency

object TrainSchedulerSettings {
    const val INIT_CURRENCY_CODE = "RUB"
    var defaultCurrency: Currency = Currency(INIT_CURRENCY_CODE)
}