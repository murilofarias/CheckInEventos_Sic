package br.com.murilofarias.checkineventos.ui.eventdetail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import br.com.murilofarias.checkineventos.R
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.util.convertLongToDateString
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class  EventDetailFragmentTest{


    @Test
    fun eventDetails_DisplayedInUi() = runBlockingTest{

        // GIVEN
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

        // WHEN - Details fragment launched to display event
        val bundle = EventDetailFragmentArgs(event).toBundle()
        launchFragmentInContainer<EventDetailFragment>(bundle, R.style.Theme_CheckInEventos)

        val context = InstrumentationRegistry.getInstrumentation().context

        onView(withId(R.id.event_title_text)).check(matches(isDisplayed()))
        onView(withId(R.id.event_title_text)).check(matches(withText(event.title)))

        onView(withId(R.id.date_value_text)).check(matches(isDisplayed()))
        onView(withId(R.id.date_value_text)).check(matches(withText(convertLongToDateString(event.date))))

        onView(withId(R.id.price_label_text)).check(matches(withText("Preço:")))
        onView(withId(R.id.price_value_text)).check(matches(isDisplayed()))
        onView(withId(R.id.price_value_text)).check(matches(withText(String.format("$%,.2f", event.price).replace(".", ","))))

        onView(withId(R.id.description_label_text)).check(matches(withText("Descrição:")))
        onView(withId(R.id.description_value_text)).check(matches(isDisplayed()))
        onView(withId(R.id.description_value_text)).check(matches(withText(event.description)))

    }



}