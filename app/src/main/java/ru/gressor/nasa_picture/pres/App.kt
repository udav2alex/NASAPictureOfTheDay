package ru.gressor.nasa_picture.pres

import android.app.Application
import ru.gressor.nasa_picture.R

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App

        var theme: Themes = Themes.DEFAULT

        fun getNextTheme() = theme.next()
            .also { theme = it }
    }
}

enum class Themes(val id: Int) {
    DEFAULT(R.style.Theme_NASAPictureOfTheDay),
    ANOTHER(R.style.AnotherTheme_NASAPictureOfTheDay);

    fun next(): Themes = values()[
            (ordinal + 1) % values().size]
}