package com.jwisozk.flightreviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RatingBar
import android.widget.TextView

class RatingBarParamHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
    listener: ParamAdapter.Listener
) : AbsParamHolder(inflater.inflate(R.layout.rating_bar, parent, false)) {

    private val ratingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
        ratingBarCell?.let {
            it.rating = rating
            listener.onRatingBarChangeListener(it)
        }
    }
    private val checkedChangeListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        ratingBarCell?.let {
            it.isCheckboxChecked = isChecked
            listener.onCheckedChangeListener(it)
        }
    }

    private val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar).apply {
        onRatingBarChangeListener = ratingBarChangeListener
    }
    private val checkBox = itemView.findViewById<CheckBox>(R.id.foodCheckBox).apply {
        setOnCheckedChangeListener(checkedChangeListener)
    }
    private val textView = itemView.findViewById<TextView>(R.id.labelRatingBar)

    private var ratingBarCell: ParamCell.RatingBar? = null

    override fun bind(cell: ParamCell) {
        val ratingBarCell = cell as ParamCell.RatingBar
        this.ratingBarCell = ratingBarCell
        ratingBar.apply {
            onRatingBarChangeListener = null
            rating = ratingBarCell.rating
            onRatingBarChangeListener = ratingBarChangeListener
        }

        checkBox.apply {
            if (ratingBarCell.isCheckboxVisible) {
                visibility = View.VISIBLE
                setOnCheckedChangeListener(null)
                isChecked = ratingBarCell.isCheckboxChecked
                setOnCheckedChangeListener(checkedChangeListener)
            } else {
                visibility = View.GONE
            }
        }

        textView.setText(ratingBarCell.labelType.id)
    }
}
