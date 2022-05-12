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
import androidx.navigation.fragment.findNavController
import ru.mirea.trainscheduler.databinding.SettingsFragmentBinding
import ru.mirea.trainscheduler.issue.TrainSchedulerException
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
        binding.currency.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_list_item_1,
            viewModel.currencyList)
        binding.apply.setOnClickListener {
            try {
                viewModel.applySettings(binding.currency.selectedItem as String)
                findNavController().popBackStack()
            } catch (e: TrainSchedulerException) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
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