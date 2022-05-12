package ru.mirea.trainscheduler.repository

import kotlinx.coroutines.flow.*
import ru.mirea.trainscheduler.BuildConfig
import ru.mirea.trainscheduler.model.CurrencyExchange
import ru.mirea.trainscheduler.repository.network.ExchangeRateApiClient
import ru.mirea.trainscheduler.repository.network.model.ConvertPair
import ru.mirea.trainscheduler.repository.room.dao.ExchangeDao
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class CurrencyConverterImpl(private val exchangeDao: ExchangeDao) : CurrencyConverter {
    companion object {
        private const val apiKey = BuildConfig.EXCHANGE_RATE_API_KEY
    }

    override fun convert(source: String, target: String, value: Double): Flow<Double?> {
        return exchangeDao.getExchange(source, target).map { localExchange ->
            if (localExchange != null) {
                val nextUpdate = Instant.ofEpochMilli(localExchange.nextUpdateOn!!)
                    .atZone(ZoneId.systemDefault()).toLocalDate()
                if (nextUpdate.isAfter(LocalDate.now()) && localExchange.rate != null) {
                    value * localExchange.rate!!
                } else null
            } else ExchangeRateApiClient.getApi().getConvertPairRate(apiKey, source, target)
                .map { remoteExchange ->
                    if (remoteExchange?.conversionRate != null) {
                        val exchangeConverted = convertRemoteExchange(remoteExchange)
                        exchangeDao.addExchange(exchangeConverted)
                        value * exchangeConverted.rate!!
                    } else null
                }.first()
        }
    }

    private fun convertRemoteExchange(remoteExchange: ConvertPair): CurrencyExchange {
        val currencyExchange = CurrencyExchange()
        currencyExchange.source = remoteExchange.baseCode
        currencyExchange.target = remoteExchange.targetCode
        currencyExchange.lastModifiedOn = remoteExchange.timeLastUpdateUnix
        currencyExchange.nextUpdateOn = remoteExchange.timeNextUpdateUnix
        currencyExchange.rate = remoteExchange.conversionRate
        return currencyExchange
    }
}