package ru.mirea.trainscheduler.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mirea.trainscheduler.ServiceLocator
import ru.mirea.trainscheduler.config.TrainSchedulerSettings
import ru.mirea.trainscheduler.issue.UnknownCurrencyException
import ru.mirea.trainscheduler.model.Currency

class SettingsViewModel : ViewModel() {
    var currencyList: MutableList<Currency> = mutableListOf()

    init {
        viewModelScope.launch {
            ServiceLocator.getCurrencyRepository().getCurrencies().collect { currencies ->
                currencyList.addAll(currencies)
            }
        }
    }

    fun applySettings(currencyCode: String) {
        val currency = currencyList.stream().filter { it.code.equals(currencyCode) }
            .findFirst().orElse(null) ?: throw UnknownCurrencyException(currencyCode)
        TrainSchedulerSettings.defaultCurrency = currency
    }
}