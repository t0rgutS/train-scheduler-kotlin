package ru.mirea.trainscheduler.repository.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mirea.trainscheduler.BuildConfig
import ru.mirea.trainscheduler.model.*
import ru.mirea.trainscheduler.repository.CurrencyRepository
import ru.mirea.trainscheduler.repository.RemoteScheduleRepository
import ru.mirea.trainscheduler.repository.ScheduleRepository
import ru.mirea.trainscheduler.repository.network.model.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

class NetworkRepository : RemoteScheduleRepository, CurrencyRepository {
    companion object {
        private const val yandexApiKey = BuildConfig.YANDEX_SCHEDULE_API_KEY
        private const val exchangeApiKey = BuildConfig.EXCHANGE_RATE_API_KEY
    }

    override fun getAvailableLocations(): Flow<List<Location>> = callbackFlow {
        val stationCallback = object : Callback<StationList> {
            override fun onResponse(
                call: Call<StationList>,
                response: Response<StationList>,
            ) {
                val locationList: MutableList<Location> = mutableListOf()
                if (response.isSuccessful) {
                    val remoteStations = response.body()
                    remoteStations?.countries?.forEach { country ->
                        country.regions.forEach { region ->
                            region.settlements.forEach { settlement ->
                                if (settlement.codes != null) {
                                    val location = Location()
                                    location.country = country.title
                                    location.region = region.title
                                    location.city = settlement.title
                                    location.codes = settlement.codes
                                    locationList.add(location)
                                }
                            }
                        }
                    }
                }
                trySend(locationList)
                close()
            }

            override fun onFailure(call: Call<StationList>, t: Throwable) {
                close(t)
            }

        }
        val call = YandexScheduleApiClient.getApi()
            .getAvailableLocations(yandexApiKey)
        call.enqueue(stationCallback)
        awaitClose { call.cancel() }
    }

    override fun getSchedule(
        from: String,
        to: String,
        date: String,
        type: String,
    ): Flow<List<ScheduleSegment>> = flow {
        var readCount: Int = 0
        var totalCount: Int = 0
        val scheduleSegmentList: MutableList<ScheduleSegment> = mutableListOf()
        do {
            val trainSchedule = YandexScheduleApiClient.getApi()
                .getTrainSchedule(yandexApiKey, from, to, date, type, readCount).execute().body()
            if (trainSchedule != null) {
                if (trainSchedule.pagination != null) {
                    totalCount = trainSchedule.pagination!!.total!!
                    readCount += trainSchedule.intervalSegments.size + trainSchedule.segments.size
                }
                trainSchedule.segments.forEach { segment ->
                    val scheduleSegment = ScheduleSegment()
                    scheduleSegment.from = segment.from?.let { mapStation(it) }
                    scheduleSegment.to = segment.to?.let { mapStation(it) }
                    scheduleSegment.setDeparture(segment.departure)
                    scheduleSegment.setArrival(segment.arrival)
                    scheduleSegment.threadUID = segment.thread?.uid
                    segment.ticketsInfo?.let { ticketsInfo ->
                        mapTickets(ticketsInfo)?.let {
                            scheduleSegment.tickets.addAll(it)
                        }
                    }
                    scheduleSegmentList.add(scheduleSegment)
                }
            }
        } while (readCount < totalCount)
        emit(scheduleSegmentList)
    }

    override fun getFollowStations(
        uid: String,
        from: String?,
        to: String?,
    ): Flow<List<Station>> = callbackFlow {
        val followStationCallback = object : Callback<FollowStations> {
            override fun onResponse(
                call: Call<FollowStations>,
                response: Response<FollowStations>,
            ) {
                val followStations: MutableList<Station> = mutableListOf()
                if (response.isSuccessful) {
                    val receivedStations = response.body()
                    if (receivedStations?.stops != null)
                        followStations.addAll(receivedStations.stops.mapNotNull { stop ->
                            if (stop.station != null) {
                                val station = mapStation(stop.station!!)
                                station.setArrival(stop.arrival)
                                station.setDeparture(stop.departure)
                                station
                            } else null
                        })
                }
                trySend(followStations)
                close()
            }

            override fun onFailure(call: Call<FollowStations>, t: Throwable) {
                close(t)
            }
        }
        val call = YandexScheduleApiClient.getApi().getFollowStations(yandexApiKey, uid, from, to)
        call.enqueue(followStationCallback)
        awaitClose { call.cancel() }
    }

    private fun mapStation(rawStation: YandexApiStation): Station {
        val station = Station()
        station.transportType = rawStation.transportType
        station.stationType = rawStation.stationType
        station.codes = when {
            rawStation.codes != null -> rawStation.codes
            rawStation.code != null -> mapOf(Pair("yandex", rawStation.code!!))
            else -> emptyMap()
        }
        station.title = rawStation.title
        return station
    }

    private fun mapTickets(ticketsInfo: TrainSchedule.TicketsInfo): List<Ticket>? {
        return ticketsInfo.places.stream().map { place ->
            val ticket = Ticket()
            ticket.currency = place.currency
            ticket.price = place.price?.cents?.toDouble()?.div(100)
                ?.let { place.price?.whole?.toDouble()?.plus(it) }
            ticket.displayCurrency = place.currency
            ticket.canBeElectronic = ticketsInfo.etMarker
            ticket
        }?.collect(Collectors.toList())
    }

    override fun getCurrencies(): Flow<List<Currency>> = callbackFlow {
        val currencyCallback = object : Callback<CurrencyCodes?> {
            override fun onResponse(
                call: Call<CurrencyCodes?>,
                response: Response<CurrencyCodes?>,
            ) {
                val currencyList = mutableListOf<Currency>()
                if (response.isSuccessful) {
                    val currencyCodes = response.body()
                    if (currencyCodes?.supportedCodes != null) {
                        currencyList.addAll(currencyCodes.supportedCodes
                            .stream().map { codePair ->
                                Currency(codePair[0])
                            }.collect(Collectors.toList()))
                    }
                }
                trySend(currencyList)
                close()
            }

            override fun onFailure(call: Call<CurrencyCodes?>, t: Throwable) {
                close(t)
            }
        }
        val call = ExchangeRateApiClient.getApi().getSupportedCodes(exchangeApiKey)
        call.enqueue(currencyCallback)
        awaitClose { call.cancel() }
    }

    override fun getExchange(source: String, target: String): Flow<CurrencyExchange?> =
        callbackFlow {
            val exchangeCallback = object : Callback<ConvertPair?> {
                override fun onResponse(
                    call: Call<ConvertPair?>,
                    response: Response<ConvertPair?>,
                ) {
                    if (response.isSuccessful && response.body() != null)
                        trySend(convertRemoteExchange(response.body()!!))
                    else trySend(null)
                    close()

                }

                override fun onFailure(call: Call<ConvertPair?>, t: Throwable) {
                    close(t)
                }
            }
            val call =
                ExchangeRateApiClient.getApi().getConvertPairRate(exchangeApiKey, source, target)
            call.enqueue(exchangeCallback)
            awaitClose { call.cancel() }
        }

    private fun convertRemoteExchange(remoteExchange: ConvertPair): CurrencyExchange {
        val currencyExchange = CurrencyExchange()
        currencyExchange.source = remoteExchange.baseCode
        currencyExchange.target = remoteExchange.targetCode
        currencyExchange.lastModifiedOn = remoteExchange.timeLastUpdateUnix
        currencyExchange.nextUpdateOn = remoteExchange.timeNextUpdateUnix
        currencyExchange.rate = remoteExchange.conversionRate
        return currencyExchange
    }
}