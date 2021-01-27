package ru.gressor.nasa_picture.domain.repos

import io.reactivex.rxjava3.core.Single
import ru.gressor.nasa_picture.domain.entities.PictureOfTheDay
import ru.gressor.nasa_picture.domain.entities.RequestResult

interface POTDRepo {
    fun getPictureOfTheDay(): Single<RequestResult>
    fun getPictureOfTheDay(date: String): Single<RequestResult>
}