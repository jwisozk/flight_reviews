package com.jwisozk.flightreviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.NonNull

class RatingBarHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup) :
    AbsHolder(inflater, parent, R.layout.rating_bar) {

    override fun bind(paramCell: AbsParamCell) {
        if (paramCell.viewType != AbsParamCell.ViewType.RATING_BAR)
            return
        val ratingBarParamCell = paramCell as RatingBarParamCell
        if (ratingBarParamCell.labelType == AbsParamCell.LabelType.FOOD) {
            val checkBox = itemView.findViewById<CheckBox>(R.id.foodCheckBox)
            checkBox.visibility = View.VISIBLE
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                ratingBarParamCell.reviewListActionListener.onCheckedChangeListener(
                    ratingBarParamCell.viewModel,
                    isChecked,
                    ratingBarParamCell.labelType
                )
            }
        }
        val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)
        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                ratingBarParamCell.reviewListActionListener.onRatingBarChangeListener(
                    ratingBarParamCell.viewModel,
                    rating,
                    ratingBarParamCell.labelType
                )
            }
        val textView = itemView.findViewById<TextView>(R.id.labelRatingBar)
        textView.text = ratingBarParamCell.labelTypeString
    }
}
