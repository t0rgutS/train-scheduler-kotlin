package ru.mirea.trainscheduler.repository

import kotlinx.coroutines.flow.Flow
import ru.mirea.trainscheduler.model.Location
import ru.mirea.trainscheduler.model.ScheduleSegment
import ru.mirea.trainscheduler.model.Station

interface RemoteScheduleRepository {
    fun getAvailableLocations(): Flow<List<Location>>

    fun getSchedule(
        from: String,
        to: String,
        date: String,
        type: String,
    ): Flow<List<ScheduleSegment>>

    fun getFollowStations(uid: String, from: String?, to: String?): Flow<List<Station>>
}