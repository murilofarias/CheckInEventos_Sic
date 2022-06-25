package br.com.murilofarias.checkineventos.ui.eventdetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.murilofarias.checkineventos.data.model.Event

class EventDetailViewModelFactory(
    private val event: Event,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            return EventDetailViewModel(event, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}