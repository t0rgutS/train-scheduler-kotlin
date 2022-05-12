package ru.mirea.trainscheduler.repository

import kotlinx.coroutines.flow.Flow

interface CurrencyConverter {
    fun convert(source: String, target: String, value: Double): Flow<Double?>
}