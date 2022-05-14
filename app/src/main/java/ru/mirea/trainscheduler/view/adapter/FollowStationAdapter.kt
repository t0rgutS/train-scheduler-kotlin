package ru.mirea.trainscheduler.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.trainscheduler.databinding.StationListItemBinding
import ru.mirea.trainscheduler.model.Station

class FollowStationAdapter(private val stations: List<Station>) :
    RecyclerView.Adapter<FollowStationAdapter.FollowStationViewHolder>() {
    class FollowStationViewHolder(val binding: StationListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowStationViewHolder {
        val binding = StationListItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return FollowStationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowStationViewHolder, position: Int) {
        val item: Station = stations[position]
        holder.binding.station.setText(item.title)
        if (item.getArrival().isNullOrEmpty())
            holder.binding.arrivalSection.isVisible = false
        else
            holder.binding.arrival.setText(item.getArrival())
        if (item.getDeparture().isNullOrEmpty())
            holder.binding.departureSection.isVisible = false
        else
            holder.binding.departure.setText(item.getDeparture())
    }

    override fun getItemCount(): Int {
        return stations.size
    }
}