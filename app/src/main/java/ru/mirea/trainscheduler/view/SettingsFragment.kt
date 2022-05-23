package ru.mirea.trainscheduler.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.mirea.trainscheduler.databinding.SettingsFragmentBinding
import ru.mirea.trainscheduler.issue.TrainSchedulerException
import ru.mirea.trainscheduler.model.Currency
import ru.mirea.trainscheduler.model.Profile
import ru.mirea.trainscheduler.service.ProfileDataService
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
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getCurrencies().collect { currencies ->
                viewModel.getProfile(ProfileDataService.DEFAULT_CURRENCY_CODE)
                    .collect { defaultCurrencyProfile ->
                        requireActivity().runOnUiThread {
                            binding.currency.adapter = ArrayAdapter<Currency>(requireContext(),
                                android.R.layout.simple_spinner_item,
                                currencies)
                            val defaultCurrency = currencies.find {
                                it.code == defaultCurrencyProfile?.value
                            }
                            binding.currency.setSelection(if (defaultCurrency != null) currencies.indexOf(
                                defaultCurrency) else 0)
                            binding.currency.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        p0: AdapterView<*>?,
                                        p1: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        try {
                                            currencies[position].code?.let { it1 ->
                                                if (it1 != defaultCurrency?.code)
                                                    viewModel.saveProfile(ProfileDataService.DEFAULT_CURRENCY_CODE,
                                                        it1)
                                            }
                                        } catch (e: TrainSchedulerException) {
                                            Toast.makeText(requireContext(),
                                                e.message,
                                                Toast.LENGTH_LONG)
                                                .show()
                                        }
                                    }

                                    override fun onNothingSelected(p0: AdapterView<*>?) {
                                    }
                                }
                        }
                    }
            }

            viewModel.getProfile(ProfileDataService.THEME_CODE).collect { profile ->
                requireActivity().runOnUiThread {
                    binding.darkMode.isChecked =
                        profile?.value == Profile.ThemeProfileValues.DARK.name
                    binding.darkMode.setOnCheckedChangeListener { button, isChecked ->
                        if (isChecked)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        else
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        (requireActivity() as AppCompatActivity).delegate.applyDayNight()
                        viewModel.saveProfile(ProfileDataService.THEME_CODE, if (isChecked)
                            Profile.ThemeProfileValues.DARK.name
                        else Profile.ThemeProfileValues.DEFAULT.name)
                    }
                }
            }
        }
        binding.locationCount.setText(viewModel.getLocationCount().toString())
        binding.resyncLocations.setOnClickListener {
            viewModel.resyncLocations()
        }
        binding.exchangeCount.setText(viewModel.getExchangeCount().toString())
        binding.clearExchanges.setOnClickListener {
            viewModel.clearExchanges()
        }
    }

}