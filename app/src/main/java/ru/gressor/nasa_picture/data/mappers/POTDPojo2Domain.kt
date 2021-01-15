package ru.gressor.nasa_picture.data.mappers

import ru.gressor.nasa_picture.data.api.pojo.PictureOfTheDayPOJO
import ru.gressor.nasa_picture.domain.entities.PictureOfTheDay
import java.util.*

class POTDPojo2Domain {

    fun map(pictureOfTheDayPOJO: PictureOfTheDayPOJO): PictureOfTheDay = with(pictureOfTheDayPOJO) {
        PictureOfTheDay(
            date ?: Date().toString(),
            title ?: "",
            copyright ?: "",
            explanation ?: "",
            mediaType ?: "video",
            url ?: "",
            hdurl ?: ""
        )}
}