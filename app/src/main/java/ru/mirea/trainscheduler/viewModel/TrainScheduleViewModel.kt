package ru.mirea.trainscheduler.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mirea.trainscheduler.ServiceLocator
import ru.mirea.trainscheduler.config.TrainSchedulerSettings
import ru.mirea.trainscheduler.model.Location
import ru.mirea.trainscheduler.model.ScheduleSegment

class TrainScheduleViewModel : ViewModel() {

    fun suggestLocations(suggestBy: String): Flow<List<Location>> {
        return ServiceLocator.getScheduleService().suggestLocations(suggestBy)
    }

    fun findLocation(searchBy: String): Flow<Location?> {
        return ServiceLocator.getScheduleService().findLocation(searchBy)
    }

    /*   fun getSchedule(
           from: String,
           to: String,
           date: String,
           type: String,
       ): Flow<List<ScheduleSegment>> {
           return ServiceLocator.getScheduleService().getSchedule(from, to, date, type)
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
       }*/
}