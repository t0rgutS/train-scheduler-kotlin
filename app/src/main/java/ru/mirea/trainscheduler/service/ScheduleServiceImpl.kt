package ru.mirea.trainscheduler.service

import kotlinx.coroutines.flow.*
import ru.mirea.trainscheduler.model.Location
import ru.mirea.trainscheduler.model.ScheduleSegment
import ru.mirea.trainscheduler.model.Station
import ru.mirea.trainscheduler.repository.LocalScheduleRepository
import ru.mirea.trainscheduler.repository.RemoteScheduleRepository
import java.util.*

class ScheduleServiceImpl(
    private val remoteRepository: RemoteScheduleRepository,
    private val localRepository: LocalScheduleRepository,
) : ScheduleService {

    override fun findLocations(searchBy: String): Flow<List<Location>> {
        return if (localRepository.locationsExists())
            localRepository.findLocations(searchBy)
        else
            remoteRepository.getAvailableLocations()
                .map { remoteLocations ->
                    val filteredLocations = remoteLocations.filter {
                        it.country?.contains(searchBy) == true
                                || it.region?.contains(searchBy) == true
                                || it.city?.contains(searchBy) == true
                    }
                    localRepository.addLocationList(remoteLocations)
                    filteredLocations
                }
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