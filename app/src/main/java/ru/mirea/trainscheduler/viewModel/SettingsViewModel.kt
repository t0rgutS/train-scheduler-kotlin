package ru.mirea.trainscheduler.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.mirea.trainscheduler.ServiceLocator
import ru.mirea.trainscheduler.model.Currency

class SettingsViewModel : ViewModel() {
    companion object {
        const val TAG = "Settings"
    }

    fun getCurrencies(): Flow<List<Currency>> {
        return ServiceLocator.getCurrencyService().getCurrencies()
    }

    fun getLocationCount(): Flow<Long> {
        return ServiceLocator.getScheduleService().getLocationCount()
    }

    fun getCurrencyCount(): Flow<Long> {
        return ServiceLocator.getCurrencyService().getCurrencyCount()
    }

    fun resyncCurrencies() {
        viewModelScope.launch(Dispatchers.IO) {
            ServiceLocator.getCurrencyService().updateCurrencyList()
        }
        Log.i(TAG, "Запущена ресихнронизация валют")
    }

    fun resyncLocations() {
        viewModelScope.launch(Dispatchers.IO) { ServiceLocator.getScheduleService().updateLocationList() }
        Log.i(TAG, "Запущена ресинхронизация локаций")
    }

    fun getExchangeCount(): Flow<Long> {
        return ServiceLocator.getCurrencyService().getExchangeCount()
    }

    fun clearExchanges() {
        ServiceLocator.getCurrencyService().clearExchanges()
        Log.i(TAG, "Запущена очистка списка конвертаций валют")
    }


}