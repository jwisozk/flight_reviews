package com.jwisozk.flightreviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

abstract class AbsHolder(
    @NonNull inflater: LayoutInflater,
    @NonNull parent: ViewGroup,
    layoutId: Int
) :
    RecyclerView.ViewHolder(inflater.inflate(layoutId, parent, false)) {

    abstract fun bind(@NonNull paramCell: AbsParamCell)
}
