package ru.mirea.trainscheduler.repository.room

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mirea.trainscheduler.model.Currency
import ru.mirea.trainscheduler.model.CurrencyExchange
import ru.mirea.trainscheduler.model.Location
import ru.mirea.trainscheduler.repository.LocalCurrencyRepository
import ru.mirea.trainscheduler.repository.LocalScheduleRepository
import ru.mirea.trainscheduler.repository.room.dao.ExchangeDao
import ru.mirea.trainscheduler.repository.room.dao.LocationDao
import java.util.concurrent.Executor

class RoomRepository(
    private val executor: Executor,
    private val locationDao: LocationDao,
    private val exchangeDao: ExchangeDao,
) : LocalScheduleRepository, LocalCurrencyRepository {
    companion object {
        const val TAG = "Local Repository"
    }

    override fun addLocationList(locationList: List<Location>) {
        executor.execute {
            locationDao.addLocationList(locationList)
            Log.d(TAG, "Локации успешно внесены в локальную базу данных")
        }
    }

    override fun addLocation(location: Location) {
        executor.execute {
            if (!locationDao.locationExists(location.city, location.region, location.country))
                locationDao.addLocation(location)
        }
    }

    override fun getLocationCount(): Flow<Long> {
        return locationDao.countLocations()
    }

    override fun locationsExists(): Flow<Boolean> {
        return locationDao.countLocations().map { it > 0 }
    }

    override fun suggestLocations(suggestBy: String): Flow<List<Location>> {
        return locationDao.suggestLocations(suggestBy)
    }

    override fun findLocation(searchBy: String): Location? {
        val found = locationDao.findLocation(searchBy)
        return if (found.size == 1) found[0] else null
    }

    override fun currenciesExists(): Flow<Boolean> {
        return exchangeDao.countCurrencies().map { it < 0 }
    }

    override fun addCurrencyList(currencyList: List<Currency>) {
        executor.execute {
            exchangeDao.addCurrencyList(currencyList)
            Log.d(TAG, "В локальную базу данных добавлено ${currencyList.size} кодов валют")
        }
    }

    override fun addCurrency(currency: Currency) {
        executor.execute {
            currency.code?.let {
                if (!exchangeDao.currencyExists(it))
                    exchangeDao.addCurrency(currency)
            }
        }
    }

    override fun addExchange(exchange: CurrencyExchange) {
        executor.execute {
            exchangeDao.addExchange(exchange)
            Log.d(TAG, "В локальную базу данных добавлена конвертация из ${exchange.source} " +
                    "в ${exchange.target}")
        }
    }

    override fun getCurrencyCount(): Flow<Long> {
        return exchangeDao.countCurrencies()
    }

    override fun getExchangeCount(): Flow<Long> {
        return exchangeDao.countExchanges()
    }

    override fun clearExchanges() {
        executor.execute {
            exchangeDao.clearExchanges()
        }
    }

    override fun getCurrencies(): Flow<List<Currency>> {
        return exchangeDao.getCurrencies()
    }

    override fun getExchange(source: String, target: String): Flow<CurrencyExchange?> {
        return exchangeDao.getExchange(source, target)
    }
}