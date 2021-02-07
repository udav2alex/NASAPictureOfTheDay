package ru.gressor.nasa_picture.pres.views

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import coil.load
import ru.gressor.nasa_picture.R
import ru.gressor.nasa_picture.data.repo.POTDRepoImpl
import ru.gressor.nasa_picture.databinding.FragmentPotdFullBinding
import ru.gressor.nasa_picture.domain.entities.RequestResult
import ru.gressor.nasa_picture.domain.entities.Vocabulary
import ru.gressor.nasa_picture.pres.App
import ru.gressor.nasa_picture.pres.vmodels.POTDViewModel
import ru.gressor.nasa_picture.pres.vmodels.POTDViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class POTDFragment : Fragment() {
    private lateinit var binding: FragmentPotdFullBinding
    private lateinit var date: String

    private var pictureExpanded: Boolean = true

    private val viewModel: POTDViewModel by viewModels {
        POTDViewModelFactory(POTDRepoImpl(), date)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPotdFullBinding.inflate(inflater, container, false)
        .also {
            binding = it
            date = this.arguments?.getString(BUNDLE_TAG_DATE, "") ?: ""

            activity?.let {
                binding.tvDescription.typeface =
                    Typeface.createFromAsset(activity?.assets, "HallOfFame.ttf")
            }
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

        binding.ivPicture.setOnClickListener {
            if (pictureExpanded) {
                pictureExpanded = false
                collapsePicture()
            } else {
                pictureExpanded = true
                expandPicture()
            }
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

                val spannableText = SpannableString(it.explanation)
                tvDescription.setText(spannableText, TextView.BufferType.SPANNABLE)
                tvDescription.movementMethod = LinkMovementMethod.getInstance()

                val spannable = tvDescription.text as Spannable

                Vocabulary.forEach { phrase ->
                    var position = 0

                    while (position >= 0) {
                        position = spannable.indexOf(phrase, position)

                        if (position >= 0) {
                            val end = position + phrase.length

                            spannable.setSpan(
                                BackgroundColorSpan(Color.RED),
                                position,
                                end,
                                Spannable.SPAN_INCLUSIVE_INCLUSIVE
                            )

                            spannable.setSpan(
                                object: ClickableSpan() {
                                    override fun onClick(widget: View) {
                                        startActivity(Intent(Intent.ACTION_VIEW).apply {
                                            data = Uri.parse(
                                                "https://en.wikipedia.org/wiki/${phrase}"
                                            )
                                        })
                                    }
                                },
                                position,
                                end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )

                            position = end
                        }
                    }
                }

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

    private fun expandPicture() {
        doAnimations(R.layout.fragment_potd_full)
    }

    private fun collapsePicture() {
        doAnimations(R.layout.fragment_potd)
    }

    private fun doAnimations(@LayoutRes layout: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(context, layout)

        val transition = ChangeBounds()
        transition.interpolator = AccelerateDecelerateInterpolator()
        transition.duration = 500

        TransitionManager.beginDelayedTransition(binding.clContainer, transition)
        constraintSet.applyTo(binding.clContainer)
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