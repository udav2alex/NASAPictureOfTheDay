package ru.gressor.nasa_picture.pres

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.gressor.nasa_picture.R
import ru.gressor.nasa_picture.pres.views.PagerContainerFragment
import ru.gressor.nasa_picture.pres.views.SettingsFragment
import java.lang.IllegalArgumentException
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(App.theme.id)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fl_container, PagerContainerFragment())
                .commitNow()
        }

        findViewById<BottomNavigationView>(R.id.bn_main_navigation_view)
            .setOnNavigationItemSelectedListener {
                val fragment = when (it.itemId) {
                    R.id.mi_pictures -> PagerContainerFragment()
                    R.id.mi_todo -> PagerContainerFragment()
                    R.id.mi_settings -> SettingsFragment()
                    else -> throw IllegalArgumentException("Unknown menu item!")
                }

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .apply {
                        if (isAddToBackStackAllowed) addToBackStack(null)
                    }
                    .commit()

                return@setOnNavigationItemSelectedListener true
            }
    }
}