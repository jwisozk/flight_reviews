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

class ReviewAdapter(
    val viewModel: ParamViewModel,
    val labelArray: Array<String>,
    val param: Parameters
) : RecyclerView.Adapter<ReviewAdapter.ItemHolder>() {
    val penultimateIndex = this.labelArray.lastIndex - 1
    val lastIndex = this.labelArray.lastIndex
    lateinit var editText: EditText

    inner class ItemHolder(@NonNull itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {

        fun bind(@NonNull itemView: View, position: Int, layoutId: Int) {
            when (layoutId) {
                R.layout.item -> {
                    when (position) {
                        penultimateIndex -> {
                            val checkBox = itemView.findViewById<CheckBox>(R.id.foodCheckBox)
                            checkBox.visibility = View.VISIBLE
                            checkBox.setOnCheckedChangeListener { _, isChecked ->
                                if (isChecked) param.food = null
                            }
                        }
                        lastIndex -> {
                            val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)
                            ratingBar.visibility = View.GONE
                            editText = itemView.findViewById(R.id.feedbackEditText)
                            editText.visibility = View.VISIBLE
                        }
                    }
                    val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)
                    ratingBar.onRatingBarChangeListener =
                        RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                            run {
                                when (position) {
                                    0 -> param.people = (rating.toInt() + RATE_OFFSET).toString()
                                    1 -> param.aircraft = (rating.toInt() + RATE_OFFSET).toString()
                                    2 -> param.seat = (rating.toInt() + RATE_OFFSET).toString()
                                    3 -> param.crew = (rating.toInt() + RATE_OFFSET).toString()
                                    4 -> {
                                        if (param.food != null)
                                            param.food = (rating.toInt() + RATE_OFFSET).toString()
                                    }
                                }
                            }
                        }
                    val textView = itemView.findViewById<TextView>(R.id.labelRatingBar)
                    textView.text = labelArray[position]
                }
                R.layout.button -> {
                    val button = itemView.findViewById<Button>(R.id.submitButton)
                    button.setOnClickListener {
                        if (editText.text.isNotEmpty())
                            param.text = editText.text.toString()
                        val activity = editText.context as MainActivity
                        viewModel.refreshData(activity, param)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            labelArray.size -> R.layout.button
            else -> R.layout.item
        }
    }

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): ItemHolder {
        val itemView = when (viewType) {
            R.layout.item -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item, parent, false)
            else -> LayoutInflater.from(parent.context)
                .inflate(R.layout.button, parent, false)
        }
        return ItemHolder(itemView as ViewGroup)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(holder.itemView, position, getItemViewType(position))
    }

    override fun getItemCount() = labelArray.size + 1
}