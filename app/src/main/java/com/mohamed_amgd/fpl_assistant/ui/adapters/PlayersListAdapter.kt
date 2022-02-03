package com.mohamed_amgd.fpl_assistant.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.mohamed_amgd.fpl_assistant.data.models.Player
import com.mohamed_amgd.fpl_assistant.databinding.PlayersListItemBinding

class PlayersListAdapter(private val players: ArrayList<Player>) :
    RecyclerView.Adapter<PlayersListAdapter.ViewHolder>() {

    class ViewHolder(binding: PlayersListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val root: View = binding.root
        val playerImage: ImageView = binding.playerImage
        val playerName: TextView = binding.playerName
        val playerPosition: TextView = binding.playerPosition
        val playerValue: TextView = binding.playerValue
        val playerTeam: TextView = binding.playerTeamValue
        val playerOwnedBy: TextView = binding.playerOwnedByValue
        val playerPriceChangeGm: TextView = binding.playerPriceChangeGwValue
        val playerPriceChangePrediction: TextView = binding.playerPriceChangePredictionValue
        val playerPointsPerPrice: TextView = binding.playerPointsPerPriceValue
        val playerFixtures: TextView = binding.playerFixturesValue
        val playerNews: TextView = binding.playerNewsValue
        val playerPrice: TextView = binding.playerPriceValue
        val playerForm: TextView = binding.playerFormValue
        val playerStatus: TextView = binding.playerStatusValue
        val extraData: ConstraintLayout = binding.extraData

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: PlayersListItemBinding =
            PlayersListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player: Player = players[position]
        holder.extraData.visibility = View.GONE
        holder.playerName.text = player.playerName
        holder.playerPosition.text = player.position
        holder.playerFixtures.text = player.fixtures
        holder.playerForm.text = player.form
        holder.playerNews.text = player.news
        holder.playerOwnedBy.text = player.ownedBy
        holder.playerPrice.text = player.priceText
        holder.playerPriceChangeGm.text = player.priceChangeGmText
        holder.playerPriceChangePrediction.text = player.priceChangePrediction
        holder.playerPointsPerPrice.text = player.pointsPerPrice.toString()
        holder.playerStatus.text = player.status
        holder.playerTeam.text = player.team
        holder.playerValue.text = player.value

        holder.root.setOnClickListener(View.OnClickListener {
            TransitionManager.beginDelayedTransition(
                it.rootView as ViewGroup,
                AutoTransition()
            )
            if (holder.extraData.visibility == View.VISIBLE) {
                holder.extraData.visibility = View.GONE
                return@OnClickListener
            }
            holder.extraData.visibility = View.VISIBLE
        })
        decidePlayerImageBackground(holder.playerImage, player.status)
        Glide.with(holder.itemView).load(player.imgUrl).into(holder.playerImage)
    }

    private fun decidePlayerImageBackground(playerImage: ImageView, status: String?) {
        var imageBackground = playerImage.background
        imageBackground = DrawableCompat.wrap(imageBackground)
        when (status) {
            "a" -> {
                DrawableCompat.setTint(imageBackground, Color.WHITE)
            }
            "d" -> {
                DrawableCompat.setTint(imageBackground, Color.YELLOW)
            }
            else -> {
                DrawableCompat.setTint(imageBackground, Color.RED)
            }
        }
    }

    override fun getItemCount(): Int {
        return players.size
    }

    fun updateDataSet(list: List<Player>) {
        players.clear()
        players.addAll(list)
        notifyDataSetChanged()
    }
}