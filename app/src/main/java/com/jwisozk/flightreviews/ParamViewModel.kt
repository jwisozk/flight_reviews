package com.jwisozk.flightreviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val spinner: LiveData<Boolean>
        get() = _spinner

    /**
     * Request a snackbar to display a string.
     */
    val snackbar: LiveData<String?>
        get() = _snackBar

    private val _parameters = MutableLiveData<Parameters>()
    private val _snackBar = MutableLiveData<String?>()
    private val _spinner = MutableLiveData(false)

    fun getParameters(): LiveData<Parameters>? {
        return _parameters
    }

    fun setParameters(parameters: Parameters) {
        _parameters.value = parameters
    }

    /**
     * Called immediately after the UI shows the snackbar.
     */
    fun onSnackbarShown() {
        _snackBar.value = null
    }

    /**
     * Refresh the data, showing a loading spinner while it refreshes and errors via snackbar.
     */
    fun refreshData(activity: MainActivity, param: Parameters) = launchDataLoad {
        repository.refreshData()
        setParameters(param)
        activity.transitionToSuccessFragment()
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
                _spinner.value = true
                block()
            } catch (error: DataRefreshError) {
                _snackBar.value = error.message
            } finally {
                _spinner.value = false
            }
        }
    }
}