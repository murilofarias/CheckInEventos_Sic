package br.com.murilofarias.checkineventos.ui.eventlist

import android.app.Application
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.data.source.local.LocalSource
import br.com.murilofarias.checkineventos.data.source.remote.EventApi
import br.com.murilofarias.checkineventos.data.source.remote.RemoteSource
import br.com.murilofarias.checkineventos.util.isUserInfoValid
import kotlinx.coroutines.launch

class EventListViewModel(private val remoteSource: RemoteSource,
                         private val localSource: LocalSource,
) : ViewModel() {

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


    private val _navigateToUserInfo= MutableLiveData(false)
    val navigateToUserInfo: LiveData<Boolean>
        get() = _navigateToUserInfo

    init {
        getEvents()
    }

    private fun getEvents() {
        viewModelScope.launch {
            _status.value = EventApiStatus.LOADING
            try {
                //_events.value = EventApi.retrofitService.getEvents()
                _events.value = remoteSource.getEvents()
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

    fun checkUserInfoSetup() {
        viewModelScope.launch {

            val user = localSource.getUser()
            if (!isUserInfoValid(user.name, user.email))
                _navigateToUserInfo.value = true
            }
    }

    fun displayUserInfoComplete() {
        _navigateToUserInfo.value = false
    }

}
