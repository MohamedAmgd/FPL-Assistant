package com.mohamed_amgd.fpl_assistant.util

import com.mohamed_amgd.fpl_assistant.data.models.Player
import com.mohamed_amgd.fpl_assistant.data.models.PlayerValueFilter

object Util {
    fun completePlayerData(player: Player, filter: PlayerValueFilter) {
        player.playerName = "${player.firstName} ${player.secondName}"
        player.priceText = "${player.price} m"
        player.priceChangeGmText = "${player.priceChangeGameweek} m"
        player.priceChangePrediction = "${player.priceChangePrediction} %"
        player.ownedBy = "${player.ownedBy} %"
        player.value = getPlayerValue(player, filter)
    }

    private fun getPlayerValue(player: Player, filter: PlayerValueFilter): String {
        return when (filter) {
            is PlayerValueFilter.Price -> player.priceText
            is PlayerValueFilter.Form -> "${player.form}"
            is PlayerValueFilter.OwnedBy -> "${player.ownedBy}"
            is PlayerValueFilter.PointsPerPrice -> "${player.pointsPerPrice}"
            is PlayerValueFilter.Position -> "${player.position}"
            is PlayerValueFilter.PriceChangeGW -> player.priceChangeGmText
            is PlayerValueFilter.PriceChangePrediction -> "${player.priceChangePrediction}"
            is PlayerValueFilter.TotalPoints -> "${player.totalPoints}"
        }
    }
}