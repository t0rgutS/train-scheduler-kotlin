package ru.mirea.trainscheduler.service

import kotlinx.coroutines.flow.Flow
import ru.mirea.trainscheduler.model.Location
import ru.mirea.trainscheduler.model.ScheduleSegment
import ru.mirea.trainscheduler.model.Station

interface ScheduleDataService : DataService {
    fun findLocation(searchBy: String): Location?

    fun suggestLocations(suggestBy: String): Flow<List<Location>>

    fun getSchedule(
        from: String,
        to: String,
        date: String,
        type: String,
    ): Flow<List<ScheduleSegment>>

    fun getFollowStations(
        uid: String,
        from: String?,
        to: String?,
    ): Flow<List<Station>>

    fun getLocationCount(): Flow<Long>

    fun updateLocationList()
}