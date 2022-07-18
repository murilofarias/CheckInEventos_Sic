package br.com.murilofarias.checkineventos.data.source.local

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import br.com.murilofarias.checkineventos.data.model.User


//SharedPreferences keys
const val MAIN_SHARED = "MAIN_SHARED"
const val USER_NAME = "USER_NAME"
const val USER_EMAIL = "USER_EMAIL"
const val USER_CHECKS = "USER_CHECKS"

class SharedPreferenceStorage(app: Application) : LocalSource {

    private val sharedPreferences: SharedPreferences =
        app.getSharedPreferences(MAIN_SHARED, Context.MODE_PRIVATE)

    override fun getUser(): User {
        val userName = sharedPreferences.getString(USER_NAME, "") ?: ""
        val userEmail = sharedPreferences.getString(USER_EMAIL, "") ?: ""

        return User(userName, userEmail)
    }

    override fun saveUser(user: User){
        val editor = sharedPreferences.edit()

        editor?.apply{
            putString(USER_NAME, user.name)
            putString(USER_EMAIL, user.email)
            apply()
        }

    }

    override fun getCheckIns() : String{
        return sharedPreferences.getString(USER_CHECKS, "") ?: ""
    }

    override fun saveCheckIn(eventId: String){
        var userChecks = sharedPreferences.getString(USER_CHECKS, "") ?: ""
        userChecks = "$userChecks;${eventId}"

        val editor = sharedPreferences.edit()
        editor?.apply{
            putString(USER_CHECKS, userChecks)
            apply()
        }
    }

    override fun reset() {
        val editor = sharedPreferences.edit()

        editor?.apply{
            putString(USER_NAME, "")
            putString(USER_EMAIL, "")
            putString(USER_CHECKS, "")
            apply()
        }
    }
}