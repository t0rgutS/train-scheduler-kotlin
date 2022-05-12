package ru.mirea.trainscheduler.repository.room

import kotlinx.coroutines.flow.*
import ru.mirea.trainscheduler.model.Location
import ru.mirea.trainscheduler.repository.LocalScheduleRepository
import ru.mirea.trainscheduler.repository.room.dao.LocationDao
import java.util.concurrent.Executor

class RoomRepository(
    private val executor: Executor,
    private val locationDao: LocationDao,
) : LocalScheduleRepository {

    override fun addLocationList(locationList: List<Location>) {
        executor.execute {
            locationDao.addLocationList(locationList)
        }
    }

    override fun locationsExists(): Boolean {
        return locationDao.countLocations() > 0//.map { it > 0 }
    }

    override fun findLocations(searchBy: String): Flow<List<Location>> {
        return locationDao.suggestLocations(searchBy)
    }


}