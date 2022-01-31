package com.mohamed_amgd.fpl_assistant.data.models

sealed class PlayerValueFilter(val key: String, val name: String) {
    object Price : PlayerValueFilter("price", "Price")
    object OwnedBy : PlayerValueFilter("owned_by", "Owned By")
    object Position : PlayerValueFilter("position", "Position")
    object PriceChangeGW : PlayerValueFilter("cost_change_event", "Price Change Gameweek")
    object PriceChangePrediction :
        PlayerValueFilter("cost_change_prediction", "Price Change Prediction")

    object PointsPerPrice : PlayerValueFilter("points_per_price", "Points Per Price")
    object Form : PlayerValueFilter("form", "Form")
    object TotalPoints : PlayerValueFilter("total_points", "Total Points")

    companion object {
        fun names(): List<String> {
            val result: ArrayList<String> = ArrayList()
            PlayerValueFilter::class.sealedSubclasses.forEach {
                it.objectInstance?.name?.let { name ->
                    result.add(name)
                }
            }
            return result
        }

        fun fromName(name: String): PlayerValueFilter {
            return PlayerValueFilter::class.sealedSubclasses.firstOrNull {
                it.objectInstance?.name == name
            }?.objectInstance ?: Price
        }
    }
}
