package ru.mirea.trainscheduler.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.mirea.trainscheduler.R
import ru.mirea.trainscheduler.databinding.DisplayScheduleFragmentBinding
import ru.mirea.trainscheduler.view.adapter.ScheduleAdapter
import ru.mirea.trainscheduler.viewModel.DisplayScheduleViewModel
import java.lang.Exception

class DisplayScheduleFragment : Fragment() {
    private lateinit var binding: DisplayScheduleFragmentBinding
    private lateinit var viewModel: DisplayScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DisplayScheduleFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.to_settings)
            findNavController()
                .navigate(R.id.action_displayScheduleFragment_to_settingsFragment)
        return false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DisplayScheduleViewModel::class.java)
        binding.schedule.layoutManager = LinearLayoutManager(requireContext())
        if (!viewModel.isInit()) {
            if (arguments == null)
                findNavController().popBackStack()
            else
                viewModel.init(requireArguments())
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                viewModel.getSchedule().collect { scheduleList ->
                    if (scheduleList.isNotEmpty()) {
                        scheduleList.sortedBy { it.getDeparture() }
                        requireActivity().runOnUiThread {
                            binding.schedule.adapter = ScheduleAdapter(scheduleList)
                        }
                    } else {
                        requireActivity().runOnUiThread {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Уведомление")
                                .setMessage("Актуальные рейсы не найдены")
                                .setPositiveButton("OK") { dialog, id -> dialog.cancel() }.show()
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    showErrorDialog(e)
                }
            }
        }
    }

    private fun showErrorDialog(t: Throwable) {
        AlertDialog.Builder(requireContext())
            .setTitle("Ошибка")
            .setMessage("Произошла ошибка: ${t.message}")
            .setPositiveButton("OK") { dialog, id -> dialog.cancel() }.show()
    }

}