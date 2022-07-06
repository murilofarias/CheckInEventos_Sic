package br.com.murilofarias.checkineventos.ui.eventlist

import android.content.Context
import android.os.Bundle
import androidx.appcompat.view.menu.ActionMenuItem
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.core.internal.deps.guava.base.Joiner.on
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import br.com.murilofarias.checkineventos.R
import br.com.murilofarias.checkineventos.ServiceLocator
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.data.model.User
import br.com.murilofarias.checkineventos.data.source.local.LocalSource
import br.com.murilofarias.checkineventos.data.source.remote.RemoteSource
import br.com.murilofarias.checkineventos.date.source.local.FakeLocalSource
import br.com.murilofarias.checkineventos.date.source.remote.FakeRemoteSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class EventListFragmentTest {

    private lateinit var localSource: LocalSource
    private lateinit var remoteSource: RemoteSource

    private val events_sample: List<Event> = listOf(
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

    @Before
    fun initLocalSource() {
        localSource = FakeLocalSource()
        remoteSource = FakeRemoteSource()

        ServiceLocator.localSource = localSource
        ServiceLocator.remoteSource = remoteSource
    }


    @After
    fun cleanSource() {
        localSource.reset()
    }



    @Test
    fun clickEventListItem_RemoteSourceWorking_navigateToEventDetailOfFirstElementInList() = runBlockingTest {

        // GIVEN - On the list fragment without
        remoteSource.uploadEvents(events_sample)
        val navController = Mockito.mock(NavController::class.java)
        val scenario = launchFragmentInContainer<EventListFragment>(Bundle(), R.style.Theme_CheckInEventos) {
            EventListFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

        //get first event from remoteSource
        val pos = 0
        val firstEvent = remoteSource.getEvents().get(0)

        //WHEN
        onView(withId(R.id.event_list)).perform(actionOnItemAtPosition<EventAdapter.EventViewHolder>(pos, click()))

        // THEN
        Mockito.verify(navController).navigate(
            EventListFragmentDirections.actionShowDetail(firstEvent)
        )
    }

    @Test
    fun startEventListItem_RemoteSourceWorking_navigateToUserInfoIfEmpty() = runBlockingTest {

        // GIVEN - On the list fragment without
        remoteSource.uploadEvents(events_sample)
        val navController = mock(NavController::class.java)
        val scenario = launchFragmentInContainer<EventListFragment>(Bundle(), R.style.Theme_CheckInEventos) {
            EventListFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

        // THEN

        verify(navController).navigate(
            EventListFragmentDirections.actionEventListFragmentToCheckInInputFragment()
        )
    }




}
