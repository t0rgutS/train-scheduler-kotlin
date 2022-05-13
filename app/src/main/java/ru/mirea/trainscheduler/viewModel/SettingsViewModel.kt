package ru.mirea.trainscheduler.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.mirea.trainscheduler.ServiceLocator
import ru.mirea.trainscheduler.config.TrainSchedulerSettings
import ru.mirea.trainscheduler.issue.UnknownCurrencyException
import ru.mirea.trainscheduler.model.Currency

class SettingsViewModel : ViewModel() {
    fun getCurrencies(): Flow<List<Currency>> {
        return ServiceLocator.getCurrencyService().getCurrencies()
    }

    fun applySettings(currency: Currency) {
        TrainSchedulerSettings.defaultCurrency = currency
    }
}