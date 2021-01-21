package ru.gressor.nasa_picture.pres.vmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.gressor.nasa_picture.domain.entities.RequestResult
import ru.gressor.nasa_picture.domain.repos.POTDRepo

class MainViewModel(
    private val potdRepo: POTDRepo
) : ViewModel() {

    private val mutableState = MutableLiveData<RequestResult>(RequestResult.Loading(0))
    val state: LiveData<RequestResult> = mutableState

    private val compositeDisposable = CompositeDisposable()

    init {
        loadData()
    }

    private fun loadData() {
        mutableState.postValue(RequestResult.Loading(0))

        compositeDisposable.add(
            potdRepo.getPictureOfTheDay()
                .subscribe { requestResult ->
                    mutableState.postValue(requestResult)
                }
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}