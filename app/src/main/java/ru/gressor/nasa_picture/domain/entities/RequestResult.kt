package ru.gressor.nasa_picture.domain.entities

sealed class RequestResult {
    class Success(val data: PictureOfTheDay): RequestResult()
    class Error(val throwable: Throwable): RequestResult()
    class Loading(val progress: Int): RequestResult()
}