package ru.mirea.trainscheduler.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import ru.mirea.trainscheduler.ServiceLocator
import ru.mirea.trainscheduler.model.ScheduleSegment
import ru.mirea.trainscheduler.model.Station
import ru.mirea.trainscheduler.model.Ticket
import ru.mirea.trainscheduler.service.ProfileService

class ScheduleElementViewModel : ViewModel() {
    private var scheduleSegment: ScheduleSegment? = null
    private lateinit var initialCurrencies: List<String>

    fun setScheduleSegment(scheduleSegment: ScheduleSegment) {
        this.scheduleSegment = scheduleSegment
        this.initialCurrencies = scheduleSegment.tickets.map(Ticket::currency)
            .toList().filterNotNull().distinct()
    }

    fun getScheduleSegment(): ScheduleSegment? {
        return scheduleSegment
    }

    fun getTickets(): Flow<List<Ticket>> {
        return flow {
            val defaultCurrency = ServiceLocator.getProfileService()
                .getProfileByCode(ProfileService.DEFAULT_CURRENCY_CODE).firstOrNull()?.value
            val tickets = scheduleSegment!!.tickets
            tickets.forEach { ticket ->
                if (ticket.displayCurrency != defaultCurrency) {
                    viewModelScope.launch {
                        ServiceLocator.getCurrencyService()
                            .convert(ticket.currency!!, defaultCurrency!!,
                                ticket.price!!).collect { convertedPrice ->
                                ticket.displayCurrency = defaultCurrency
                                ticket.price = convertedPrice
                            }
                    }
                }
            }
            emit(tickets)
        }
    }

    fun getFollowStations(): Flow<List<Station>> {
        return if (scheduleSegment?.threadUID != null)
            ServiceLocator.getScheduleService().getFollowStations(scheduleSegment?.threadUID!!,
                scheduleSegment?.from?.getDefaultCode(), scheduleSegment?.to?.getDefaultCode())
        else emptyFlow()
    }
}