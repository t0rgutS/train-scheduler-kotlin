package ru.mirea.trainscheduler.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.mirea.trainscheduler.TrainSchedulerConstants
import ru.mirea.trainscheduler.databinding.SettingsFragmentBinding
import ru.mirea.trainscheduler.issue.TrainSchedulerException
import ru.mirea.trainscheduler.model.Currency
import ru.mirea.trainscheduler.viewModel.SettingsViewModel
import java.lang.Exception

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
            try {
                viewModel.getCurrencies().collect { currencies ->
                    requireActivity().runOnUiThread {
                        try {
                            binding.currency.adapter = ArrayAdapter<Currency>(requireContext(),
                                android.R.layout.simple_spinner_item,
                                currencies)
                            val defaultCurrency = currencies.find {
                                it.code == requireActivity().getSharedPreferences(
                                    TrainSchedulerConstants.SHARED_PREF_NAME,
                                    Context.MODE_PRIVATE)
                                    .getString(TrainSchedulerConstants.DEFAULT_CURRENCY_PREF, null)
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
                                                requireActivity()
                                                    .getSharedPreferences(TrainSchedulerConstants.SHARED_PREF_NAME,
                                                        Context.MODE_PRIVATE).edit()
                                                    .putString(TrainSchedulerConstants.DEFAULT_CURRENCY_PREF,
                                                        it1).apply()
                                            }
                                        } catch (e: Exception) {
                                            showErrorDialog(e)
                                        }
                                    }

                                    override fun onNothingSelected(p0: AdapterView<*>?) {
                                    }
                                }
                        } catch (e: Exception) {
                            showErrorDialog(e)
                        }
                    }
                }

            } catch (e: Exception) {
                activity?.runOnUiThread { showErrorDialog(e) }
            }
        }

        lifecycleScope.launch {
            viewModel.getLocationCount().collect { locationCount ->
                binding.locationCount.setText(locationCount.toString())
                binding.resyncLocations.setOnClickListener {
                    viewModel.resyncLocations()
                    Toast.makeText(requireContext(), "Ресинхронизация локаций запущена",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.getCurrencyCount().collect { currencyCount ->
                binding.currencyCount.setText(currencyCount.toString())
                binding.resyncCurrencies.setOnClickListener {
                    viewModel.resyncCurrencies()
                    Toast.makeText(requireContext(), "Ресинхронизация валют запущена",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.getExchangeCount().collect { exchangeCount ->
                binding.exchangeCount.setText(exchangeCount.toString())
                binding.clearExchanges.setOnClickListener {
                    viewModel.clearExchanges()
                    Toast.makeText(requireContext(), "Очистка курсов запущена",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showErrorDialog(t: Throwable) {
        context?.let { AlertDialog.Builder(it)
            .setTitle("Ошибка")
            .setMessage("Произошла ошибка: ${t.message}")
            .setPositiveButton("OK") { dialog, id -> dialog.cancel() }.show() }
    }

}