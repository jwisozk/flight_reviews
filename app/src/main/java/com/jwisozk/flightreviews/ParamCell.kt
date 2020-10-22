package com.jwisozk.flightreviews

sealed class ParamCell {

    data class RatingBar(
        val labelType: AbsParamCell.LabelType,
        var rating: Float,
        val isCheckboxVisible: Boolean,
        var isCheckboxChecked: Boolean
    ) : ParamCell()

    data class EditText(var text: String) : ParamCell()
    object Button : ParamCell()
}
