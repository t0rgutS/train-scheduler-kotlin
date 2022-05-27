package ru.mirea.trainscheduler.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.trainscheduler.databinding.TicketListItemBinding
import ru.mirea.trainscheduler.model.Ticket

class TicketAdapter(private val tickets: List<Ticket>) :
    RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {
    companion object {
        private const val DEFAULT_TICKET_NAME = "Стандартный"
    }

    class TicketViewHolder(val binding: TicketListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = TicketListItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return TicketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val item: Ticket = tickets[position]
        if (item.name.isNullOrEmpty())
            holder.binding.ticketName.setText(DEFAULT_TICKET_NAME)
        else
            holder.binding.ticketName.setText(item.name)
        holder.binding.price.setText("${String.format("%.2f", item.price)} ${item.getCurrencyAsString()}")
        holder.binding.etAvailable.isChecked = item.canBeElectronic == true
    }

    override fun getItemCount(): Int {
        return tickets.size
    }
}