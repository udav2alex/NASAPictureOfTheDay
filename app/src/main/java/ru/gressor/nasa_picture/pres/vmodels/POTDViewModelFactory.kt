package ru.gressor.nasa_picture.pres.vmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.gressor.nasa_picture.domain.repos.POTDRepo

class POTDViewModelFactory(
    private val potdRepo: POTDRepo,
    private val date: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        POTDViewModel::class.java -> POTDViewModel(potdRepo, date)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}