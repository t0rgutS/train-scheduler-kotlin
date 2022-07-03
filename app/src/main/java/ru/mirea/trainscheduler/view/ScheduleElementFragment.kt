package ru.mirea.trainscheduler.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.mirea.trainscheduler.R
import ru.mirea.trainscheduler.ServiceLocator
import ru.mirea.trainscheduler.databinding.ScheduleElementFragmentBinding
import ru.mirea.trainscheduler.model.ScheduleSegment
import ru.mirea.trainscheduler.view.adapter.FollowStationAdapter
import ru.mirea.trainscheduler.view.adapter.TicketAdapter
import ru.mirea.trainscheduler.viewModel.ScheduleElementViewModel
import java.lang.Exception

class ScheduleElementFragment : Fragment() {
    companion object {
        const val BUNDLE_SCHEDULE_SEGMENT = "scheduleSegment"
    }

    private lateinit var viewModel: ScheduleElementViewModel
    private lateinit var binding: ScheduleElementFragmentBinding
    private var ticketAdapter: TicketAdapter? = null
    private var stationAdapter: FollowStationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ScheduleElementFragmentBinding.inflate(inflater, container, false)
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
                .navigate(R.id.action_scheduleElementFragment_to_settingsFragment)
        return false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ScheduleElementViewModel::class.java]
        if (viewModel.getScheduleSegment() == null) {
            val scheduleSegmentJson: String? = arguments?.get(BUNDLE_SCHEDULE_SEGMENT) as String?
            if (scheduleSegmentJson == null)
                findNavController().popBackStack()
            else
                viewModel.setScheduleSegment(ServiceLocator.getGson().fromJson(scheduleSegmentJson,
                    ScheduleSegment::class.java))
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val scheduleSegment = viewModel.getScheduleSegment()!!
        binding.from.setText(scheduleSegment.from?.getFullName())
        binding.to.setText(scheduleSegment.to?.getFullName())
        binding.date.setText(scheduleSegment.getDeparture())
        binding.travelTime.setText(parseTravelTime(scheduleSegment.getTravelTimeInSeconds()))
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                viewModel.getTickets().collect { tickets ->
                    requireActivity().runOnUiThread {
                        ticketAdapter = TicketAdapter(tickets)
                        binding.recyclerView.adapter = ticketAdapter
                    }
                }
            } catch (e: Exception) {
                activity?.runOnUiThread { showErrorDialog(e) }
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                viewModel.getFollowStations().collect { stations ->
                    requireActivity().runOnUiThread {
                        stationAdapter = FollowStationAdapter(stations)
                    }
                }
            } catch (e: Exception) {
                activity?.runOnUiThread { showErrorDialog(e) }
            }
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> binding.recyclerView.adapter = ticketAdapter
                    1 -> binding.recyclerView.adapter = stationAdapter
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun parseTravelTime(travelTimeInSeconds: Long): String {
        val minutes = travelTimeInSeconds / 60
        val hours = minutes / 60
        val days = hours / 24
        return (if (days > 0) "$days дней " else "" + if (hours > 0) "$hours часов " else "" +
                if (minutes > 0) "$minutes минут" else "").trim()
    }

    private fun showErrorDialog(t: Throwable) {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle("Ошибка")
                .setMessage("Произошла ошибка: ${t.message}")
                .setPositiveButton("OK") { dialog, id -> dialog.cancel() }.show()
        }
    }

}