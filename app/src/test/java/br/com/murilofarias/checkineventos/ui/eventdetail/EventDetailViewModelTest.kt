package br.com.murilofarias.checkineventos.ui.eventdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.data.model.User
import br.com.murilofarias.checkineventos.data.source.local.FakeLocalSource
import br.com.murilofarias.checkineventos.data.source.local.LocalSource
import br.com.murilofarias.checkineventos.getOrAwaitValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)

class EventDetailViewModelTest{


    private lateinit var eventDetailViewModel: EventDetailViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var localSource: LocalSource


    @Before
    fun setupViewModel() {

        localSource = FakeLocalSource()
        val user = User("murilo", "murilo.farias@gmail.com")
        localSource.saveUser(user)

        val event = Event(
            "1",
            1534784400000,
            "Descricao do evento",
            "http://lproweb.procempa.com.br/pmpa/prefpoa/seda_news/usu_img/Papel%20de%20Parede.png",
            -30.0392981,
            -51.2146267,
            "Feira de adoção de animais na Redenção",
            29.99
            )


        eventDetailViewModel = EventDetailViewModel(event, localSource)

    }

    @Test
    fun markCheckInAsDone_setsIsCheckInToTrue(){
        eventDetailViewModel.markCheckInAsDone()

        assertEquals(eventDetailViewModel.isCheckIn.getOrAwaitValue(), true)
    }

    @Test
    fun verifyCheckIn_setsIsCheckInToTrueIfAlreadyInLocalSource(){
        localSource.saveCheckIn(eventDetailViewModel.selectedEvent.value!!.id)

        eventDetailViewModel.verifyCheckIn()

        assertEquals(eventDetailViewModel.isCheckIn.getOrAwaitValue(), true)
    }

    @Test
    fun verifyCheckIn_setsIsCheckInToFalseIfItIsntInLocalSource(){

        eventDetailViewModel.verifyCheckIn()

        assertEquals(eventDetailViewModel.isCheckIn.getOrAwaitValue(), false)
    }

}