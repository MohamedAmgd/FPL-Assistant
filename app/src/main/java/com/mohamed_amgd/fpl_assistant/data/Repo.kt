package com.mohamed_amgd.fpl_assistant.data

import com.mohamed_amgd.fpl_assistant.data.models.Player
import com.mohamed_amgd.fpl_assistant.data.models.PlayerValueFilter
import com.mohamed_amgd.fpl_assistant.data.models.PlayersListFilter
import com.mohamed_amgd.fpl_assistant.data.models.SortDirectionFilter
import com.mohamed_amgd.fpl_assistant.data.retrofit.RetrofitHelper
import com.mohamed_amgd.fpl_assistant.util.Util
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Repo {
    private val defaultFilter = PlayersListFilter(
        null, PlayerValueFilter.Price, null, null,
        SortDirectionFilter.Descending
    )
    private val _playersFlow = MutableStateFlow<List<Player>>(emptyList())
    private val _filtersFlow = MutableStateFlow(defaultFilter)

    val filtersFlow: StateFlow<PlayersListFilter> get() = _filtersFlow

    suspend fun fetchPlayers(): List<Player> {
        val key = _filtersFlow.value.key.key
        val minValue = _filtersFlow.value.minValue
        val maxValue = _filtersFlow.value.maxValue
        val sortDir = _filtersFlow.value.sortDirection.key
        val response = RetrofitHelper().getInstance().getPlayers(key, minValue, maxValue, sortDir)
        if (response.isSuccessful) {
            response.body()?.forEach { player ->
                Util.completePlayerData(player, _filtersFlow.value.key)
            }
            _playersFlow.value = response.body() ?: emptyList()
            return response.body() ?: emptyList()
        }
        throw Exception(response.code().toString())
    }

    fun filterPLayersByName(name: String): List<Player> {
        return _playersFlow.value.filter { player ->
            player.playerName.lowercase().contains(name.lowercase())
        }
    }

    fun updateFilter(filter: PlayersListFilter) {
        _filtersFlow.value = filter
    }
}