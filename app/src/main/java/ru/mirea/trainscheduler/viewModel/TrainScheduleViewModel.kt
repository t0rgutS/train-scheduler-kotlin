package ru.mirea.trainscheduler.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.mirea.trainscheduler.ServiceLocator
import ru.mirea.trainscheduler.model.Location

class TrainScheduleViewModel : ViewModel() {
    private val suggestedFrom: MutableStateFlow<List<Location>> = MutableStateFlow(emptyList())
    private val suggestedTo: MutableStateFlow<List<Location>> = MutableStateFlow(emptyList())

    fun getSuggestedLocations(from: Boolean): Flow<List<Location>> {
        return if (from) suggestedFrom else suggestedTo
    }

    suspend fun suggestLocations(suggestBy: String, from: Boolean) {
        ServiceLocator.getScheduleService().suggestLocations(suggestBy).collect {
            if (from)
                suggestedFrom.value = it
            else suggestedTo.value = it
        }
    }

    fun findLocation(searchBy: String): Location? {
        return ServiceLocator.getScheduleService().findLocation(searchBy)
    }
}