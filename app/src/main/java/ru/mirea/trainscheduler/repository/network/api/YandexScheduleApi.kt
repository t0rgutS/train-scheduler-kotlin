package ru.mirea.trainscheduler.repository.network.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.mirea.trainscheduler.repository.network.model.FollowStations
import ru.mirea.trainscheduler.repository.network.model.StationList
import ru.mirea.trainscheduler.repository.network.model.TrainSchedule

interface YandexScheduleApi {
    @GET("/v3.0/stations_list")
    fun getAvailableLocations(@Header("Authorization") apiKey: String): Call<StationList>

    @GET("/v3.0/search")
    fun getTrainSchedule(
        @Header("Authorization") apiKey: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("date") date: String,
        @Query("transport_types") type: String,
        @Query("offset") offset: Int,
    ): Call<TrainSchedule>

    @GET("/v3.0/thread")
    fun getFollowStations(
        @Header("Authorization") apiKey: String,
        @Query("uid") uid: String,
        @Query("from") from: String?,
        @Query("to") to: String?,
    ): Call<FollowStations>
}