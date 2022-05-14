package ru.mirea.trainscheduler.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.mirea.trainscheduler.model.Currency
import ru.mirea.trainscheduler.model.CurrencyExchange
import ru.mirea.trainscheduler.model.Location
import ru.mirea.trainscheduler.model.Profile
import ru.mirea.trainscheduler.repository.room.converter.MapToStringConverter
import ru.mirea.trainscheduler.repository.room.dao.ExchangeDao
import ru.mirea.trainscheduler.repository.room.dao.LocationDao
import ru.mirea.trainscheduler.repository.room.dao.ProfileDao
import java.util.concurrent.Executors

@Database(entities = [Location::class, CurrencyExchange::class, Currency::class, Profile::class],
    version = 2)
@TypeConverters(MapToStringConverter::class)
abstract class TrainScheduleDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun exchangeDao(): ExchangeDao
    abstract fun profileDao(): ProfileDao
    val executor = Executors.newFixedThreadPool(THREAD_MAX_COUNT)

    companion object {
        @Volatile
        private var INSTANCE: TrainScheduleDatabase? = null

        const val THREAD_MAX_COUNT = 5

        fun getDatabase(context: Context): TrainScheduleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    TrainScheduleDatabase::class.java,
                    "TrainScheduleDatabase"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}