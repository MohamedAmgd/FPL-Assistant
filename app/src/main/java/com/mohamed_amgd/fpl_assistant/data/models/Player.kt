package com.mohamed_amgd.fpl_assistant.data.models

import com.google.gson.annotations.SerializedName


data class Player(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("second_name") var secondName: String? = null,
    @SerializedName("team") var team: String? = null,
    @SerializedName("position") var position: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("owned_by") var ownedBy: String? = null,
    @SerializedName("price") var price: Double? = null,
    @SerializedName("cost_change_event") var priceChangeGameweek: Double? = null,
    @SerializedName("cost_change_prediction") var priceChangePrediction: String? = null,
    @SerializedName("fixtures") var fixtures: String? = null,
    @SerializedName("img_url") var imgUrl: String? = null,
    @SerializedName("dreamteam_count") var dreamteamCount: Int? = null,
    @SerializedName("in_dreamteam") var inDreamteam: Boolean? = null,
    @SerializedName("form") var form: String? = null,
    @SerializedName("chance_of_playing_this_round") var chanceOfPlayingThisRound: Int? = null,
    @SerializedName("chance_of_playing_next_round") var chanceOfPlayingNextRound: Int? = null,
    @SerializedName("news") var news: String? = null,
    @SerializedName("total_points") var totalPoints: Int? = null,
    @SerializedName("points_per_price") var pointsPerPrice: Double? = null
) {
    lateinit var playerName: String
    lateinit var priceText: String
    lateinit var priceChangeGmText: String
    lateinit var value: String
}