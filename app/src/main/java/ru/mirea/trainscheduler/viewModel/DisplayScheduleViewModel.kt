package ru.mirea.trainscheduler.viewModel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mirea.trainscheduler.ServiceLocator
import ru.mirea.trainscheduler.config.TrainSchedulerSettings
import ru.mirea.trainscheduler.model.ScheduleSegment

class DisplayScheduleViewModel : ViewModel() {
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
        return ServiceLocator.getScheduleService().getSchedule(fromCode!!, toCode!!, date!!, transportType!!)
            .map { segments ->
                segments.forEach { segment ->
                    segment.tickets.forEach { ticket ->
                        val ticketCurrency = ticket.currency
                        val defaultCurrency = TrainSchedulerSettings.defaultCurrency.code
                        if (ticketCurrency != defaultCurrency) {
                            ServiceLocator.getCurrencyConverter().convert(ticketCurrency!!,
                                defaultCurrency!!, ticket.price!!).collect { convertedPrice ->
                                ticket.price = convertedPrice
                                ticket.displayCurrency = defaultCurrency
                            }
                        }
                    }
                }
                segments
            }
    }
}