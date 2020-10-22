package com.jwisozk.flightreviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

class EditTextParamHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
    listener: ParamAdapter.Listener
) : AbsParamHolder(inflater.inflate(R.layout.edit_text, parent, false)) {

    private val editTextFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        editTextCell?.let {
            it.text = (v as EditText).text.toString()
            listener.onEditTextFocusChangeListener(it, hasFocus)
        }
    }

    private val editText = itemView.findViewById<EditText>(R.id.feedbackEditText).apply {
        onFocusChangeListener = editTextFocusChangeListener
    }

    private var editTextCell: ParamCell.EditText? = null

    override fun bind(cell: ParamCell) {
        val editTextCell = cell as ParamCell.EditText
        this.editTextCell = editTextCell
    }
}
