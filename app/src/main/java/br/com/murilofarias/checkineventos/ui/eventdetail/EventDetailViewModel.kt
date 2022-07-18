package br.com.murilofarias.checkineventos.ui.eventdetail

import androidx.lifecycle.*
import br.com.murilofarias.checkineventos.data.model.CheckInInfo
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.data.source.local.LocalSource
import br.com.murilofarias.checkineventos.data.source.remote.RemoteSource
import kotlinx.coroutines.launch


class EventDetailViewModel (event: Event,
                            private val remoteSource: RemoteSource,
                            private val localSource: LocalSource,
) : ViewModel() {

    // The internal MutableLiveData for the selected property
    private val _selectedEvent = MutableLiveData<Event>()

    // The external LiveData for the SelectedProperty
    val selectedEvent: LiveData<Event>
        get() = _selectedEvent

    private val _checkInSuccess = MutableLiveData<Boolean>()


    val checkInSuccess: LiveData<Boolean>
        get() = _checkInSuccess

    // The internal MutableLiveData for the selected property
    private val _isCheckedIn = MutableLiveData<Boolean>()

    // The external LiveData for the SelectedProperty
    val isCheckedIn: LiveData<Boolean>
        get() = _isCheckedIn


    // Initialize the _selectedProperty MutableLiveData
    init {
        _selectedEvent.value = event
        verifyCheckIn()
    }

    fun verifyCheckIn(){
        viewModelScope.launch {
            try {
                val userChecks = localSource.getCheckIns()
                val userChecksMutable = userChecks.split(";").toMutableList()

                _selectedEvent.value?.let{ selectedEvent ->
                    _isCheckedIn.value = userChecksMutable.contains(selectedEvent.id)
                }
            }catch (e: Exception) {
                _isCheckedIn.value = false
            }
        }
    }

    fun onCheckIn(eventId: String){
        viewModelScope.launch {

            val user = localSource.getUser()
            val userName = user.name
            val userEmail = user.email

            try {
                val checkInInfo = CheckInInfo(userName, userEmail, eventId)
                remoteSource.doCheckIn(checkInInfo)
                markCheckInAsDone()
                _checkInSuccess.value = true
            } catch (e: Exception) {
                _checkInSuccess.value = false
            }
        }
    }

    fun markCheckInAsDone(){
        viewModelScope.launch {
            _selectedEvent.value?.let { selectedEvent ->
                localSource.saveCheckIn(selectedEvent.id)
                _isCheckedIn.value = true
            }

        }
    }

}