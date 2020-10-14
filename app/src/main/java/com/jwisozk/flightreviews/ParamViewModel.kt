package com.jwisozk.flightreviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jwisozk.flightreviews.util.ParamEnum
import com.jwisozk.flightreviews.util.SingleEventLiveData
import com.jwisozk.flightreviews.util.singleArgViewModelFactory
import kotlinx.coroutines.launch


class ParamViewModel(private val repository: Repository) : ViewModel() {

    companion object {
        /**
         * Factory for creating [ParamViewModel]
         *
         * @param arg the repository to pass to [ParamViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::ParamViewModel)
    }

    /**
     * Show a loading spinner if true
     */
    val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData

    /**
     * Request a snackbar to display a string.
     */
    val snackbar: LiveData<String?>
        get() = snackBarLiveData

    /**
     * Transition to SuccessFragment if true
     */
    val isRefreshData: LiveData<Boolean>
        get() = isRefreshLiveData

    private val parametersLiveData = MutableLiveData<Parameters>()
    private val snackBarLiveData = SingleEventLiveData<String?>()
    private val isLoadingLiveData = MutableLiveData(false)
    private val isRefreshLiveData = MutableLiveData(false)

    fun getParameters(): LiveData<Parameters>? {
        return parametersLiveData
    }

    fun setParameters(parameters: Parameters) {
            parametersLiveData.value = parameters
    }

    fun setParameters(flightParam: String? = null,
                      peopleParam: String? = null,
                      aircraftParam: String? = null,
                      seatParam: String? = null,
                      crewParam: String? = null,
                      foodParam: String? = null,
                      textParam: String? = null,
    ) {
        parametersLiveData.value?.let {
            val param = it.copy(
                flight = flightParam ?: it.flight,
                people = peopleParam ?: it.people,
                aircraft = aircraftParam ?: it.aircraft,
                seat = seatParam ?: it.seat,
                crew = crewParam ?: it.crew,
                food = foodParam ?: it.food,
                text = textParam ?: it.text
            )
            setParameters(param)
        }
    }

    fun onOverallRatingChanged(rating: Float, paramEnum: ParamEnum) {
        when (paramEnum) {
            ParamEnum.FLIGHT -> {
                setParameters(flightParam = (rating.toInt() + Constants.RATE_OFFSET).toString())
            }
            ParamEnum.PEOPLE -> {
                setParameters(peopleParam = (rating.toInt() + Constants.RATE_OFFSET).toString())
            }
            ParamEnum.AIRCRAFT -> {
                setParameters(aircraftParam = (rating.toInt() + Constants.RATE_OFFSET).toString())
            }
            ParamEnum.SEAT -> {
                setParameters(seatParam = (rating.toInt() + Constants.RATE_OFFSET).toString())
            }
            ParamEnum.CREW -> {
                setParameters(crewParam = (rating.toInt() + Constants.RATE_OFFSET).toString())
            }
            ParamEnum.FOOD -> {
                setParameters(foodParam = (rating.toInt() + Constants.RATE_OFFSET).toString())
            }
            ParamEnum.TEXT -> {
                setParameters(textParam = (rating.toInt() + Constants.RATE_OFFSET).toString())
            }
        }
    }

    /**
     * Refresh the data, showing a loading spinner while it refreshes and errors via snackbar.
     */
    fun refreshData() = launchDataLoad {
        repository.refreshData()
        isRefreshLiveData.value = true
    }
    
    /**
     * Helper function to call a data load function with a loading spinner, errors will trigger a
     * snackbar.
     *
     * By marking `block` as `suspend` this creates a suspend lambda which can call suspend
     * functions.
     *
     * @param block lambda to actually load data. It is called in the viewModelScope. Before calling the
     *              lambda the loading spinner will display, after completion or error the loading
     *              spinner will stop
     */
    private fun launchDataLoad(block: suspend () -> Unit): Unit {
        viewModelScope.launch {
            try {
                isLoadingLiveData.value = true
                block()
            } catch (error: DataRefreshError) {
                snackBarLiveData.value = error.message
            } finally {
                isLoadingLiveData.value = false
            }
        }
    }
}