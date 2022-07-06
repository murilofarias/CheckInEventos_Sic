package br.com.murilofarias.checkineventos.ui.eventlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.murilofarias.checkineventos.data.model.User
import br.com.murilofarias.checkineventos.data.source.local.FakeLocalSource
import br.com.murilofarias.checkineventos.data.source.local.LocalSource
import br.com.murilofarias.checkineventos.data.source.remote.FakeRemoteSource
import br.com.murilofarias.checkineventos.data.source.remote.RemoteSource
import br.com.murilofarias.checkineventos.events_sample
import br.com.murilofarias.checkineventos.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventListViewModelTest {


    private lateinit var eventListViewModel: EventListViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var localSource: LocalSource
    private lateinit var remoteSource: RemoteSource

    @Before
    fun setUpList() = runBlocking {
        remoteSource = FakeRemoteSource()
        localSource = FakeLocalSource()
        remoteSource.uploadEvents(events_sample)
        eventListViewModel = EventListViewModel(remoteSource, localSource)
    }

    //An std Event is an
    @Test
    fun displayEventDetails_StandardEvent_navigatoToSelectedToStandardEventGiven() = runBlocking {
        //GIVEN
        //Any standard event
        val event = remoteSource.getEvents().get(0)

        //WHEN
        eventListViewModel.displayEventDetails(event)

        //THEN
        assertEquals(event.id, eventListViewModel.navigateToSelectedEvent.getOrAwaitValue()?.id)
    }

    @Test
    fun displayEventDetailsComplete_navigateSelectedEventToNull() {

        //WHEN
        eventListViewModel.displayEventDetailsComplete()

        //THEN
        assertEquals(null, eventListViewModel.navigateToSelectedEvent.getOrAwaitValue()?.id)
    }

    @Test
    fun checkUserInfoSetup_NameAndEmailEmpty_navigateToUserInfoToTrue() {
        //GIVEN
        localSource.saveUser(User("", ""))

        //WHEN
        eventListViewModel.checkUserInfoSetup()

        //THEN
        assertEquals(true, eventListViewModel.navigateToUserInfo.getOrAwaitValue())
    }

    @Test
    fun checkUserInfoSetup_NameNotBlankAndEmpty_navigateToUserInfoToTrue() {
        //GIVEN
        localSource.saveUser(User("murilo", ""))

        //WHEN
        eventListViewModel.checkUserInfoSetup()

        //THEN
        assertEquals(true, eventListViewModel.navigateToUserInfo.getOrAwaitValue())
    }

    @Test
    fun checkUserInfoSetup_NameAndEmptyNotBlank_navigateToUserInfoToTrue() {
        //GIVEN
        localSource.saveUser(User("murilo", "murilo@gmail.com"))

        //WHEN
        eventListViewModel.checkUserInfoSetup()

        //THEN
        assertEquals(false, eventListViewModel.navigateToUserInfo.getOrAwaitValue())
    }

    @Test
    fun displayUserInfoComplete_navigateToUserInfoToFalse() {

        //WHEN
        eventListViewModel.displayUserInfoComplete()

        //THEN
        assertEquals(false, eventListViewModel.navigateToUserInfo.getOrAwaitValue())
    }
}