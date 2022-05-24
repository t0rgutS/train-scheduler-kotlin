package ru.mirea.trainscheduler.repository

import kotlinx.coroutines.flow.Flow
import ru.mirea.trainscheduler.model.Location

interface LocalScheduleRepository {
    fun addLocationList(locationList: List<Location>)

    fun addLocation(location: Location)

    fun getLocationCount(): Long

    fun locationsExists(): Boolean

    fun suggestLocations(suggestBy: String): Flow<List<Location>>

    fun findLocation(searchBy: String): Location?
}