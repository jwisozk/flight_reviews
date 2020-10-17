package com.jwisozk.flightreviews

import android.widget.Button
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jwisozk.flightreviews.util.SingleEventLiveData
import kotlinx.coroutines.launch

class ParamViewModel : ViewModel() {

    companion object {
        data class Input(val repository: Repository)
    }

    @Nullable
    private var input: Input? = null

    private val _parametersLiveData = MutableLiveData<Parameters>()
    val parametersLiveData: LiveData<Parameters> = _parametersLiveData

    private val _snackBarLiveData = SingleEventLiveData<String>()
    val snackBarLiveData: LiveData<String> = _snackBarLiveData

    private val _isLoadingLiveData = SingleEventLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingLiveData

    private val _isRefreshLiveData = SingleEventLiveData<Boolean>()
    val isRefreshLiveData: LiveData<Boolean> = _isRefreshLiveData

    private val _editTextLiveData = SingleEventLiveData<EditText>()
    val editTextLiveData: LiveData<EditText> = _editTextLiveData

    private val _submitButtonLiveData = SingleEventLiveData<Button>()
    val submitButtonLiveData: LiveData<Button> = _submitButtonLiveData

    fun setEditTextLiveData(editText: EditText) {
        _editTextLiveData.value = editText
    }

    fun setSubmitButtonLiveData(button: Button) {
        _submitButtonLiveData.value = button
    }

    fun reset(@NonNull input: Input) {
        if (input == this.input) {
            return
        }
        this.input = input
    }

    fun onOverallRatingChanged(rating: Float?, labelType: AbsParamCell.LabelType) {
        if (rating == null) {
            if (labelType == AbsParamCell.LabelType.FOOD)
                _parametersLiveData.value =
                    _parametersLiveData.value?.copy(food = null) ?: Parameters(food = null)
            return
        }
        val ratingString = (rating.toInt() + Constants.RATE_OFFSET).toString()
        _parametersLiveData.value = when (labelType) {
            AbsParamCell.LabelType.FLIGHT ->
                _parametersLiveData.value?.copy(flight = ratingString)
                    ?: Parameters(flight = ratingString)
            AbsParamCell.LabelType.PEOPLE ->
                _parametersLiveData.value?.copy(people = ratingString)
                    ?: Parameters(people = ratingString)
            AbsParamCell.LabelType.AIRCRAFT ->
                _parametersLiveData.value?.copy(aircraft = ratingString)
                    ?: Parameters(aircraft = ratingString)
            AbsParamCell.LabelType.SEAT ->
                _parametersLiveData.value?.copy(seat = ratingString)
                    ?: Parameters(seat = ratingString)
            AbsParamCell.LabelType.CREW ->
                _parametersLiveData.value?.copy(crew = ratingString)
                    ?: Parameters(crew = ratingString)
            AbsParamCell.LabelType.FOOD ->
                _parametersLiveData.value?.copy(food = ratingString)
                    ?: Parameters(food = ratingString)
            else -> _parametersLiveData.value
        }
    }

    fun onOverallTextChanged(text: String) {
        _parametersLiveData.value =
            _parametersLiveData.value?.copy(text = text) ?: Parameters(text = text)
    }

    /**
     * Refresh the data, showing a loading spinner while it refreshes and errors via snackbar.
     */
    fun refreshData() = launchDataLoad {
        input?.repository?.refreshData()
        if (_parametersLiveData.value == null)
            _parametersLiveData.value = Parameters()
        _isRefreshLiveData.value = true
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
    private fun launchDataLoad(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                _isLoadingLiveData.value = true
                block()
            } catch (error: DataRefreshError) {
                _snackBarLiveData.value = error.message
            } finally {
                _isLoadingLiveData.value = false
            }
        }
    }
}
