package com.jwisozk.flightreviews

abstract class AbsParamCell {

    enum class LabelType(val id: Int) {
        FLIGHT(R.string.label_rating_bar_overall),
        PEOPLE(R.string.label_rating_bar_people),
        AIRCRAFT(R.string.label_rating_bar_aircraft),
        SEAT(R.string.label_rating_bar_seat),
        CREW(R.string.label_rating_bar_crew),
        FOOD(R.string.label_rating_bar_food),
        TEXT(R.string.label_any_feedback)
    }
}
