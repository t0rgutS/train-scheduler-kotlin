package ru.mirea.trainscheduler.service

import kotlinx.coroutines.flow.Flow
import ru.mirea.trainscheduler.model.Currency

interface CurrencyDataService: DataService {
    fun getCurrencies(): Flow<List<Currency>>

    fun getExchangeCount(): Long

    fun clearExchanges()

    fun convert(source: String, target: String, value: Double): Flow<Double?>
}