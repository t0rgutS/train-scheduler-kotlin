package ru.mirea.trainscheduler.repository

import kotlinx.coroutines.flow.Flow
import ru.mirea.trainscheduler.model.Location

interface LocalScheduleRepository {
    fun addLocationList(
        locationList: List<Location>,
    )

    fun locationsExists(): Boolean

    fun findLocations(searchBy: String): Flow<List<Location>>
}