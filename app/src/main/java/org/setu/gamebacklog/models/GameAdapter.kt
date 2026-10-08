package org.setu.gamebacklog.models


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.setu.gamebacklog.R

class GameAdapter(
    private var games: List<GameModel>
) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val detailsText: TextView = itemView.findViewById(R.id.detailsText)
        val statsText: TextView = itemView.findViewById(R.id.statsText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.titleText.text = game.title
        holder.detailsText.text = "Platform: ${game.platform} Status of the Game: ${game.status.label}"
        holder.statsText.text = "Rating of the game: ${game.rating}/5 Playtime: ${game.playtimeHours} hours played"
    }

    override fun getItemCount(): Int = games.size

    fun updateGames(newGames: List<GameModel>) {
        games = newGames
//        notifyDataSetChanged()
    }
}