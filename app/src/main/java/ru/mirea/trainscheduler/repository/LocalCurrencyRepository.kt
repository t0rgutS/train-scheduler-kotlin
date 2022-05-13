package ru.mirea.trainscheduler.repository

import ru.mirea.trainscheduler.model.Currency
import ru.mirea.trainscheduler.model.CurrencyExchange

interface LocalCurrencyRepository : CurrencyRepository {
    fun currenciesExists(): Boolean
    fun addCurrencyList(currencyList: List<Currency>)
    fun addExchange(exchange: CurrencyExchange)
}