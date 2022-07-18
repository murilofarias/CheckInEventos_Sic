package br.com.murilofarias.checkineventos.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("dd/MM/yyyy HH:mm")
        .format(systemTime).toString()
}

fun isUserInfoValid(userName: String, userEmail: String) : Boolean {
    if(userName.isBlank() || userEmail.isBlank())
        return false

    return android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()

}
