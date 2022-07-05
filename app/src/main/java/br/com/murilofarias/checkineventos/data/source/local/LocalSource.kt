package br.com.murilofarias.checkineventos.data.source.local

import br.com.murilofarias.checkineventos.data.model.User

interface LocalSource {

    fun getUser(): User

    fun saveUser(user: User)

    fun getCheckIns() : String

    fun saveCheckIn(eventId: String)

    fun reset()
}