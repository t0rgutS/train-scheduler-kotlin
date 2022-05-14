package ru.mirea.trainscheduler.service

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

    override suspend fun init() {
        if (!localRepository.currenciesExists()) {
            remoteRepository.getCurrencies().collect { remoteCurrencies ->
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
        localRepository.getExchange(source, target).collect { localExchange ->
            val nextUpdate = if (localExchange?.nextUpdateOn != null)
                Instant.ofEpochSecond(localExchange.nextUpdateOn!!)
                    .atZone(ZoneId.systemDefault()).toLocalDate() else null
            if (nextUpdate != null && nextUpdate.isAfter(LocalDate.now()) && localExchange?.rate != null) {
                emit(value * localExchange.rate!!)
            } else remoteRepository.getExchange(source, target)
                .collect { remoteExchange ->
                    if (remoteExchange != null) {
                        localRepository.addExchange(remoteExchange)
                        emit(value * remoteExchange.rate!!)
                    } else emit(null)
                }
        }
    }
}