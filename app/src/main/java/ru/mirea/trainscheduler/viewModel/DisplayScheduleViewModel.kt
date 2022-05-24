package ru.mirea.trainscheduler.viewModel

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.mirea.trainscheduler.ServiceLocator
import ru.mirea.trainscheduler.TrainSchedulerConstants
import ru.mirea.trainscheduler.model.ScheduleSegment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DisplayScheduleViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val FROM_CODE_ARG = "fromCode"
        const val TO_CODE_ARG = "toCode"
        const val DATE_ARG = "date"
        const val TRANSPORT_TYPE_ARG = "transportType"
    }

    private var fromCode: String? = null
    private var toCode: String? = null
    private var date: String? = null
    private var transportType: String? = null

    fun init(arguments: Bundle) {
        fromCode = arguments.getString(FROM_CODE_ARG)
        toCode = arguments.getString(TO_CODE_ARG)
        date = arguments.getString(DATE_ARG)
        transportType = arguments.getString(TRANSPORT_TYPE_ARG)
    }

    fun isInit(): Boolean {
        return !fromCode.isNullOrEmpty() && !toCode.isNullOrEmpty() && !date.isNullOrEmpty() &&
                !transportType.isNullOrEmpty()
    }

    fun getSchedule(): Flow<List<ScheduleSegment>> {
        return ServiceLocator.getScheduleService()
            .getSchedule(fromCode!!, toCode!!, date!!, transportType!!)
            .map { segments ->
                val defaultCurrency = getApplication<Application>()
                    .getSharedPreferences(TrainSchedulerConstants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
                    .getString(TrainSchedulerConstants.DEFAULT_CURRENCY_PREF, null)
                segments.mapNotNull { segment ->
                    val departureDateTime = LocalDateTime.parse(segment.getDeparture(),
                        DateTimeFormatter.ofPattern(ScheduleSegment.TARGET_DATE_FORMAT))
                    if (!departureDateTime.isBefore(LocalDateTime.now())) {
                        segment.tickets.forEach { ticket ->
                            val ticketCurrency = ticket.currency
                            if (ticketCurrency != defaultCurrency) {
                                val convertedPrice =
                                    ServiceLocator.getCurrencyService().convert(ticketCurrency!!,
                                        defaultCurrency!!, ticket.price!!).firstOrNull()
                                if (convertedPrice != null) {
                                    ticket.price = convertedPrice
                                    ticket.displayCurrency = defaultCurrency
                                }
                            }
                        }
                        segment
                    } else null
                }
            }
    }
}