package com.jwisozk.flightreviews

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.NonNull

class ButtonHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup) :
    AbsHolder(inflater, parent, R.layout.submit_button) {

    override fun bind(paramCell: AbsParamCell) {
        if (paramCell.viewType != AbsParamCell.ViewType.SUBMIT_BUTTON)
            return
        val buttonResultCell = paramCell as ButtonParamCell
        val button = itemView.findViewById<Button>(R.id.submitButton)
        paramCell.viewModel.setSubmitButtonLiveData(button)
        button.setOnClickListener {
            buttonResultCell.reviewListActionListener.onSubmitButtonClick(buttonResultCell.viewModel)
        }
    }
}
