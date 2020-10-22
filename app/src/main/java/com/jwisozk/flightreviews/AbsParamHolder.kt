package com.jwisozk.flightreviews

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class AbsParamHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(cell: ParamCell)
}
