package br.com.murilofarias.checkineventos.data.source.remote

import br.com.murilofarias.checkineventos.data.model.CheckInInfo
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.data.source.remote.RemoteSource

class FakeRemoteSourceAndroid(): RemoteSource {

    private val events : MutableList<Event> = mutableListOf()

    override suspend fun getEvents(): List<Event> {
        return events
    }

    override suspend fun getEvent(id: String): Event {
        return events.first{event -> event.id == id}
    }

    override suspend fun uploadEvents(eventsToLoad: List<Event>) {
        events.addAll(eventsToLoad)
    }

    override suspend fun doCheckIn(checkInInfo: CheckInInfo) {
        TODO("Not yet implemented")
    }
}