package org.setu.gamebacklog.models

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.setu.gamebacklog.R

class GameAdapter(
    private var games: List<GameModel>,
    private val onEdit: (GameModel) -> Unit,
    private val onDelete: (GameModel) -> Unit
) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val statusText: TextView = itemView.findViewById(R.id.statusText)
        val platformText: TextView = itemView.findViewById(R.id.platformText)
        val ratingBar: RatingBar = itemView.findViewById(R.id.cardRatingBar)
        val playtimeText: TextView = itemView.findViewById(R.id.playtimeText)
        val notesText: TextView = itemView.findViewById(R.id.notesText)
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int){
        val game = games[position]
        val context = holder.itemView.context
        holder.titleText.text = game.title
        holder.statusText.text = game.status.label
        holder.statusText.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(context, statusColor(game.status))
        )
        holder.platformText.text = game.platform.ifBlank {
            context.getString(R.string.platform_unknown)
        }
        holder.ratingBar.rating = game.rating.toFloat()
        holder.playtimeText.text = context.getString(R.string.playtime_hours, game.playtimeHours)

        if (game.notes.isBlank()) {
            holder.notesText.visibility = View.GONE
        } else {
            holder.notesText.visibility = View.VISIBLE
            holder.notesText.text = game.notes
        }
        holder.editButton.setOnClickListener { onEdit(game) }
        holder.deleteButton.setOnClickListener { onDelete(game) }
    }

    override fun getItemCount(): Int = games.size

    fun updateGames(newGames: List<GameModel>) {
        games = newGames
        notifyDataSetChanged()
    }
    private fun statusColor(status: GameStatus): Int = when (status) {
        GameStatus.WISHLIST -> R.color.status_wishlist
        GameStatus.BACKLOG -> R.color.status_backlog
        GameStatus.PLAYING -> R.color.status_playing
        GameStatus.COMPLETED -> R.color.status_completed
        GameStatus.DROPPED -> R.color.status_dropped
    }
}