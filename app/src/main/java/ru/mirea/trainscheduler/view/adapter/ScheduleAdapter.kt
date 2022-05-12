package ru.mirea.trainscheduler.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.trainscheduler.R
import ru.mirea.trainscheduler.ServiceLocator
import ru.mirea.trainscheduler.databinding.ScheduleElementItemBinding
import ru.mirea.trainscheduler.model.ScheduleSegment
import ru.mirea.trainscheduler.model.Ticket
import ru.mirea.trainscheduler.view.ScheduleElementFragment

class ScheduleAdapter(private val scheduleList: List<ScheduleSegment>) :
    RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {
    class ScheduleViewHolder(val binding: ScheduleElementItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ScheduleElementItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val item: ScheduleSegment = scheduleList[position]
        holder.binding.fromTo.setText("${item.from?.title} --> ${item.to?.title}")
        holder.binding.`when`.setText(item.getDeparture())
        holder.binding.travelTime.setText(parseTravelTime(item.getTravelTimeInSeconds()))
        holder.binding.price.setText(getPriceAsString(item.tickets))
        holder.binding.etAvailable.isChecked = item.canBuyElectronic()
        holder.binding.scheduleEl.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_displayScheduleFragment_to_scheduleElementFragment,
                    bundleOf(Pair(ScheduleElementFragment.BUNDLE_SCHEDULE_SEGMENT,
                        ServiceLocator.getGson().toJson(item))))
        }
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    private fun parseTravelTime(travelTimeInSeconds: Long): String {
        val minutes = travelTimeInSeconds / 60
        val hours = minutes / 60
        val days = hours / 24
        return (if (days > 0) "$days дней " else "" + if (hours > 0) "$hours часов " else "" +
                if (minutes > 0) "$minutes минут" else "").trim()
    }

    private fun getPriceAsString(tickets: List<Ticket>): String {

        val minPriceTicket = tickets.minByOrNull { it.price!! }
        val maxPriceTicket = tickets.maxByOrNull { it.price!! }
        if (minPriceTicket != null && maxPriceTicket != null) {
            return if (minPriceTicket.price == maxPriceTicket.price) {
                "${minPriceTicket.price} ${minPriceTicket.currency}"
            } else {
                "от ${minPriceTicket.price} ${minPriceTicket.currency}" +
                        "до ${maxPriceTicket.price} ${maxPriceTicket.currency}"
            }
        }
        return ""
    }
}