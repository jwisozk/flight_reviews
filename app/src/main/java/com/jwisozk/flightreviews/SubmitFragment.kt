package com.jwisozk.flightreviews

import android.os.Bundle
import android.view.View
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
        appBar.addOnOffsetChangedListener(object : MotionLayout(activity),
            AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                appBarLayout?.totalScrollRange?.toFloat()?.let {
                    motionLayout.progress = -verticalOffset / it
                }
            }

            override fun onAttachedToWindow() {
                super.onAttachedToWindow()
                (parent as? AppBarLayout)?.addOnOffsetChangedListener(this)
            }
        })

        val viewAdapter = ReviewListAdapter(
            layoutInflater,
            viewModel,
            object : ReviewListAdapter.ReviewListActionListener {
                override fun onSubmitButtonClick(viewModel: ParamViewModel) {
                    val editText = viewModel.editTextLiveData.value
                    if (editText != null && editText.text.isNotEmpty()) {
                        viewModel.onOverallTextChanged(editText.text.toString())
                    }
                    viewModel.refreshData()
                }

                override fun onRatingBarChangeListener(
                    viewModel: ParamViewModel,
                    rating: Float,
                    labelType: AbsParamCell.LabelType
                ) {
                    viewModel.onOverallRatingChanged(rating, labelType)
                }

                override fun onCheckedChangeListener(
                    viewModel: ParamViewModel,
                    isChecked: Boolean,
                    labelType: AbsParamCell.LabelType
                ) {
                    if (isChecked) {
                        viewModel.onOverallRatingChanged(null, labelType)
                    }
                }
            })
        view.findViewById<RecyclerView>(R.id.recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // show the isLoading when [ParamViewModel.isLoading] is true
        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) { value ->
            if (value == null) {
                return@observe
            }
            val button = viewModel.submitButtonLiveData.value
            if (value) {
                spinner.visibility = View.VISIBLE
                button?.visibility = View.GONE
            } else if (spinner.visibility == View.VISIBLE) {
                spinner.visibility = View.GONE
                button?.visibility = View.VISIBLE
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
