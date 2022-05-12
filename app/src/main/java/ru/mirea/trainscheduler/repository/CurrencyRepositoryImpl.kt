package ru.mirea.trainscheduler.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.mirea.trainscheduler.BuildConfig
import ru.mirea.trainscheduler.model.Currency
import ru.mirea.trainscheduler.repository.network.ExchangeRateApiClient
import ru.mirea.trainscheduler.repository.room.dao.ExchangeDao
import java.util.stream.Collectors

class CurrencyRepositoryImpl(private val exchangeDao: ExchangeDao) : CurrencyRepository {
    companion object {
        private const val apiKey = BuildConfig.EXCHANGE_RATE_API_KEY
    }

    override fun getCurrencies(): Flow<List<Currency>> {
        return exchangeDao.getCurrencies().map { currencies ->
            currencies.ifEmpty {
                ExchangeRateApiClient.getApi().getSupportedCodes(apiKey).map { remoteCurrencies ->
                    remoteCurrencies?.supportedCodes
                        ?.stream()?.map { codePair ->
                            Currency(codePair[0])
                        }?.collect(Collectors.toList())
                    remoteCurrencies
                }
            }
            currencies
        }
    }
}