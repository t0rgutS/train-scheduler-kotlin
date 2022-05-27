package ru.mirea.trainscheduler.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.mirea.trainscheduler.model.Location
import ru.mirea.trainscheduler.model.ScheduleSegment
import ru.mirea.trainscheduler.model.Station
import ru.mirea.trainscheduler.repository.LocalScheduleRepository
import ru.mirea.trainscheduler.repository.RemoteScheduleRepository

class ScheduleDataServiceImpl(
    private val remoteRepository: RemoteScheduleRepository,
    private val localRepository: LocalScheduleRepository,
) : ScheduleDataService {
    companion object {
        const val TAG = "Train Schedule"
    }

    private val cachedLocations: MutableList<Location> = mutableListOf()

    override suspend fun init() {
        if (!localRepository.locationsExists().first()) {
            remoteRepository.getAvailableLocations().collect { remoteLocations ->
                Log.d(TAG,
                    "Выполняется загрузка ${remoteLocations.size} в локальную базу данных. " +
                            "Для оперативного доступа локации сохраняются во временный кэш")
                cachedLocations.addAll(remoteLocations)
                localRepository.addLocationList(remoteLocations)
            }
        }
    }

    override fun suggestLocations(suggestBy: String): Flow<List<Location>> {
        Log.d(TAG, "Поиск локаций по подсказке '$suggestBy'")
        return localRepository.suggestLocations(suggestBy).map { locations ->
            Log.d(TAG,
                "В локальной базе данных по подсказке $suggestBy найдено ${locations.size} " +
                        "локаций")
            if (cachedLocations.isNotEmpty()) {
                if (locations.isEmpty()) {
                    Log.d(TAG, "Выполняется поиск во временном кэше по подсказке $suggestBy")
                    cachedLocations.filter { it.city?.startsWith(suggestBy) == true }
                } else {
                    Log.d(TAG, "Выполняется очистка найденных локаций из временного кэша")
                    cachedLocations.removeAll(locations)
                    locations
                }
            } else locations
        }
    }

    override fun findLocation(searchBy: String): Location? {
        Log.d(TAG, "Поиск локации '$searchBy'")
        val location = localRepository.findLocation(searchBy)
        Log.d(TAG, "Локация '$searchBy' ${if (location == null) "не " else ""} найдена")
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
        Log.d(TAG, "Выполняется поиск расписания:" +
                "\n\tКод локации отправления: $from" +
                "\n\tКод локации прибытия: $to" +
                "\n\tДата: $date" +
                "\n\tТип транспорта: $type")
        return remoteRepository.getSchedule(from, to, date, type)
    }

    override fun getFollowStations(
        uid: String,
        from: String?,
        to: String?,
    ): Flow<List<Station>> {
        Log.d(TAG, "Выполняется поиск станций следования:" +
                "\n\tКод локации отправления: $from" +
                "\n\tКод локации прибытия: $to" +
                "\n\tИдентификатор ветки: $uid")
        return remoteRepository.getFollowStations(uid, from, to)
    }

    override fun getLocationCount(): Flow<Long> {
        return localRepository.getLocationCount()
    }

    override suspend fun updateLocationList() {
        remoteRepository.getAvailableLocations().collect { remoteLocations ->
            if (remoteLocations.isNotEmpty()) {
                Log.d(TAG,
                    "Выполняется ресинхронизация ${remoteLocations.size} локаций. " +
                            "Для оперативного доступа локации сохраняются во временный кэш")
                remoteLocations.forEach { location ->
                    if (!cachedLocations.contains(location))
                        cachedLocations.add(location)
                    localRepository.addLocation(location)
                }
            }
        }
    }

}