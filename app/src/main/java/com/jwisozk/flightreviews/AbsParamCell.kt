package com.jwisozk.flightreviews

import androidx.annotation.NonNull

abstract class AbsParamCell(@NonNull private val _viewType: ViewType) {

    enum class ViewType {
        RATING_BAR,
        EDIT_TEXT,
        SUBMIT_BUTTON
    }

    enum class LabelType(val id: Int) {
        FLIGHT(R.string.label_rating_bar_overall),
        PEOPLE(R.string.label_rating_bar_people),
        AIRCRAFT(R.string.label_rating_bar_aircraft),
        SEAT(R.string.label_rating_bar_seat),
        CREW(R.string.label_rating_bar_crew),
        FOOD(R.string.label_rating_bar_food),
        TEXT(R.string.label_any_feedback)
    }

    val viewType = _viewType
}
