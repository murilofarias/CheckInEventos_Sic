package br.com.murilofarias.checkineventos.data.source.remote

import br.com.murilofarias.checkineventos.data.model.CheckInInfo
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.data.source.remote.RemoteSource

class FakeRemoteSource: RemoteSource {

    val events: List<Event> = listOf(
            Event(
                "1",
        1534784400000,
        "Descricao do evento",
        "http://lproweb.procempa.com.br/pmpa/prefpoa/seda_news/usu_img/Papel%20de%20Parede.png",
        -30.0392981,
        -51.2146267,
        "Feira de adoção de animais na Redenção",
        29.99
        ),
        Event(
            "2",
            1534784400000,
            "Descricao do evento 2",
            "http://lproweb.procempa.com.br/pmpa/prefpoa/seda_news/usu_img/Papel%20de%20Parede.png",
            -30.0392981,
            -51.2146267,
            "Feira de adoção de animais no brecho",
            19.99
        ),
        Event(
            "3",
            1534784400000,
            "Descricao do evento 3",
            "http://lproweb.procempa.com.br/pmpa/prefpoa/seda_news/usu_img/Papel%20de%20Parede.png",
            -30.0392981,
            -51.2146267,
            "Feira de adoção de animais no quintal",
            18.99
        )
    )

    override suspend fun getEvents(): List<Event> {
        return events
    }

    override suspend fun getEvent(id: String): Event {
        return events.first{event -> event.id == id}
    }

    override suspend fun doCheckIn(checkInInfo: CheckInInfo) {
        TODO("Not yet implemented")
    }
}