package ru.mirea.trainscheduler.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import ru.mirea.trainscheduler.ServiceLocator
import ru.mirea.trainscheduler.model.Currency

class SettingsViewModel : ViewModel() {
    companion object {
        const val TAG = "Settings"
    }

    fun getCurrencies(): Flow<List<Currency>> {
        return ServiceLocator.getCurrencyService().getCurrencies()
    }

    fun getLocationCount(): Long {
        return ServiceLocator.getScheduleService().getLocationCount()
    }

    fun getCurrencyCount(): Long {
        return ServiceLocator.getCurrencyService().getCurrencyCount()
    }

    fun resyncCurrencies() {
        ServiceLocator.getCurrencyService().updateCurrencyList()
        Log.i(TAG, "Запущена ресихнронизация валют")
    }

    fun resyncLocations() {
        ServiceLocator.getScheduleService().updateLocationList()
        Log.i(TAG, "Запущена ресинхронизация локаций")
    }

    fun getExchangeCount(): Long {
        return ServiceLocator.getCurrencyService().getExchangeCount()
    }

    fun clearExchanges() {
        ServiceLocator.getCurrencyService().clearExchanges()
        Log.i(TAG, "Запущена очистка списка конвертаций валют")
    }


}