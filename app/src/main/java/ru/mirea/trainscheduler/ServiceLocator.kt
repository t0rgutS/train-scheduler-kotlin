package ru.mirea.trainscheduler

import android.content.Context
import com.google.gson.Gson
import ru.mirea.trainscheduler.repository.CurrencyConverter
import ru.mirea.trainscheduler.repository.CurrencyConverterImpl
import ru.mirea.trainscheduler.repository.CurrencyRepository
import ru.mirea.trainscheduler.repository.CurrencyRepositoryImpl
import ru.mirea.trainscheduler.repository.network.NetworkRepository
import ru.mirea.trainscheduler.repository.room.RoomRepository
import ru.mirea.trainscheduler.repository.room.TrainScheduleDatabase
import ru.mirea.trainscheduler.service.ScheduleService
import ru.mirea.trainscheduler.service.ScheduleServiceImpl

object ServiceLocator {
    private lateinit var scheduleService: ScheduleService
    private lateinit var currencyConverter: CurrencyConverter
    private lateinit var currencyRepository: CurrencyRepository
    private lateinit var gson: Gson

    fun init(context: Context) {
        gson = Gson()
        val networkRepository = NetworkRepository()
        val roomDatabase = TrainScheduleDatabase.getDatabase(context)
        val roomRepository = RoomRepository(roomDatabase.executor, roomDatabase.locationDao())
        scheduleService = ScheduleServiceImpl(networkRepository, roomRepository)
        currencyConverter = CurrencyConverterImpl(roomDatabase.exchangeDao())
        currencyRepository = CurrencyRepositoryImpl(roomDatabase.exchangeDao())
    }

    fun getScheduleService(): ScheduleService {
        return scheduleService
    }

    fun getCurrencyConverter(): CurrencyConverter {
        return currencyConverter
    }

    fun getCurrencyRepository(): CurrencyRepository {
        return currencyRepository
    }

    fun getGson(): Gson {
        return gson
    }
}