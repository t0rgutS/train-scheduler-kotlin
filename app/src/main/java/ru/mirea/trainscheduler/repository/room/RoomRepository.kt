package ru.mirea.trainscheduler.repository.room

import kotlinx.coroutines.flow.Flow
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

    override fun addLocationList(locationList: List<Location>) {
        executor.execute {
            locationDao.addLocationList(locationList)
        }
    }

    override fun locationsExists(): Boolean {
        return locationDao.countLocations() > 0//.map { it > 0 }
    }

    override fun suggestLocations(suggestBy: String): Flow<List<Location>> {
        return locationDao.suggestLocations(suggestBy)
    }

    override fun findLocation(searchBy: String): Flow<Location?> {
        return locationDao.findLocation(searchBy)
    }

    override fun currenciesExists(): Boolean {
        return exchangeDao.countCurrencies() > 0
    }

    override fun addCurrencyList(currencyList: List<Currency>) {
        executor.execute {
            exchangeDao.addCurrencyList(currencyList)
        }
    }

    override fun addExchange(exchange: CurrencyExchange) {
        executor.execute {
            exchangeDao.addExchange(exchange)
        }
    }

    override fun getCurrencies(): Flow<List<Currency>> {
        return exchangeDao.getCurrencies()
    }

    override fun getExchange(source: String, target: String): Flow<CurrencyExchange?> {
        return exchangeDao.getExchange(source, target)
    }


}