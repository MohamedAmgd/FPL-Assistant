package com.mohamed_amgd.fpl_assistant.ui.viewStates

import com.mohamed_amgd.fpl_assistant.data.models.FilterDialogValues

sealed class FilterDialogViewState {
    object Idle : FilterDialogViewState()
    object Loading : FilterDialogViewState()
    data class PlayerValues(val values: List<String>) : FilterDialogViewState()
    data class SortDirections(val values: List<String>) : FilterDialogViewState()
    data class InitialValues(val values: FilterDialogValues) : FilterDialogViewState()
    object Confirmed : FilterDialogViewState()
    data class Error(val error: String?) : FilterDialogViewState()
}
