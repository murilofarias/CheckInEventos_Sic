package br.com.murilofarias.checkineventos.ui.checkininput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import br.com.murilofarias.checkineventos.data.source.local.LocalSource


class CheckInInputViewModelFactory(
private val localSource: LocalSource
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckInInputViewModel::class.java)) {
            return CheckInInputViewModel(localSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}