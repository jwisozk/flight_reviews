package com.jwisozk.flightreviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RatingBar
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.jwisozk.flightreviews.util.ParamEnum

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
        val param = Parameters()
        val viewModel = ViewModelProviders
            .of(activity, ParamViewModel.FACTORY(repository))
            .get(ParamViewModel::class.java)
        val viewManager = LinearLayoutManager(activity)
        viewModel.setParameters(param)
        val ratingBar = view.findViewById<RatingBar>(R.id.appbarRatingBar)
        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                viewModel.onOverallRatingChanged(rating, ParamEnum.FLIGHT)
            }
        val motionLayout = view.findViewById<MotionLayout>(R.id.motionLayout)
        val appBar = view.findViewById<AppBarLayout>(R.id.appBarLayout)
        appBar.addOnOffsetChangedListener(object: MotionLayout(activity), AppBarLayout.OnOffsetChangedListener {
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

        val viewAdapter = ReviewAdapter(viewModel)
        view.findViewById<RecyclerView>(R.id.recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // show the isLoading when [ParamViewModel.isLoading] is true
        viewModel.isLoading.observe(viewLifecycleOwner) { value ->
            value?.let { show ->
                if (show) {
                    spinner.visibility = View.VISIBLE
                    val button = view.findViewById<Button>(R.id.submitButton)
                    button.visibility = View.GONE
                } else if (spinner.visibility == View.VISIBLE) {
                    spinner.visibility = View.GONE
                    val button = view.findViewById<Button>(R.id.submitButton)
                    button.visibility = View.VISIBLE
                }
            }
        }

        // Show a snackbar whenever the [ParamViewModel.snackbar] is updated a non-null value
        viewModel.snackbar.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(rootLayout, text, Snackbar.LENGTH_SHORT).show()
            }
        }

        // Transition to SuccessFragment
        viewModel.isRefreshData.observe(viewLifecycleOwner) { value ->
            value?.let { isNextFragment ->
                if (isNextFragment)
                    activity.transitionToSuccessFragment()
            }
        }
    }
}