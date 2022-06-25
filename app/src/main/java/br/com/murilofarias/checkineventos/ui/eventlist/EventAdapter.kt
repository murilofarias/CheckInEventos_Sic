package br.com.murilofarias.checkineventos.ui.eventlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.databinding.ListItemEventBinding

class EventAdapter( private val onClickListener: OnClickListener) :
    ListAdapter<Event,
            EventAdapter.EventViewHolder>(DiffCallback) {


    class EventViewHolder(private var binding: ListItemEventBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.event = event
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): EventViewHolder {
        return EventViewHolder(ListItemEventBinding.inflate(LayoutInflater.from(parent.context)))
    }


    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(event)
        }
        holder.bind(event)
    }

    class OnClickListener(val clickListener: (event:Event) -> Unit) {
        fun onClick(event:Event) = clickListener(event)
    }
}