package com.example.coursehandicapcalculator.course_handicap_calculation.presentation

data class CourseHandicapCalculationState(
    val isHandicapIndexCorrect: Boolean = false,
    val isCourseRatingCorrect: Boolean = false,
    val isSlopeRatingCorrect: Boolean = false,
    val isPairCorrect: Boolean = false,
    val isButtonEnabled:Boolean = false
)
