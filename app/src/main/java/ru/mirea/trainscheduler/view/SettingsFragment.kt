package ru.mirea.trainscheduler.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.mirea.trainscheduler.config.TrainSchedulerSettings
import ru.mirea.trainscheduler.databinding.SettingsFragmentBinding
import ru.mirea.trainscheduler.issue.TrainSchedulerException
import ru.mirea.trainscheduler.model.Currency
import ru.mirea.trainscheduler.viewModel.SettingsViewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: SettingsFragmentBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        lifecycleScope.launch {
            viewModel.getCurrencies().collect { currencies ->
                binding.currency.adapter = ArrayAdapter<Currency>(requireContext(),
                    android.R.layout.simple_spinner_item,
                    currencies)
                var defaultCurrencyPos = currencies.indexOf(TrainSchedulerSettings.defaultCurrency)
                if (defaultCurrencyPos == -1)
                    defaultCurrencyPos = currencies.indexOfFirst {
                        it.code == TrainSchedulerSettings.defaultCurrency.code
                    }
                binding.currency.setSelection(if (defaultCurrencyPos > -1) defaultCurrencyPos else 0)
                binding.apply.setOnClickListener {
                    try {
                        viewModel.applySettings(currencies[binding.currency.selectedItemPosition])
                        findNavController().popBackStack()
                    } catch (e: TrainSchedulerException) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        binding.darkMode.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            (requireActivity() as AppCompatActivity).delegate.applyDayNight()
        }
    }

}