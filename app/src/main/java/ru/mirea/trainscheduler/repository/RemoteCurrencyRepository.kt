package ru.mirea.trainscheduler.repository

import kotlinx.coroutines.flow.Flow
import ru.mirea.trainscheduler.model.Currency
import ru.mirea.trainscheduler.model.CurrencyExchange

interface RemoteCurrencyRepository {
    fun getCurrencies(): Flow<List<Currency>>
    fun getExchange(source: String, target: String): Flow<CurrencyExchange?>
}