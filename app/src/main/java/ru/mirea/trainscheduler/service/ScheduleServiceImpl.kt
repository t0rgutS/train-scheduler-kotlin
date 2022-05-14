package ru.mirea.trainscheduler.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.mirea.trainscheduler.model.Location
import ru.mirea.trainscheduler.model.ScheduleSegment
import ru.mirea.trainscheduler.model.Station
import ru.mirea.trainscheduler.repository.LocalScheduleRepository
import ru.mirea.trainscheduler.repository.RemoteScheduleRepository

class ScheduleServiceImpl(
    private val remoteRepository: RemoteScheduleRepository,
    private val localRepository: LocalScheduleRepository,
) : ScheduleService {
    private val cachedLocations: MutableList<Location> = mutableListOf()

    override suspend fun init() {
        if (!localRepository.locationsExists()) {
            remoteRepository.getAvailableLocations().collect { remoteLocations ->
                cachedLocations.addAll(remoteLocations)
                localRepository.addLocationList(remoteLocations)
            }
        }
    }

    override fun suggestLocations(suggestBy: String): Flow<List<Location>> {
        return localRepository.suggestLocations(suggestBy).map { locations ->
            if (cachedLocations.isNotEmpty()) {
                if (locations.isEmpty())
                    cachedLocations
                else {
                    cachedLocations.removeAll(locations)
                    locations
                }
            } else locations
        }
    }

    override fun findLocation(searchBy: String): Location? {
        val location = localRepository.findLocation(searchBy)
        if (location != null && cachedLocations.contains(location)) {
            cachedLocations.remove(location)
            return location
        } else if (location == null && cachedLocations.isNotEmpty())
            return cachedLocations.find { it.city?.startsWith(searchBy) == true }
        return location
    }

    override fun getSchedule(
        from: String,
        to: String,
        date: String,
        type: String,
    ): Flow<List<ScheduleSegment>> {
        return remoteRepository.getSchedule(from, to, date, type)
    }

    override fun getFollowStations(
        uid: String,
        from: String?,
        to: String?,
    ): Flow<List<Station>> {
        return remoteRepository.getFollowStations(uid, from, to)
    }

    override fun getLocationCount(): Long {
        return localRepository.getLocationCount()
    }

    override fun updateLocationList() {
        flow<Unit> {
            remoteRepository.getAvailableLocations().collect { remoteLocations ->
                if (remoteLocations.isNotEmpty()) {
                    localRepository.addLocationList(remoteLocations)
                }
            }
        }
    }

}