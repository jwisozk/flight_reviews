package com.jwisozk.flightreviews

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull

class EditTextHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup) :
    AbsHolder(inflater, parent, R.layout.edit_text) {

    override fun bind(paramCell: AbsParamCell) {
        if (paramCell.viewType != AbsParamCell.ViewType.EDIT_TEXT)
            return
        val editTextResultCell = paramCell as EditTextParamCell
        val textView = itemView.findViewById<TextView>(R.id.labelEditText)
        textView.text = editTextResultCell.labelTypeString
        editTextResultCell.viewModel.setEditTextLiveData(itemView.findViewById(R.id.feedbackEditText))
    }
}
