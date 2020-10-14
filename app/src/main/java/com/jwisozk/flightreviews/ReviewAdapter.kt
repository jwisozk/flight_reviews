package com.jwisozk.flightreviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Button
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.jwisozk.flightreviews.util.ParamEnum

class ReviewAdapter(
    val viewModel: ParamViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var labelArray: Array<String>
    var penultimateIndex = 0
    var lastIndex = 0
    lateinit var editText: EditText
    private val param = viewModel.getParameters()?.value

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        labelArray = Parameters.getLabelArray(recyclerView.context)
        penultimateIndex = labelArray.lastIndex - 1
        lastIndex = labelArray.lastIndex
    }

    inner class ButtonHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(@NonNull itemView: View) {
            val button = itemView.findViewById<Button>(R.id.submitButton)
            button.setOnClickListener {
                if (editText.text.isNotEmpty())
                    viewModel.setParameters(textParam = editText.text.toString())
                if (param != null) {
                    viewModel.refreshData()
                }
            }
        }
    }

    inner class RatingBarHolder(@NonNull itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        fun bind(@NonNull itemView: View, position: Int) {
            if (position == penultimateIndex) {
                val checkBox = itemView.findViewById<CheckBox>(R.id.foodCheckBox)
                checkBox.visibility = View.VISIBLE
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) viewModel.setParameters(foodParam = null)
                }
            }
            val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)
            ratingBar.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                    run {
                        when (position) {
                            0 -> viewModel.onOverallRatingChanged(rating, ParamEnum.PEOPLE)
                            1 -> viewModel.onOverallRatingChanged(rating, ParamEnum.AIRCRAFT)
                            2 -> viewModel.onOverallRatingChanged(rating, ParamEnum.SEAT)
                            3 -> viewModel.onOverallRatingChanged(rating, ParamEnum.CREW)
                            4 -> {
                                param?.food?.let {
                                    viewModel.onOverallRatingChanged(rating, ParamEnum.FOOD)
                                }

                            }
                        }
                    }
                }
            val textView = itemView.findViewById<TextView>(R.id.labelRatingBar)
            textView.text = labelArray[position]
        }
    }

    inner class EditTextHolder(@NonNull itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        fun bind(@NonNull itemView: View, position: Int) {
            editText = itemView.findViewById(R.id.feedbackEditText)
            val textView = itemView.findViewById<TextView>(R.id.labelEditText)
            textView.text = labelArray[position]
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            labelArray.size -> R.layout.button
            lastIndex -> R.layout.edit_text
            else -> R.layout.rating_bar
        }
    }

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.rating_bar -> {
                RatingBarHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.rating_bar, parent, false) as ViewGroup)
            }
            R.layout.edit_text -> {
                EditTextHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.edit_text, parent, false) as ViewGroup)
            }
            else -> {
                ButtonHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.button, parent, false))
            }
        }
    }

    override fun getItemCount() = labelArray.size + 1

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.button -> {
                val buttonHolder = viewHolder as ButtonHolder
                buttonHolder.bind(viewHolder.itemView)
            }
            R.layout.edit_text -> {
                val editTextHolder = viewHolder as EditTextHolder
                editTextHolder.bind(viewHolder.itemView, position)
            }
            R.layout.rating_bar -> {
                val itemHolder = viewHolder as RatingBarHolder
                itemHolder.bind(viewHolder.itemView, position)
            }
        }
    }
}