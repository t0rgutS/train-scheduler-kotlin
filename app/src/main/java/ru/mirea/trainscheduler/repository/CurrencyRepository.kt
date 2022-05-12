package ru.mirea.trainscheduler.repository

import kotlinx.coroutines.flow.Flow
import ru.mirea.trainscheduler.model.Currency

interface CurrencyRepository {
    fun getCurrencies(): Flow<List<Currency>>
}