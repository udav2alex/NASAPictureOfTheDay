package ru.gressor.nasa_picture.pres.vmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.gressor.nasa_picture.domain.repos.POTDRepo

class MainViewModelFactory(
    private val potdRepo: POTDRepo
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MainViewModel::class.java -> MainViewModel(potdRepo)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}