package br.com.murilofarias.checkineventos.ui.eventlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.data.network.EventApi
import kotlinx.coroutines.launch

class EventListViewModel(app: Application
) : AndroidViewModel(app) {

    //status of the most recent request
    private val _status = MutableLiveData<EventApiStatus>()


    val status: LiveData<EventApiStatus>
        get() = _status


    private val _events = MutableLiveData<List<Event>>()


    val events: LiveData<List<Event>>
        get() = _events

    // LiveData to handle navigation to the selected event
    private val _navigateToSelectedEvent= MutableLiveData<Event?>()
    val navigateToSelectedEvent: LiveData<Event?>
        get() = _navigateToSelectedEvent

    init {
        getEvents()
    }

    private fun getEvents() {
        viewModelScope.launch {
            _status.value = EventApiStatus.LOADING
            try {
                _events.value = EventApi.retrofitService.getEvents()
                _status.value = EventApiStatus.DONE
            } catch (e: Exception) {
                _status.value = EventApiStatus.ERROR
                _events.value = ArrayList()
            }
        }
    }


    fun displayEventDetails(event: Event) {
        _navigateToSelectedEvent.value = event
    }

    fun displayEventDetailsComplete() {
        _navigateToSelectedEvent.value = null
    }

}
