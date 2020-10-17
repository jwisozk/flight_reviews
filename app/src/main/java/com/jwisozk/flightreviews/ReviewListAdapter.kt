package com.jwisozk.flightreviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.jwisozk.flightreviews.AbsParamCell.ViewType

class ReviewListAdapter(
    @NonNull private val inflater: LayoutInflater,
    @NonNull private val viewModel: ParamViewModel,
    @NonNull private val reviewListActionListener: ReviewListActionListener
) : RecyclerView.Adapter<AbsHolder>() {

    @NonNull
    private val viewTypeValues = ViewType.values()
    private val cells = ArrayList<AbsParamCell>()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val labelTypeValues =
            AbsParamCell.LabelType.values()
                .sliceArray(IntRange(1, AbsParamCell.LabelType.values().size - 2))
        for (position in labelTypeValues.indices) {
            cells.add(
                RatingBarParamCell(
                    ViewType.RATING_BAR,
                    labelTypeValues[position],
                    recyclerView.context.getString(labelTypeValues[position].id),
                    viewModel,
                    reviewListActionListener
                )
            )
        }
        cells.add(
            EditTextParamCell(
                ViewType.EDIT_TEXT,
                recyclerView.context.getString(AbsParamCell.LabelType.TEXT.id),
                viewModel
            )
        )
        cells.add(
            ButtonParamCell(
                ViewType.SUBMIT_BUTTON,
                viewModel,
                reviewListActionListener
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        return cells[position].viewType.ordinal
    }

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewTypeOrdinal: Int
    ): AbsHolder {
        return when (val viewType = viewTypeValues[viewTypeOrdinal]) {
            ViewType.RATING_BAR -> RatingBarHolder(inflater, parent)
            ViewType.EDIT_TEXT -> EditTextHolder(inflater, parent)
            ViewType.SUBMIT_BUTTON -> ButtonHolder(inflater, parent)
            else -> throw IllegalArgumentException("Unknown ViewType: $viewType")
        }
    }

    override fun getItemCount(): Int {
        return cells.size
    }

    interface ReviewListActionListener {
        fun onSubmitButtonClick(viewModel: ParamViewModel)
        fun onRatingBarChangeListener(
            viewModel: ParamViewModel,
            rating: Float,
            labelType: AbsParamCell.LabelType
        )

        fun onCheckedChangeListener(
            viewModel: ParamViewModel,
            isChecked: Boolean,
            labelType: AbsParamCell.LabelType
        )
    }

    override fun onBindViewHolder(holder: AbsHolder, position: Int) {
        cells[position].let {
            holder.bind(it)
        }
    }
}
