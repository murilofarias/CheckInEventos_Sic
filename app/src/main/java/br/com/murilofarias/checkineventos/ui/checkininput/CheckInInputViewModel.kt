package br.com.murilofarias.checkineventos.ui.checkininput

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.data.model.User
import br.com.murilofarias.checkineventos.data.source.local.LocalSource


class CheckInInputViewModel(
                            private val localSource: LocalSource
) : ViewModel() {

    private val _user = MutableLiveData<User>()

    // The external LiveData for the SelectedProperty
    val user: LiveData<User>
        get() = _user

    fun onSaveUserInfo(userName: String, userEmail: String){
        localSource.saveUser(User(userName, userEmail))
    }

    fun getUser(){
        _user.value =  localSource.getUser()
    }
}