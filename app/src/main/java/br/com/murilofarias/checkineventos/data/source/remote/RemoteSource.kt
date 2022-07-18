package br.com.murilofarias.checkineventos.data.source.remote

import androidx.lifecycle.LiveData
import br.com.murilofarias.checkineventos.data.model.CheckInInfo
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.ui.eventlist.EventApiStatus

interface RemoteSource {

    suspend fun getEvents(): List<Event>

    suspend fun doCheckIn(checkInInfo: CheckInInfo)

    suspend fun getEvent(id: String): Event

    suspend fun uploadEvents(events : List<Event>)
}