package ru.gressor.nasa_picture.pres

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.gressor.nasa_picture.R
import ru.gressor.nasa_picture.pres.views.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(App.theme.id)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fl_container, MainFragment.newInstance())
                .commitNow()
        }
    }
}