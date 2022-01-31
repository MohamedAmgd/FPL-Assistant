package com.mohamed_amgd.fpl_assistant.data.models

data class PlayersListFilter(
    val playerName: String?,
    val key: PlayerValueFilter,
    val minValue: String?,
    val maxValue: String?,
    val sortDirection: SortDirectionFilter
)
