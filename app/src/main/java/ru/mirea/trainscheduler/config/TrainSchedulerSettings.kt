package ru.mirea.trainscheduler.config

import ru.mirea.trainscheduler.model.Currency

object TrainSchedulerSettings {
    private const val INIT_CURRENCY_CODE = "USD"
    var defaultCurrency: Currency = Currency(INIT_CURRENCY_CODE)
}