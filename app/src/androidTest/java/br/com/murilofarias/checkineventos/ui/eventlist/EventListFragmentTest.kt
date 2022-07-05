package br.com.murilofarias.checkineventos.ui.eventlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import br.com.murilofarias.checkineventos.R
import br.com.murilofarias.checkineventos.ServiceLocator
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.data.source.local.LocalSource
import br.com.murilofarias.checkineventos.data.source.remote.RemoteSource
import br.com.murilofarias.checkineventos.date.source.local.FakeLocalSource
import br.com.murilofarias.checkineventos.date.source.local.FakeRemoteSource
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.timeout


class EventListFragmentTest {

    private lateinit var localSource: LocalSource
    private lateinit var remoteSource: RemoteSource

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
    fun clickEventListItem_navigateToEventDetail() = runBlockingTest {

        // GIVEN - On the list fragment without
        val scenario = launchFragmentInContainer<EventListFragment>(Bundle(), R.style.Theme_CheckInEventos)
        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.event_list)).perform(actionOnItemAtPosition<EventAdapter.EventViewHolder>(0, click()));

        // THEN - Verify that we navigate to the add screen
        Mockito.verify(navController, timeout(5000).times(1)).navigate(
            EventListFragmentDirections.actionShowDetail(remoteSource.getEvent("1"))
        )
    }
}
