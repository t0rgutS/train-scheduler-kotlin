package ru.mirea.trainscheduler.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.mirea.trainscheduler.R
import ru.mirea.trainscheduler.databinding.TrainScheduleFragmentBinding
import ru.mirea.trainscheduler.model.Location
import ru.mirea.trainscheduler.view.adapter.LocationAutoCompleteAdapter
import ru.mirea.trainscheduler.view.adapter.ScheduleAdapter
import ru.mirea.trainscheduler.viewModel.DisplayScheduleViewModel
import ru.mirea.trainscheduler.viewModel.TrainScheduleViewModel

class TrainScheduleFragment : Fragment() {
    companion object {
        const val AUTO_COMPLETE_THRESHOLD = 3
    }

    private lateinit var viewModel: TrainScheduleViewModel
    private lateinit var binding: TrainScheduleFragmentBinding
    private var from: Location? = null
    private var to: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = TrainScheduleFragmentBinding.inflate(inflater, container, false)
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
                .navigate(R.id.action_trainScheduleFragment_to_settingsFragment)
        return false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[TrainScheduleViewModel::class.java]
        //binding.schedule.layoutManager = LinearLayoutManager(requireContext())
        binding.transportType.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (from != null && to != null && binding.fromDate.text.isNotEmpty())
                    getSchedule()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        binding.from.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.count() >= AUTO_COMPLETE_THRESHOLD) {
                    val searchBy: String = if (s[0].isUpperCase()) s.toString() else
                        s[0].uppercase() + s.substring(1)
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.findLocations(searchBy).collect { found ->
                            requireActivity().runOnUiThread {
                                val stationListAdapter = LocationAutoCompleteAdapter(
                                    requireContext(),
                                    found
                                )
                                binding.from.setAdapter(stationListAdapter)
                                stationListAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        })

        binding.to.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.count() >= AUTO_COMPLETE_THRESHOLD) {
                    val searchBy: String = if (s[0].isUpperCase()) s.toString() else
                        s[0].uppercase() + s.substring(1)
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.findLocations(searchBy).collect { found ->
                            requireActivity().runOnUiThread {
                                val stationListAdapter = LocationAutoCompleteAdapter(
                                    requireContext(),
                                    found
                                )
                                binding.to.setAdapter(stationListAdapter)
                                stationListAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        })

        binding.from.setOnItemClickListener { adapterView, view, position, id ->
            from = adapterView?.getItemAtPosition(position) as Location
        }
        binding.to.setOnItemClickListener { adapterView, view, position, id ->
            to = adapterView?.getItemAtPosition(position) as Location
        }

        binding.fromDate.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                val datePickerDialog = DatePickerDialog(requireContext())
                datePickerDialog.setOnDateSetListener { datePicker, year, month, day ->
                    binding.fromDate.setText("${year}" +
                            "-${if (month < 9) "0${month + 1}" else (month + 1)}" +
                            "-${if (day < 10) "0${day}" else day}")
                }
                datePickerDialog.show()
            }
            true
        }

        binding.find.setOnClickListener { getSchedule() }
    }

    private fun defineTransportType(tabPosition: Int): String {
        when (tabPosition) {
            0 -> return "train"
            1 -> return "suburban"
            2 -> return "bus"
        }
        return ""
    }

    private fun getSchedule() {
        var fromCode = from?.getDefaultCode()
        var toCode = to?.getDefaultCode()
        if (fromCode == null) {
            Toast.makeText(requireContext(), "Выберите точку отправления!", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (toCode == null) {
            Toast.makeText(requireContext(), "Выберите точку прибытия!", Toast.LENGTH_LONG).show()
            return
        }
        if (binding.fromDate.text.isEmpty()) {
            Toast.makeText(
                requireContext(), "Выберите дату!",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        val selectedTabPos = binding.transportType.selectedTabPosition
        findNavController().navigate(R.id.action_trainScheduleFragment_to_displayScheduleFragment,
            Bundle().also {
                it.putString(DisplayScheduleViewModel.FROM_CODE_ARG, fromCode)
                it.putString(DisplayScheduleViewModel.TO_CODE_ARG, toCode)
                it.putString(DisplayScheduleViewModel.DATE_ARG, binding.fromDate.text.toString())
                it.putString(DisplayScheduleViewModel.TRANSPORT_TYPE_ARG,
                    defineTransportType(selectedTabPos))
            })
        /* viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
             val selectedTabPos = binding.transportType.selectedTabPosition
             viewModel.getSchedule(
                 fromCode,
                 toCode,
                 binding.fromDate.text.toString(),
                 defineTransportType(selectedTabPos)
             ).collect { scheduleList ->
                 scheduleList.sortedBy { it.getDepartureTimeAsString() }
                 requireActivity().runOnUiThread {
                     binding.schedule.adapter = ScheduleAdapter(scheduleList)
                 }
             }
         }*/
    }
}