package ru.mirea.trainscheduler

import android.content.Context
import com.google.gson.Gson
import ru.mirea.trainscheduler.repository.network.NetworkRepository
import ru.mirea.trainscheduler.repository.room.RoomRepository
import ru.mirea.trainscheduler.repository.room.TrainScheduleDatabase
import ru.mirea.trainscheduler.service.CurrencyService
import ru.mirea.trainscheduler.service.CurrencyServiceImpl
import ru.mirea.trainscheduler.service.ScheduleService
import ru.mirea.trainscheduler.service.ScheduleServiceImpl

object ServiceLocator {
    private lateinit var scheduleService: ScheduleService
    private lateinit var currencyService: CurrencyService
    private lateinit var gson: Gson

    fun init(context: Context) {
        gson = Gson()
        val networkRepository = NetworkRepository()
        val roomDatabase = TrainScheduleDatabase.getDatabase(context)
        val roomRepository = RoomRepository(roomDatabase.executor, roomDatabase.locationDao(),
            roomDatabase.exchangeDao())
        scheduleService = ScheduleServiceImpl(networkRepository, roomRepository)
        currencyService = CurrencyServiceImpl(networkRepository, roomRepository)
    }

    fun getScheduleService(): ScheduleService {
        return scheduleService
    }

    fun getCurrencyService(): CurrencyService {
        return currencyService
    }

    fun getGson(): Gson {
        return gson
    }
}