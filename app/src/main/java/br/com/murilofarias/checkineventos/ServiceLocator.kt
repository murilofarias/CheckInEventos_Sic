package br.com.murilofarias.checkineventos

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import br.com.murilofarias.checkineventos.data.source.local.LocalSource
import br.com.murilofarias.checkineventos.data.source.local.SharedPreferenceStorage
import br.com.murilofarias.checkineventos.data.source.remote.EventApi
import br.com.murilofarias.checkineventos.data.source.remote.EventApiService
import br.com.murilofarias.checkineventos.data.source.remote.RemoteSource
import kotlinx.coroutines.runBlocking

object ServiceLocator {

    private val lock = Any()
    @Volatile
    var localSource: LocalSource? = null
        @VisibleForTesting set

    @Volatile
    var remoteSource: RemoteSource? = null
        @VisibleForTesting set

    fun provideRemoteSource(context: Context): RemoteSource {
        synchronized(this) {
            return remoteSource ?: createRemoteSource(context)
        }
    }

    private fun createRemoteSource(context: Context): RemoteSource {
        val remoteSource = EventApi.retrofitService
        return remoteSource
    }

        fun provideLocalSource(context: Context): LocalSource {
        synchronized(this) {
            return localSource ?: createLocalSource(context)
        }
    }

    private fun createLocalSource(context: Context): LocalSource {
        val newlocalSource = SharedPreferenceStorage(context.applicationContext as Application)
        return newlocalSource
    }



    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            runBlocking {
                localSource?.reset()
            }
            localSource = null

        }
    }
}