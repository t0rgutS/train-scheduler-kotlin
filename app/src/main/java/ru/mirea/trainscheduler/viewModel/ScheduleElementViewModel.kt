package ru.mirea.trainscheduler.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import ru.mirea.trainscheduler.ServiceLocator
import ru.mirea.trainscheduler.TrainSchedulerConstants
import ru.mirea.trainscheduler.model.ScheduleSegment
import ru.mirea.trainscheduler.model.Station
import ru.mirea.trainscheduler.model.Ticket

class ScheduleElementViewModel(application: Application) : AndroidViewModel(application) {
    private var scheduleSegment: ScheduleSegment? = null
    private val ticketsFlow: MutableStateFlow<List<Ticket>> = MutableStateFlow(emptyList())
    private val followStationsFlow: MutableStateFlow<List<Station>> = MutableStateFlow(emptyList())

    fun setScheduleSegment(scheduleSegment: ScheduleSegment) {
        this.scheduleSegment = scheduleSegment
        viewModelScope.launch(Dispatchers.IO) {
            val defaultCurrency = getApplication<Application>()
                .getSharedPreferences(TrainSchedulerConstants.SHARED_PREF_NAME,
                    Context.MODE_PRIVATE)
                .getString(TrainSchedulerConstants.DEFAULT_CURRENCY_PREF, null)
            val tickets = scheduleSegment.tickets
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
            ticketsFlow.value = tickets
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (scheduleSegment.threadUID != null)
                ServiceLocator.getScheduleService().getFollowStations(scheduleSegment.threadUID!!,
                    scheduleSegment.from?.getDefaultCode(), scheduleSegment.to?.getDefaultCode())
                    .collect {
                        followStationsFlow.value = it
                    }
        }
    }

    fun getScheduleSegment(): ScheduleSegment? {
        return scheduleSegment
    }

    fun getTickets(): Flow<List<Ticket>> {
        return ticketsFlow
    }

    fun getFollowStations(): Flow<List<Station>> {
        return followStationsFlow
    }
}