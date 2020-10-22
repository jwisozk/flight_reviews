package com.jwisozk.flightreviews

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RatingBar
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar


class SubmitFragment : Fragment(R.layout.fragment_submit) {

    companion object {
        @JvmStatic
        fun newInstance() = SubmitFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as MainActivity
        val rootLayout: CoordinatorLayout = view.findViewById(R.id.submitFragment)
        val spinner: ProgressBar = view.findViewById(R.id.spinner)
        val repository = Repository(getNetworkService())
        val viewModel = ViewModelProvider(activity).get(ParamViewModel::class.java)
        viewModel.reset(ParamViewModel.Companion.Input(repository))
        val viewManager = LinearLayoutManager(activity)
        val ratingBar = view.findViewById<RatingBar>(R.id.appbarRatingBar)
        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                viewModel.onOverallRatingChanged(rating, AbsParamCell.LabelType.FLIGHT)
            }
        val motionLayout = view.findViewById<MotionLayout>(R.id.motionLayout)
        val appBar = view.findViewById<AppBarLayout>(R.id.appBarLayout)
        appBar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                motionLayout.progress = -verticalOffset / appBar.totalScrollRange.toFloat()
            }
        )
        val cells = viewModel.listParamCellLiveData.value!!
        var submitButton: Button? = null
        val viewAdapter = ParamAdapter(cells, layoutInflater, object : ParamAdapter.Listener {
            override fun onRatingBarChangeListener(cell: ParamCell.RatingBar) {
                viewModel.onOverallRatingChanged(cell.rating, cell.labelType)
            }

            override fun onCheckedChangeListener(cell: ParamCell.RatingBar) {
                if (cell.isCheckboxChecked) {
                    viewModel.onOverallRatingChanged(null, cell.labelType)
                } else {
                    viewModel.onOverallRatingChanged(cell.rating, cell.labelType)
                    viewModel.updateListOfCells(cell)
                }
            }

            override fun onEditTextFocusChangeListener(
                cell: ParamCell.EditText,
                hasFocus: Boolean
            ) {
                if (!hasFocus)
                    viewModel.onOverallTextChanged(cell.text)
            }

            override fun onButtonClickListener(cell: ParamCell.Button, button: Button) {
                if (submitButton == null)
                    submitButton = button
                viewModel.refreshData()
            }
        })
        view.findViewById<RecyclerView>(R.id.recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        viewModel.listParamCellLiveData.observe(viewLifecycleOwner) { list ->
            if (list == null)
                return@observe
            viewAdapter.submitList(list)
        }

        // show the isLoading when [ParamViewModel.isLoading] is true
        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) { value ->
            if (value == null) {
                return@observe
            }
            if (value) {
                spinner.visibility = View.VISIBLE
                submitButton?.visibility = View.GONE
            } else if (spinner.visibility == View.VISIBLE) {
                spinner.visibility = View.GONE
                submitButton?.visibility = View.VISIBLE
            }
        }

        // Show a snackbar whenever the [ParamViewModel.snackbar] is updated a non-null value
        viewModel.snackBarLiveData.observe(viewLifecycleOwner) { text ->
            if (text == null) {
                return@observe
            }
            Snackbar.make(rootLayout, text, Snackbar.LENGTH_SHORT).show()
        }

        // Transition to SuccessFragment
        viewModel.isRefreshLiveData.observe(viewLifecycleOwner) { value ->
            if (value != true) {
                return@observe
            }
            activity.transitionToSuccessFragment()
        }
    }
}
