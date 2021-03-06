package br.com.murilofarias.checkineventos.ui.eventdetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.data.source.local.LocalSource
import br.com.murilofarias.checkineventos.data.source.remote.RemoteSource

class EventDetailViewModelFactory(
    private val event: Event,
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            return EventDetailViewModel(event, remoteSource, localSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}