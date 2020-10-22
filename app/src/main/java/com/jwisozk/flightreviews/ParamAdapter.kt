package com.jwisozk.flightreviews

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class ParamAdapter(
    private var cells: List<ParamCell>,
    private val inflater: LayoutInflater,
    private val listener: Listener
) : RecyclerView.Adapter<AbsParamHolder>() {

    private enum class ViewType {
        RATING_BAR,
        EDIT_TEXT,
        BUTTON
    }

    private val ParamCell.viewType: ViewType
        get() = when (this) {
            is ParamCell.RatingBar -> ViewType.RATING_BAR
            is ParamCell.EditText -> ViewType.EDIT_TEXT
            is ParamCell.Button -> ViewType.BUTTON
        }

    private val viewTypeValues = ViewType.values()

    fun submitList(newCells: List<ParamCell>) {
        cells = newCells
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewTypeOrdinal: Int): AbsParamHolder =
        when (viewTypeValues[viewTypeOrdinal]) {
            ViewType.RATING_BAR -> RatingBarParamHolder(inflater, parent, listener)
            ViewType.EDIT_TEXT -> EditTextParamHolder(inflater, parent, listener)
            ViewType.BUTTON -> ButtonParamHolder(inflater, parent, listener)
        }

    override fun onBindViewHolder(holder: AbsParamHolder, position: Int) =
        holder.bind(cells[position])

    override fun getItemCount(): Int =
        cells.size

    override fun getItemViewType(position: Int): Int =
        cells[position].viewType.ordinal

    interface Listener {
        fun onRatingBarChangeListener(cell: ParamCell.RatingBar)
        fun onCheckedChangeListener(cell: ParamCell.RatingBar)
        fun onButtonClickListener(cell: ParamCell.Button, button: Button)
        fun onEditTextFocusChangeListener(cell: ParamCell.EditText, hasFocus: Boolean)
    }
}
