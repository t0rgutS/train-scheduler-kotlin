package ru.mirea.trainscheduler.repository

import kotlinx.coroutines.flow.Flow
import ru.mirea.trainscheduler.model.Currency
import ru.mirea.trainscheduler.model.CurrencyExchange

interface LocalCurrencyRepository : RemoteCurrencyRepository {
    fun currenciesExists(): Flow<Boolean>

    fun addCurrencyList(currencyList: List<Currency>)

    fun addCurrency(currency: Currency)

    fun addExchange(exchange: CurrencyExchange)

    fun getCurrencyCount(): Flow<Long>

    fun getExchangeCount(): Flow<Long>

    fun clearExchanges()
}