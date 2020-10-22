package com.jwisozk.flightreviews

data class Parameters(
    val flight: String = "1",
    val people: String = "1",
    val aircraft: String = "1",
    val seat: String = "1",
    val crew: String = "1",
    val food: String? = "1",
    val text: String? = null,
)
