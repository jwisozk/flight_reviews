package com.jwisozk.flightreviews

import androidx.annotation.Nullable

class ButtonParamCell(
    @Nullable private val _viewType: ViewType,
    @Nullable private val _viewModel: ParamViewModel,
    @Nullable private val _reviewListActionListener: ReviewListAdapter.ReviewListActionListener
) : AbsParamCell(_viewType) {
    val viewModel = _viewModel
    val reviewListActionListener = _reviewListActionListener
}
