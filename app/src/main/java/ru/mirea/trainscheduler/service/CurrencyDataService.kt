package ru.mirea.trainscheduler.service

import kotlinx.coroutines.flow.Flow
import ru.mirea.trainscheduler.model.Currency

interface CurrencyDataService: DataService {
    fun getCurrencies(): Flow<List<Currency>>

    suspend fun updateCurrencyList()

    fun getCurrencyCount(): Flow<Long>

    fun getExchangeCount(): Flow<Long>

    fun clearExchanges()

    fun convert(source: String, target: String, value: Double): Flow<Double?>
}