package com.mohamed_amgd.fpl_assistant.ui.viewStates

import com.mohamed_amgd.fpl_assistant.data.models.Player

sealed class MainViewState {
    object Idle : MainViewState()
    object Loading : MainViewState()
    data class PlayersList(val players: List<Player>) : MainViewState()
    data class FilterKey(val key: String) : MainViewState()
    data class Error(val error: String?) : MainViewState()
}
