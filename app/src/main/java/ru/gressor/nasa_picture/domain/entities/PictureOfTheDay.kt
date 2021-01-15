package ru.gressor.nasa_picture.domain.entities

data class PictureOfTheDay(
	val date: String = "",
	val title: String = "",
	val copyright: String = "",
	val explanation: String = "",
	val mediaType: String = "",
	val url: String = "",
	val hdurl: String = ""
)
