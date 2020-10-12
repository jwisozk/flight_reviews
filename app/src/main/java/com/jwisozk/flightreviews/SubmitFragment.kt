package com.jwisozk.flightreviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RatingBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

var RATE_OFFSET = 1

class SubmitFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    companion object {
        @JvmStatic
        fun newInstance() = SubmitFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_submit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity ?: return

        val rootLayout: CoordinatorLayout = activity.findViewById(R.id.submitFragment)
        val spinner: ProgressBar = activity.findViewById(R.id.spinner)
        val repository = Repository(getNetworkService())
        val viewModel = ViewModelProviders
            .of(activity, ParamViewModel.FACTORY(repository))
            .get(ParamViewModel::class.java)
        viewManager = LinearLayoutManager(activity)
        val param = Parameters()
        val ratingBar = activity.findViewById<RatingBar>(R.id.appbarRatingBar)
        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                param.flight = (rating.toInt() + RATE_OFFSET).toString()
            }
        viewAdapter = ReviewAdapter(viewModel, Parameters.getLabelArray(activity), param)
        recyclerView = activity.findViewById<RecyclerView>(R.id.recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // show the spinner when [ParamViewModel.spinner] is true
        viewModel.spinner.observe(this) { value ->
            value.let { show ->
                spinner.visibility = if (show) View.VISIBLE else View.GONE
            }
        }

        // Show a snackbar whenever the [ParamViewModel.snackbar] is updated a non-null value
        viewModel.snackbar.observe(this) { text ->
            text?.let {
                Snackbar.make(rootLayout, text, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackbarShown()
            }
        }
    }
}