package ru.mirea.trainscheduler.repository.room

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mirea.trainscheduler.model.Currency
import ru.mirea.trainscheduler.model.CurrencyExchange
import ru.mirea.trainscheduler.model.Location
import ru.mirea.trainscheduler.model.Profile
import ru.mirea.trainscheduler.repository.LocalCurrencyRepository
import ru.mirea.trainscheduler.repository.LocalScheduleRepository
import ru.mirea.trainscheduler.repository.ProfileRepository
import ru.mirea.trainscheduler.repository.room.dao.ExchangeDao
import ru.mirea.trainscheduler.repository.room.dao.LocationDao
import ru.mirea.trainscheduler.repository.room.dao.ProfileDao
import java.util.concurrent.Executor

class RoomRepository(
    private val executor: Executor,
    private val locationDao: LocationDao,
    private val exchangeDao: ExchangeDao,
    private val profileDao: ProfileDao,
) : LocalScheduleRepository, LocalCurrencyRepository, ProfileRepository {
    companion object {
        const val TAG = "Local Repository"
    }

    override fun addLocationList(locationList: List<Location>) {
        executor.execute {
            locationDao.addLocationList(locationList)
            Log.d(TAG, "Локации успешно внесены в локальную базу данных")
        }
    }

    override fun getLocationCount(): Long {
        return locationDao.countLocations()
    }

    override fun locationsExists(): Boolean {
        return locationDao.countLocations() > 0//.map { it > 0 }
    }

    override fun suggestLocations(suggestBy: String): Flow<List<Location>> {
        return locationDao.suggestLocations(suggestBy)
    }

    override fun findLocation(searchBy: String): Location? {
        val found = locationDao.findLocation(searchBy)
        return if (found.size == 1) found[0] else null
    }

    override fun currenciesExists(): Boolean {
        return exchangeDao.countCurrencies() > 0
    }

    override fun addCurrencyList(currencyList: List<Currency>) {
        executor.execute {
            exchangeDao.addCurrencyList(currencyList)
            Log.d(TAG, "В локальную базу данных добавлено ${currencyList.size} кодов валют")
        }
    }

    override fun addExchange(exchange: CurrencyExchange) {
        executor.execute {
            exchangeDao.addExchange(exchange)
            Log.d(TAG, "В локальную базу данных добавлена конвертация из ${exchange.source} " +
                    "в ${exchange.target}")
        }
    }

    override fun getExchangeCount(): Long {
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

    override fun getProfileByCode(code: String): Profile? {
        return profileDao.getProfileByCode(code)
    }

    override fun upsertProfile(profile: Profile) {
        executor.execute {
            profileDao.upsertProfile(profile)
            Log.i(TAG, "Задано новое значение системного профиля ${profile.code}: ${profile.value}")
        }
    }


}