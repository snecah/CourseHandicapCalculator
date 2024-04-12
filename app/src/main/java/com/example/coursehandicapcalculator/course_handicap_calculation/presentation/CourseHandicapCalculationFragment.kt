package com.example.coursehandicapcalculator.course_handicap_calculation.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.coursehandicapcalculator.R
import com.example.coursehandicapcalculator.databinding.FragmentCourseHandicapCalculationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CourseHandicapCalculationFragment : Fragment() {

    private var _binding: FragmentCourseHandicapCalculationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CourseHandicapCalculationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseHandicapCalculationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTextWatchers()
        setUpClick()

        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach(::onStateChanged)
            .launchIn(lifecycleScope)
    }

    private fun setUpClick() {
        with(binding) {
            calculateButton.setOnClickListener {
                val score = viewModel.calculateCourseHandicap(
                    handicapIndex.text.toString(),
                    courseRating.text.toString(),
                    slopeRaiting.text.toString(),
                    parRating.text.toString()
                )
                handicapScoreValue.text = score.toString()
            }
        }
    }

    private fun onStateChanged(state: CourseHandicapCalculationState) {
        with(binding) {

            calculateButton.isEnabled = state.isButtonEnabled

            if (state.isSlopeRatingCorrect) {
                slopeRatingContainer.error = null
            } else
                slopeRatingContainer.error = getString(R.string.error_message_slope)

            if (state.isHandicapIndexCorrect) {
                handicapIndexContainer.error = null
            } else
                handicapIndexContainer.error = getString(R.string.error_message_handicap)

            if (state.isCourseRatingCorrect) {
                courseRatingContainer.error = null
            } else
                courseRatingContainer.error = getString(R.string.error_message_course)

            if (state.isPairCorrect) {
                parContainer.error = null
            } else
                parContainer.error = getString(R.string.error_message_par)
        }
    }

    private fun setUpTextWatchers() {
        with(binding) {
            handicapIndex.doAfterTextChanged {
                viewModel.onValueChanged(it.toString(), TextInputType.HANDICAP_INDEX)
            }

            courseRating.doAfterTextChanged {
                viewModel.onValueChanged(it.toString(), TextInputType.COURSE_RATING)
            }

            slopeRaiting.doAfterTextChanged {
                viewModel.onValueChanged(it.toString(), TextInputType.SLOPE_RATING)
            }

            parRating.doAfterTextChanged {
                viewModel.onValueChanged(it.toString(), TextInputType.PAR_RATING)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}