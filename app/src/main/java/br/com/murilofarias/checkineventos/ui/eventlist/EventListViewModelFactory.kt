package br.com.murilofarias.checkineventos.ui.eventlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.murilofarias.checkineventos.data.source.local.LocalSource
import br.com.murilofarias.checkineventos.data.source.remote.RemoteSource

class EventListViewModelFactory(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventListViewModel::class.java)) {
            return EventListViewModel(remoteSource, localSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}