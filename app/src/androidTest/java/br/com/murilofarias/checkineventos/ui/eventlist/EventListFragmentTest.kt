package br.com.murilofarias.checkineventos.ui.eventlist

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import br.com.murilofarias.checkineventos.R
import br.com.murilofarias.checkineventos.ServiceLocator
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.data.source.local.LocalSource
import br.com.murilofarias.checkineventos.data.source.remote.RemoteSource
import br.com.murilofarias.checkineventos.date.source.local.FakeLocalSource
import br.com.murilofarias.checkineventos.date.source.remote.FakeRemoteSource
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito


class EventListFragmentTest {

    private lateinit var localSource: LocalSource
    private lateinit var remoteSource: RemoteSource

    private val events: List<Event> = listOf(
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
        remoteSource = FakeRemoteSource(events)
        ServiceLocator.localSource = localSource
        ServiceLocator.remoteSource = remoteSource
    }


    @After
    fun cleanSource() {
        localSource.reset()
    }



    @Test
    fun clickEventListItem_navigateToEventDetailOfFirstElementInList() = runBlockingTest {

        // GIVEN - On the list fragment without
        val navController = Mockito.mock(NavController::class.java)
        //val scenario = launchFragmentInContainer<EventListFragment>(Bundle(), R.style.Theme_CheckInEventos)
        val scenario = launchFragmentInContainer<EventListFragment>(Bundle(), R.style.Theme_CheckInEventos) {
            EventListFragment().also { fragment ->
                // In addition to returning a new instance of our Fragment,
                // get a callback whenever the fragment’s view is created
                // or destroyed so that we can set the mock NavController
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        // The fragment’s view has just been created
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

        //get first event from remoteSource
        val pos = 0
        val firstEvent = remoteSource.getEvents().get(0)

        //Then
        onView(withId(R.id.event_list)).perform(actionOnItemAtPosition<EventAdapter.EventViewHolder>(pos, click()))

        // THEN
        Mockito.verify(navController).navigate(
            EventListFragmentDirections.actionShowDetail(firstEvent)
        )
    }

    @Test
    fun clickEventListItem_navigateToUserInfoIfEmpty() = runBlockingTest {

        // GIVEN - On the list fragment without
        val navController = Mockito.mock(NavController::class.java)
        //val scenario = launchFragmentInContainer<EventListFragment>(Bundle(), R.style.Theme_CheckInEventos)
        val scenario = launchFragmentInContainer<EventListFragment>(Bundle(), R.style.Theme_CheckInEventos) {
            EventListFragment().also { fragment ->
                // In addition to returning a new instance of our Fragment,
                // get a callback whenever the fragment’s view is created
                // or destroyed so that we can set the mock NavController
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        // The fragment’s view has just been created
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }


        // THEN
        Mockito.verify(navController).navigate(
            EventListFragmentDirections.actionEventListFragmentToCheckInInputFragment()
        )
    }


}
