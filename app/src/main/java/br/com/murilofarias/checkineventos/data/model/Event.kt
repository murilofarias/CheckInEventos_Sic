package br.com.murilofarias.checkineventos.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val id: String,
    val date: Long,
    val description: String,
    val image: String,
    val latitude: Double,
    val longitude:Double,
    val title: String,
    val price: Double) : Parcelable

data class CheckInInfo(
    val eventId : String,
    val userName: String,
    val userEmail: String
)