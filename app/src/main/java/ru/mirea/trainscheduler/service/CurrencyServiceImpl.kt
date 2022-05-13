package ru.mirea.trainscheduler.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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
    override fun getCurrencies(): Flow<List<Currency>> {
        return if (localRepository.currenciesExists())
            localRepository.getCurrencies()
        else remoteRepository.getCurrencies().map { remoteCurrencies ->
            localRepository.addCurrencyList(remoteCurrencies)
            remoteCurrencies
        }
    }

    override fun convert(source: String, target: String, value: Double): Flow<Double?> = flow {
        localRepository.getExchange(source, target).collect { localExchange ->
            val nextUpdate = if(localExchange?.nextUpdateOn != null)
                Instant.ofEpochMilli(localExchange.nextUpdateOn!!)
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