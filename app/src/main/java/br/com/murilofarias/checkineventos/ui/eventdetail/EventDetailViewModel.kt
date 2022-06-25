package br.com.murilofarias.checkineventos.ui.eventdetail

import android.app.Application
import androidx.lifecycle.*
import br.com.murilofarias.checkineventos.data.model.CheckInInfo
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.data.network.EventApi
import br.com.murilofarias.checkineventos.ui.eventlist.EventApiStatus
import kotlinx.coroutines.launch

class EventDetailViewModel (event: Event,
                            app: Application
) : AndroidViewModel(app) {

    // The internal MutableLiveData for the selected property
    private val _selectedEvent = MutableLiveData<Event>()

    // The external LiveData for the SelectedProperty
    val selectedEvent: LiveData<Event>
        get() = _selectedEvent

    // Initialize the _selectedProperty MutableLiveData
    init {
        _selectedEvent.value = event
    }

    private val _checkInSuccess = MutableLiveData<Boolean>()


    val checkInSuccess: LiveData<Boolean>
        get() = _checkInSuccess

    fun onCheckIn(userName: String, userEmail: String, eventId: String){
        viewModelScope.launch {
            try {
                val checkInInfo = CheckInInfo(userName, userEmail, eventId)
                EventApi.retrofitService.doCheckIn(checkInInfo)
                _checkInSuccess.value = true
            } catch (e: Exception) {
                _checkInSuccess.value = false
            }
        }
    }

}