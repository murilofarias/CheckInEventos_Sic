package br.com.murilofarias.checkineventos

import android.app.Application
import androidx.multidex.MultiDexApplication
import br.com.murilofarias.checkineventos.data.source.local.LocalSource
import br.com.murilofarias.checkineventos.data.source.remote.RemoteSource
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller

class EventApplication: MultiDexApplication() {

    val localSource: LocalSource
        get() = ServiceLocator.provideLocalSource(this)

    val remoteSource: RemoteSource
        get() = ServiceLocator.provideRemoteSource(this)

    override fun onCreate() {
        super.onCreate()
        updateAndroidSecurityProvider()
    }

    private fun updateAndroidSecurityProvider() {
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: GooglePlayServicesRepairableException) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            // IGNORE
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
    }

}