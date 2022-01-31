package com.mohamed_amgd.fpl_assistant.ui.intents

sealed class MainIntent {
    object FetchPlayersList : MainIntent()
    object FetchFilterKey : MainIntent()
    data class FilterPlayersByName(val name: String) : MainIntent()
}
