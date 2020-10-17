package com.jwisozk.flightreviews

import androidx.annotation.Nullable

class EditTextParamCell(
    @Nullable private val _viewType: ViewType,
    @Nullable private val _labelTypeString: String,
    @Nullable private val _viewModel: ParamViewModel,
) : AbsParamCell(_viewType) {
    val viewModel = _viewModel
    val labelTypeString = _labelTypeString
}
