package ru.mirea.trainscheduler

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.mirea.trainscheduler.repository.network.NetworkRepository
import ru.mirea.trainscheduler.repository.room.RoomRepository
import ru.mirea.trainscheduler.repository.room.TrainScheduleDatabase
import ru.mirea.trainscheduler.service.*

object ServiceLocator {
    private lateinit var scheduleService: ScheduleService
    private lateinit var currencyService: CurrencyService
    private lateinit var profileService: ProfileService
    private lateinit var gson: Gson

    fun init(context: Context, scope: CoroutineScope) {
        gson = Gson()
        val networkRepository = NetworkRepository()
        val roomDatabase = TrainScheduleDatabase.getDatabase(context)
        val roomRepository = RoomRepository(roomDatabase.executor, roomDatabase.locationDao(),
            roomDatabase.exchangeDao(), roomDatabase.profileDao())
        scheduleService = ScheduleServiceImpl(networkRepository, roomRepository)
        currencyService = CurrencyServiceImpl(networkRepository, roomRepository)
        profileService = ProfileServiceImpl(roomRepository)
        scope.launch(Dispatchers.IO) {
            scheduleService.init()
            currencyService.init()
            profileService.init()
        }
    }

    fun getScheduleService(): ScheduleService {
        return scheduleService
    }

    fun getCurrencyService(): CurrencyService {
        return currencyService
    }

    fun getProfileService(): ProfileService {
        return profileService
    }

    fun getGson(): Gson {
        return gson
    }
}