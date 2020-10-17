package com.jwisozk.flightreviews

import androidx.annotation.Nullable

class RatingBarParamCell(
    @Nullable private val _viewType: ViewType,
    @Nullable private val _labelType: LabelType,
    @Nullable private val _labelTypeString: String,
    @Nullable private val _viewModel: ParamViewModel,
    @Nullable private val _reviewListActionListener: ReviewListAdapter.ReviewListActionListener
) : AbsParamCell(_viewType) {
    val viewModel = _viewModel
    val labelType = _labelType
    val labelTypeString = _labelTypeString
    val reviewListActionListener = _reviewListActionListener
}
