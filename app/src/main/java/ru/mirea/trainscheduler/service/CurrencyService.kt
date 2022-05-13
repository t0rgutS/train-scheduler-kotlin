package ru.mirea.trainscheduler.service

import kotlinx.coroutines.flow.Flow
import ru.mirea.trainscheduler.model.Currency

interface CurrencyService {
    fun getCurrencies(): Flow<List<Currency>>

    fun convert(source: String, target: String, value: Double): Flow<Double?>
}