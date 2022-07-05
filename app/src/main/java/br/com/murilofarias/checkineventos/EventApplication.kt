package br.com.murilofarias.checkineventos

import android.app.Application
import br.com.murilofarias.checkineventos.data.source.local.LocalSource
import br.com.murilofarias.checkineventos.data.source.remote.RemoteSource

class EventApplication: Application() {

    val localSource: LocalSource
        get() = ServiceLocator.provideLocalSource(this)

    val remoteSource: RemoteSource
        get() = ServiceLocator.provideRemoteSource(this)
}