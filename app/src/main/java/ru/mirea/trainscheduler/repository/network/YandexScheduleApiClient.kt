package ru.mirea.trainscheduler.repository.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mirea.trainscheduler.repository.network.api.YandexScheduleApi

object YandexScheduleApiClient {
    private const val BASE_URL: String = "https://api.rasp.yandex.net/"
    private var retrofit: Retrofit? = null
    private var api: YandexScheduleApi? = null

    fun getApi(): YandexScheduleApi {
        if (api == null) {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            api = retrofit?.create(YandexScheduleApi::class.java)
        }
        return api!!
    }
}