package com.mohamed_amgd.fpl_assistant.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed_amgd.fpl_assistant.data.Repo
import com.mohamed_amgd.fpl_assistant.data.models.Player
import com.mohamed_amgd.fpl_assistant.ui.intents.MainIntent
import com.mohamed_amgd.fpl_assistant.ui.viewStates.MainViewState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    val intentChannel = Channel<MainIntent>(Channel.UNLIMITED)
    private val _stateFlow = MutableStateFlow<MainViewState>(MainViewState.Idle)
    val state: StateFlow<MainViewState>
        get() = _stateFlow

    init {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect {
                processIntent(it)
            }
        }
        viewModelScope.launch {
            Repo.filtersFlow.collect {
                _stateFlow.value = MainViewState.FilterKey(it.key.name)
            }
        }
    }

    private suspend fun processIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.FetchPlayersList -> {
                _stateFlow.value = MainViewState.Loading
                _stateFlow.value = try {
                    MainViewState.PlayersList(loadPlayers())
                } catch (e: Exception) {
                    MainViewState.Error(e.localizedMessage)
                }
            }
            is MainIntent.FilterPlayersByName -> {
                _stateFlow.value = MainViewState.Loading
                _stateFlow.value = try {
                    MainViewState.PlayersList(filterPlayersByName(intent.name))
                } catch (e: Exception) {
                    MainViewState.Error(e.localizedMessage)
                }
            }
            is MainIntent.FetchFilterKey -> {
                _stateFlow.value = MainViewState.Loading
                _stateFlow.value = try {
                    MainViewState.FilterKey(loadFilterKey())
                } catch (e: Exception) {
                    MainViewState.Error(e.localizedMessage)
                }
            }
        }
    }

    private fun loadFilterKey(): String {
        return Repo.filtersFlow.value.key.name
    }

    private suspend fun loadPlayers(): List<Player> {
        return Repo.fetchPlayers()
    }

    private fun filterPlayersByName(playerName: String): List<Player> {
        return Repo.filterPLayersByName(playerName)
    }

}