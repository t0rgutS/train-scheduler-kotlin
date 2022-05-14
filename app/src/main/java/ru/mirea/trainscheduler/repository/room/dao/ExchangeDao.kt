package ru.mirea.trainscheduler.repository.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.mirea.trainscheduler.model.Currency
import ru.mirea.trainscheduler.model.CurrencyExchange

@Dao
interface ExchangeDao {
    @Query("SELECT COUNT(*) FROM currencies")
    fun countCurrencies(): Long

    @Query("SELECT COUNT(*) FROM currency_exchange")
    fun countExchanges(): Long

    @Query("SELECT * FROM currencies")
    fun getCurrencies(): Flow<List<Currency>>

    @Query("SELECT * FROM currency_exchange WHERE source=:source AND target=:target")
    fun getExchange(source: String, target: String): Flow<CurrencyExchange?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addExchange(exchange: CurrencyExchange)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCurrencyList(currencyList: List<Currency>)

    @Query("DELETE FROM currency_exchange")
    fun clearExchanges()
}