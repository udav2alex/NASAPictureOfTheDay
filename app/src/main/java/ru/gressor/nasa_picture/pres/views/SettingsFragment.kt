package ru.gressor.nasa_picture.pres.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.gressor.nasa_picture.databinding.FragmentSettingsBinding
import ru.gressor.nasa_picture.pres.App

class SettingsFragment: Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSettingsBinding.inflate(inflater, container, false)
        .also {
            binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnChangeTheme.setOnClickListener {
            App.getNextTheme()
            activity?.recreate()
        }
    }
}