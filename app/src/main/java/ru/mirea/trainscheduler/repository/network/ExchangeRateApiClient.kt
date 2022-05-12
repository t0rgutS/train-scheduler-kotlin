package ru.mirea.trainscheduler.repository.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mirea.trainscheduler.repository.network.api.ExchangeApi

object ExchangeRateApiClient {
    private val BASE_URL: String = "https://v6.exchangerate-api.com/"
    private var retrofit: Retrofit? = null
    private var api: ExchangeApi? = null

    fun getApi(): ExchangeApi {
        if (api == null) {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            api = retrofit?.create(ExchangeApi::class.java)
        }
        return api!!
    }
}