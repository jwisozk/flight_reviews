package com.jwisozk.flightreviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView


class SuccessFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = SuccessFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity ?: return
        val resultTextView = activity.findViewById<TextView>(R.id.resultReview)
        val repository = Repository(getNetworkService())
        val viewModel = ViewModelProviders
            .of(activity, ParamViewModel.FACTORY(repository))
            .get(ParamViewModel::class.java)
        viewModel.getParameters()?.observe(viewLifecycleOwner,
            {
                if (it == null)
                    return@observe
                resultTextView.text = it.toString()
            })
        val button = activity.findViewById<Button>(R.id.showParamButton)
        button.setOnClickListener {
            val animation = activity.findViewById<LottieAnimationView>(R.id.animationView)
            animation.visibility = View.GONE
            resultTextView.visibility = View.VISIBLE
        }
    }
}