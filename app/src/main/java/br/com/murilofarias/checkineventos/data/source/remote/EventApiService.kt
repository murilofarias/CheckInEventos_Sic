package br.com.murilofarias.checkineventos.data.source.remote

import android.util.Log
import br.com.murilofarias.checkineventos.data.model.CheckInInfo
import br.com.murilofarias.checkineventos.data.model.Event
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

//if is because of error "Trust Anchor not found for Android SSL Connection"
private val BASE_URL = if(android.os.Build.VERSION.SDK_INT > 25)  "https://5f5a8f24d44d640016169133.mockapi.io/api/" else "http://5f5a8f24d44d640016169133.mockapi.io/api/"



val spec =   listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.COMPATIBLE_TLS)


private val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val client = OkHttpClient.Builder()
    .addInterceptor(interceptor )
    .connectionSpecs(spec)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .callFactory(client)
    .build()

interface EventApiService {

    @GET("events")
    fun getEvents(): Call<List<Event>>

    @GET("events")
    suspend fun getEvents2(): List<Event>

    @POST("checkin")
    suspend fun doCheckIn(@Body checkInInfo: CheckInInfo)

    @GET("events/{eventId}")
    suspend fun getEvent(@Path("eventId") eventId: String): Event



}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object EventApi : RemoteSource {

    private val retrofitService : EventApiService by lazy { retrofit.create(EventApiService::class.java) }

    override suspend fun getEvents(): List<Event> {
        return retrofitService.getEvents2()
    }

    override suspend fun doCheckIn(checkInInfo: CheckInInfo) {
        retrofitService.doCheckIn(checkInInfo)
    }

    override suspend fun getEvent(id: String): Event {
        return retrofitService.getEvent(id)
    }

    override suspend fun uploadEvents(events: List<Event>) {
        Log.i("Network", "Upload Events Not implemented")
    }
}