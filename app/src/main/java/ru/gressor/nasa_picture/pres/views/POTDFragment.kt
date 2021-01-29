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
import ru.gressor.nasa_picture.databinding.FragmentPotdBinding
import ru.gressor.nasa_picture.databinding.FragmentPotdStartBinding
import ru.gressor.nasa_picture.domain.entities.RequestResult
import ru.gressor.nasa_picture.pres.App
import ru.gressor.nasa_picture.pres.vmodels.POTDViewModel
import ru.gressor.nasa_picture.pres.vmodels.POTDViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class POTDFragment : Fragment() {
    private lateinit var binding: FragmentPotdStartBinding
    private lateinit var date: String

    private val viewModel: POTDViewModel by viewModels {
        POTDViewModelFactory(POTDRepoImpl(), date)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPotdStartBinding.inflate(inflater, container, false)
        .also {
            binding = it
            date = this.arguments?.getString(BUNDLE_TAG_DATE, "") ?: ""
        }
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
                    lifecycle(this@POTDFragment)
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
        private const val BUNDLE_TAG_DATE = "POTDFragment.BUNDLE_TAG_DATE"

        private fun newInstance(date: String): POTDFragment {
            val fragment = POTDFragment()

            val bundle = Bundle()
            bundle.putString(BUNDLE_TAG_DATE, date)
            fragment.arguments = bundle

            return fragment
        }

        fun newInstance(daysBefore: Int = 0): POTDFragment {
            return newInstance(getDate(daysBefore))
        }

        fun getDate(daysBefore: Int = 0): String {
            val calendar: Calendar = Calendar.getInstance()
            calendar.time = Date()
            calendar.add(Calendar.DATE, -daysBefore)

            val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            return df.format(calendar.time)
        }
    }
}