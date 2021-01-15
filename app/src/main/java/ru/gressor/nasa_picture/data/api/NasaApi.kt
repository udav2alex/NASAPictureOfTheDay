package ru.gressor.nasa_picture.data.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import ru.gressor.nasa_picture.data.api.pojo.PictureOfTheDayPOJO

interface NasaApi {
    @GET("planetary/apod")
    fun getPictureOfTheDayPOJO(): Single<PictureOfTheDayPOJO>
}