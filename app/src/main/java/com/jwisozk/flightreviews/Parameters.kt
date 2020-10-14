package com.jwisozk.flightreviews

import android.content.Context

data class Parameters(
    val flight: String = "1",
    val people: String = "1",
    val aircraft: String = "1",
    val seat: String = "1",
    val crew: String = "1",
    val food: String? = "1",
    val text: String? = null,
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