package br.com.murilofarias.checkineventos.data.source.remote

import br.com.murilofarias.checkineventos.data.model.CheckInInfo
import br.com.murilofarias.checkineventos.data.model.Event
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "https://5f5a8f24d44d640016169133.mockapi.io/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface EventApiService: RemoteSource {

    @GET("events")
    override suspend fun getEvents(): List<Event>

    @POST("checkin")
    override suspend fun doCheckIn(@Body checkInInfo: CheckInInfo)

    @GET("events/{eventId}")
    override suspend fun getEvent(@Path("eventId") eventId: String): Event

    override suspend fun uploadEvents(events : List<Event>)


}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object EventApi {
    val retrofitService : EventApiService by lazy { retrofit.create(EventApiService::class.java) }
}