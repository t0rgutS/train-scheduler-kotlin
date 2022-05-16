package ru.mirea.trainscheduler.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mirea.trainscheduler.model.Currency
import ru.mirea.trainscheduler.repository.CurrencyRepository
import ru.mirea.trainscheduler.repository.LocalCurrencyRepository
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class CurrencyServiceImpl(
    private val remoteRepository: CurrencyRepository,
    private val localRepository: LocalCurrencyRepository,
) : CurrencyService {
    companion object {
        const val TAG = "Currency Operations"
    }

    override suspend fun init() {
        if (!localRepository.currenciesExists()) {
            remoteRepository.getCurrencies().collect { remoteCurrencies ->
                Log.d(TAG, "Выполняется загрузка $remoteCurrencies кодов валют в локальную " +
                        "базу данных")
                localRepository.addCurrencyList(remoteCurrencies)
            }
        }
    }

    override fun getCurrencies(): Flow<List<Currency>> {
        return localRepository.getCurrencies()
    }

    override fun getExchangeCount(): Long {
        return localRepository.getExchangeCount()
    }

    override fun clearExchanges() {
        localRepository.clearExchanges()
    }

    override fun convert(source: String, target: String, value: Double): Flow<Double?> = flow {
        Log.d(TAG, "Перевод $value $source в $target")
        localRepository.getExchange(source, target).collect { localExchange ->
            if (localExchange != null)
                Log.d(TAG, "Данные перевода из $source в $target найдены в локальной базе")
            else Log.d(TAG, "Данные перевода из $source в $target не найдены в локальной базе " +
                    "данных.")
            val nextUpdate = if (localExchange?.nextUpdateOn != null)
                Instant.ofEpochSecond(localExchange.nextUpdateOn!!)
                    .atZone(ZoneId.systemDefault()).toLocalDate() else null
            if (nextUpdate != null && nextUpdate.isAfter(LocalDate.now()) && localExchange?.rate != null) {
                val result = value * localExchange.rate!!
                Log.d(TAG, "$value $source = $result $target")
                emit(result)
            } else {
                if (localExchange == null)
                    Log.d(TAG,
                        "Данные перевода из $source в $target устарели, требуется обновление")
                remoteRepository.getExchange(source, target)
                    .collect { remoteExchange ->
                        Log.d(TAG, "Получены данные о переводе из $source в $target")
                        if (remoteExchange != null) {
                            localRepository.addExchange(remoteExchange)
                            val result = value * remoteExchange.rate!!
                            Log.d(TAG, "$value $source = $result $target")
                            emit(result)
                        } else emit(null)
                    }
            }
        }
    }
}