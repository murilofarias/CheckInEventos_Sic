package br.com.murilofarias.checkineventos.data.source.local

import br.com.murilofarias.checkineventos.data.model.User

class FakeLocalSource: LocalSource {

    private var userSaved : User =User("", "")
    private var checkIns : String = ""

    override fun getUser(): User {
        return userSaved
    }

    override fun saveUser(user: User) {
        userSaved = user
    }

    override fun getCheckIns(): String {
        return checkIns
    }

    override fun saveCheckIn(eventId: String) {
        checkIns = "$checkIns;${eventId}"
    }
}