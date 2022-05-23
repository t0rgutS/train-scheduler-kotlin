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
    private lateinit var scheduleService: ScheduleDataService
    private lateinit var currencyService: CurrencyDataService
    private lateinit var profileService: ProfileDataService
    private lateinit var gson: Gson

    fun init(context: Context, scope: CoroutineScope) {
        gson = Gson()
        val networkRepository = NetworkRepository()
        val roomDatabase = TrainScheduleDatabase.getDatabase(context)
        val roomRepository = RoomRepository(roomDatabase.executor, roomDatabase.locationDao(),
            roomDatabase.exchangeDao(), roomDatabase.profileDao())
        scheduleService = ScheduleDataServiceImpl(networkRepository, roomRepository)
        currencyService = CurrencyDataServiceImpl(networkRepository, roomRepository)
        profileService = ProfileDataServiceImpl(roomRepository)
        scope.launch(Dispatchers.IO) {
            scheduleService.init()
            currencyService.init()
            profileService.init()
        }
    }

    fun getScheduleService(): ScheduleDataService {
        return scheduleService
    }

    fun getCurrencyService(): CurrencyDataService {
        return currencyService
    }

    fun getProfileService(): ProfileDataService {
        return profileService
    }

    fun getGson(): Gson {
        return gson
    }
}