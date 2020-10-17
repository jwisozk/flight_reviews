package com.jwisozk.flightreviews

import androidx.annotation.NonNull

data class Parameters(
    @NonNull private val flight: String = "1",
    @NonNull private val people: String = "1",
    @NonNull private val aircraft: String = "1",
    @NonNull private val seat: String = "1",
    @NonNull private val crew: String = "1",
    private val food: String? = "1",
    private val text: String? = null,
)
