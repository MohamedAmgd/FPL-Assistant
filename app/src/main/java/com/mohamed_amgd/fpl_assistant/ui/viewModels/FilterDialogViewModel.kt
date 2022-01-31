package com.mohamed_amgd.fpl_assistant.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed_amgd.fpl_assistant.data.Repo
import com.mohamed_amgd.fpl_assistant.data.models.FilterDialogValues
import com.mohamed_amgd.fpl_assistant.data.models.PlayerValueFilter
import com.mohamed_amgd.fpl_assistant.data.models.PlayersListFilter
import com.mohamed_amgd.fpl_assistant.data.models.SortDirectionFilter
import com.mohamed_amgd.fpl_assistant.ui.intents.FilterDialogIntent
import com.mohamed_amgd.fpl_assistant.ui.viewStates.FilterDialogViewState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class FilterDialogViewModel : ViewModel() {
    val intentChannel = Channel<FilterDialogIntent>(Channel.UNLIMITED)
    private val _stateFlow = MutableStateFlow<FilterDialogViewState>(FilterDialogViewState.Idle)
    val state: StateFlow<FilterDialogViewState>
        get() = _stateFlow

    init {
        loadPlayerValues()
        loadSortDirections()
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect {
                processIntent(it)
            }
        }
    }

    private fun processIntent(intent: FilterDialogIntent) {
        when (intent) {
            is FilterDialogIntent.Confirm -> {
                _stateFlow.value = FilterDialogViewState.Loading
                _stateFlow.value = try {
                    confirm(intent.values)
                    FilterDialogViewState.Confirmed
                } catch (e: Exception) {
                    FilterDialogViewState.Error(e.localizedMessage)
                }
            }
            is FilterDialogIntent.InitialValues -> {
                _stateFlow.value = FilterDialogViewState.Loading
                _stateFlow.value = try {
                    FilterDialogViewState.InitialValues(loadInitialValues())
                } catch (e: Exception) {
                    FilterDialogViewState.Error(e.localizedMessage)
                }
            }
            is FilterDialogIntent.PlayerValues -> {
                _stateFlow.value = FilterDialogViewState.Loading
                _stateFlow.value = try {
                    FilterDialogViewState.PlayerValues(loadPlayerValues())
                } catch (e: Exception) {
                    FilterDialogViewState.Error(e.localizedMessage)
                }
            }
            is FilterDialogIntent.SortDirections -> {
                _stateFlow.value = FilterDialogViewState.Loading
                _stateFlow.value = try {
                    FilterDialogViewState.SortDirections(loadSortDirections())
                } catch (e: Exception) {
                    FilterDialogViewState.Error(e.localizedMessage)
                }
            }
        }
    }

    private fun confirm(
        values: FilterDialogValues
    ) {
        val playerValue = loadPlayerValues()[values.playerValueIndex]
        val sortDir = loadSortDirections()[values.sortDirIndex]
        val playersListFilter = PlayersListFilter(
            null,
            PlayerValueFilter.fromName(playerValue),
            values.minValue.ifEmpty { null },
            values.maxValue.ifEmpty { null },
            SortDirectionFilter.fromName(sortDir)
        )
        Repo.updateFilter(playersListFilter)
    }

    private fun loadInitialValues(): FilterDialogValues {
        val filter = Repo.filtersFlow.value
        return FilterDialogValues(
            PlayerValueFilter.names().indexOf(filter.key.name),
            filter.minValue ?: "",
            filter.maxValue ?: "",
            SortDirectionFilter.names().indexOf(filter.sortDirection.name)
        )
    }

    private fun loadPlayerValues(): List<String> {
        return PlayerValueFilter.names()
    }

    private fun loadSortDirections(): List<String> {
        return SortDirectionFilter.names()
    }
}