package br.com.murilofarias.checkineventos.data.network

import br.com.murilofarias.checkineventos.data.model.CheckInInfo
import br.com.murilofarias.checkineventos.data.model.Event
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL = "https://5f5a8f24d44d640016169133.mockapi.io/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface EventApiService {

    @GET("events")
    suspend fun getEvents(): List<Event>

    @POST("checkin")
    suspend fun doCheckIn(@Body checkInInfo: CheckInInfo)
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object EventApi {
    val retrofitService : EventApiService by lazy { retrofit.create(EventApiService::class.java) }
}