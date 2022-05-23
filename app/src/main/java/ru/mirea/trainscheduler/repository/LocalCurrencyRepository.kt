package ru.mirea.trainscheduler.repository

import ru.mirea.trainscheduler.model.Currency
import ru.mirea.trainscheduler.model.CurrencyExchange

interface LocalCurrencyRepository : RemoteCurrencyRepository {
    fun currenciesExists(): Boolean

    fun addCurrencyList(currencyList: List<Currency>)

    fun addExchange(exchange: CurrencyExchange)

    fun getExchangeCount(): Long

    fun clearExchanges()
}