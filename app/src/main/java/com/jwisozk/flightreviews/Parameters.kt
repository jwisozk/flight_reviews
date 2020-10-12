package com.jwisozk.flightreviews

import android.content.Context

data class Parameters(
    var flight: String = "1",
    var people: String = "1",
    var aircraft: String = "1",
    var seat: String = "1",
    var crew: String = "1",
    var food: String? = "1",
    var text: String? = null,
) {
    companion object {
        fun getLabelArray(context: Context): Array<String> {
            return arrayOf(
                context.getString(R.string.label_rating_bar_people),
                context.getString(R.string.label_rating_bar_aircraft),
                context.getString(R.string.label_rating_bar_seat),
                context.getString(R.string.label_rating_bar_crew),
                context.getString(R.string.label_rating_bar_food),
                context.getString(R.string.label_any_feedback)
            )
        }
    }

    override fun toString(): String {
        return "flight: $flight\n" +
                "people: $people\n" +
                "aircraft: $aircraft\n" +
                "seat: $seat\n" +
                "crew: $crew\n" +
                "food: $food\n" +
                "text: $text"
    }
}