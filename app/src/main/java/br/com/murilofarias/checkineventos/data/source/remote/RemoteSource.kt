package br.com.murilofarias.checkineventos.data.source.remote

import br.com.murilofarias.checkineventos.data.model.CheckInInfo
import br.com.murilofarias.checkineventos.data.model.Event

interface RemoteSource {

    suspend fun getEvents() :List<Event>

    suspend fun doCheckIn(checkInInfo: CheckInInfo)

    suspend fun getEvent(id: String): Event

    suspend fun uploadEvents(events : List<Event>)
}