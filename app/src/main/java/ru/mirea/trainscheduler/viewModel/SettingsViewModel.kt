package ru.mirea.trainscheduler.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import ru.mirea.trainscheduler.ServiceLocator
import ru.mirea.trainscheduler.model.Currency
import ru.mirea.trainscheduler.model.Profile

class SettingsViewModel : ViewModel() {
    companion object {
        const val TAG = "Settings"
    }

    fun getCurrencies(): Flow<List<Currency>> {
        return ServiceLocator.getCurrencyService().getCurrencies()
    }

    fun getProfile(code: String): Flow<Profile?> {
        return ServiceLocator.getProfileService().getProfileByCode(code)
    }

    fun saveProfile(code: String, value: String) {
        val profile = Profile()
        profile.code = code
        profile.value = value
        ServiceLocator.getProfileService().upsertProfile(profile)
    }

    fun getLocationCount(): Long {
        return ServiceLocator.getScheduleService().getLocationCount()
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