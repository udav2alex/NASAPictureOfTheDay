package ru.gressor.nasa_picture.pres.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import ru.gressor.nasa_picture.R
import ru.gressor.nasa_picture.data.repo.POTDRepoImpl
import ru.gressor.nasa_picture.databinding.FragmentMainBinding
import ru.gressor.nasa_picture.domain.entities.RequestResult
import ru.gressor.nasa_picture.pres.App
import ru.gressor.nasa_picture.pres.vmodels.MainViewModel
import ru.gressor.nasa_picture.pres.vmodels.MainViewModelFactory

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(POTDRepoImpl())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMainBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.state.observe(this.viewLifecycleOwner, this::populateViews)

        binding.etSearch.setOnEditorActionListener { _, actionId, event ->
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER))
                || (actionId == EditorInfo.IME_ACTION_DONE)
            ) {
                startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(
                        "https://en.wikipedia.org/wiki/${binding.etSearch.text.toString()}"
                    )
                })
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.btnChangeTheme.setOnClickListener {
            App.getNextTheme()
            activity?.recreate()
        }
    }

    private fun populateViews(requestResult: RequestResult) {
        when (requestResult) {
            is RequestResult.Success -> populateViews(requestResult)
            is RequestResult.Error -> populateViews(requestResult)
            is RequestResult.Loading -> populateViews(requestResult)
        }
    }

    private fun populateViews(success: RequestResult.Success) {
        binding.apply {
            success.data.let {
                tvDate.text = it.date
                tvTitle.text = it.title
                tvCopyright.text = it.copyright
                tvDescription.text = it.explanation
                ivPicture.load(it.url) {
                    lifecycle(this@MainFragment)
                    error(R.drawable.ic_load_error_vector)
                    placeholder(R.drawable.ic_no_photo_vector)
                }
            }
        }
    }

    private fun populateViews(loading: RequestResult.Loading) {
        binding.apply {
            tvDate.text = loading.progress.toString()
            tvTitle.text = getString(R.string.loading)
            tvCopyright.text = getString(R.string.loading)
            tvDescription.text = ""
            ivPicture.load(R.drawable.ic_no_photo_vector)
        }
    }

    private fun populateViews(error: RequestResult.Error) {
        binding.apply {
            tvDate.text = ""
            tvTitle.text = getString(R.string.error)
            tvCopyright.text = getString(R.string.error)
            tvDescription.text = error.throwable.message
            ivPicture.load(R.drawable.ic_load_error_vector)
        }
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}