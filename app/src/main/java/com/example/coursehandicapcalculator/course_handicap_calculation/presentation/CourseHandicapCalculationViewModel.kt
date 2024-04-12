package com.example.coursehandicapcalculator.course_handicap_calculation.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val HANDICAP_LEFT_BOUND = -10.0
private const val HANDICAP_RIGHT_BOUND = 54.0
private const val COURSE_LEFT_BOUND = 50.0
private const val COURSE_RIGHT_BOUND = 86.0
private const val PAR_LEFT_BOUND = 50.0
private const val PAR_RIGHT_BOUND = 85.0
private const val SLOPE_LEFT_BOUND = 55.0
private const val SLOPE_RIGHT_BOUND = 155.0


@HiltViewModel
class CourseHandicapCalculationViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(
        CourseHandicapCalculationState()
    )
    val state = _state.asStateFlow()
    fun onValueChanged(value: String, type: TextInputType) {
        val isCorrect = when (type) {
            TextInputType.HANDICAP_INDEX -> {
                validateField(
                    value, HANDICAP_LEFT_BOUND, HANDICAP_RIGHT_BOUND
                )
            }

            TextInputType.COURSE_RATING -> validateField(
                value, COURSE_LEFT_BOUND, COURSE_RIGHT_BOUND
            )

            TextInputType.SLOPE_RATING -> validateField(value, SLOPE_LEFT_BOUND, SLOPE_RIGHT_BOUND)
            TextInputType.PAR_RATING -> validateField(value, PAR_LEFT_BOUND, PAR_RIGHT_BOUND)
        }


        when (type) {
            TextInputType.HANDICAP_INDEX -> {
                val isCalculateButtonEnabled = isCorrect && checkAreFieldsCorrect(
                    state.value.isCourseRatingCorrect,
                    state.value.isSlopeRatingCorrect,
                    state.value.isPairCorrect
                )
                _state.update { state ->
                    state.copy(
                        isHandicapIndexCorrect = isCorrect,
                        isButtonEnabled = isCalculateButtonEnabled
                    )
                }
            }

            TextInputType.COURSE_RATING -> {
                val isCalculateButtonEnabled = isCorrect && checkAreFieldsCorrect(
                    state.value.isHandicapIndexCorrect,
                    state.value.isSlopeRatingCorrect,
                    state.value.isPairCorrect
                )
                _state.update { state ->
                    state.copy(
                        isCourseRatingCorrect = isCorrect,
                        isButtonEnabled = isCalculateButtonEnabled
                    )
                }
            }

            TextInputType.SLOPE_RATING -> {
                val isCalculateButtonEnabled = isCorrect && checkAreFieldsCorrect(
                    state.value.isHandicapIndexCorrect,
                    state.value.isCourseRatingCorrect,
                    state.value.isPairCorrect
                )
                _state.update { state ->
                    state.copy(
                        isSlopeRatingCorrect = isCorrect,
                        isButtonEnabled = isCalculateButtonEnabled
                    )
                }
            }

            TextInputType.PAR_RATING -> {
                val isCalculateButtonEnabled = isCorrect && checkAreFieldsCorrect(
                    state.value.isHandicapIndexCorrect,
                    state.value.isCourseRatingCorrect,
                    state.value.isSlopeRatingCorrect
                )
                _state.update { state ->
                    state.copy(
                        isPairCorrect = isCorrect,
                        isButtonEnabled = isCalculateButtonEnabled
                    )
                }
            }
        }
    }

    private fun checkAreFieldsCorrect(field1: Boolean, field2: Boolean, field3: Boolean): Boolean {
        return field1 && field2 && field3
    }

    private fun validateField(value: String, leftBound: Double, rightBound: Double): Boolean {
        if (value.isNotEmpty()) {
            if (value.first() != '.') {
                val ratingNumber = value.toDouble()
                return (ratingNumber >= leftBound) and (ratingNumber <= rightBound)
            }
        }
        return false
    }
}