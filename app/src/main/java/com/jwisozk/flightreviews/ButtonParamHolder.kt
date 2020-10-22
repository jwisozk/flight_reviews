package com.jwisozk.flightreviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class ButtonParamHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
    listener: ParamAdapter.Listener
) : AbsParamHolder(inflater.inflate(R.layout.submit_button, parent, false)) {

    private var buttonCell: ParamCell.Button? = null

    private val button = itemView.findViewById<Button>(R.id.submitButton).apply {
        setOnClickListener(View.OnClickListener {
            if (buttonCell == null) {
                return@OnClickListener
            }
            listener.onButtonClickListener(buttonCell!!, it as Button)
        })
    }

    override fun bind(cell: ParamCell) {
        buttonCell = cell as ParamCell.Button
    }
}
