package ru.mirea.trainscheduler.repository.network.api

import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.mirea.trainscheduler.repository.network.model.ConvertPair
import ru.mirea.trainscheduler.repository.network.model.CurrencyCodes

interface ExchangeApi {
    @GET("/v6/{key}/pair/{source}/{target}")
    fun getConvertPairRate(
        @Path("key") apiKey: String,
        @Path("source") sourceCurrency: String,
        @Path("target") targetCurrency: String,
    ): Flow<ConvertPair?>

    @GET("/v6/{key}/codes")
    fun getSupportedCodes(@Path("key") apiKey: String?): Flow<CurrencyCodes?>
}