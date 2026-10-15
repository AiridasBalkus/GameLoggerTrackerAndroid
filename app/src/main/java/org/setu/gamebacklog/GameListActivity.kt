package org.setu.gamebacklog

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.setu.gamebacklog.models.GameAdapter
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.setu.gamebacklog.models.GameModel
import com.google.android.material.chip.ChipGroup

class GameListActivity : AppCompatActivity() {
    private lateinit var adapter: GameAdapter
    private lateinit var emptyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_list)

        emptyText = findViewById(R.id.emptyText)

        adapter = GameAdapter(games = AppData.games.findAll(), onEdit = {game -> editGame(game)}, onDelete = {game -> confirmDelete(game)})

        val recyclerView = findViewById<RecyclerView>(R.id.gamesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.returnButton).setOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.listAddButton).setOnClickListener {
            startActivity(Intent(this, AddEditActivity::class.java))
        }
        //another placeholder for CA2
        findViewById<Button>(R.id.sortButton).setOnClickListener { showComingSoon() }
        findViewById<ChipGroup>(R.id.filterChips).setOnCheckedStateChangeListener { _, _ ->
            showComingSoon()
        }
        refreshList()
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            refreshList()
        }
    }

    private fun refreshList() {
        val games = AppData.games.findAll()
        adapter.updateGames(games)
        emptyText.visibility = if (games.isEmpty()) View.VISIBLE else View.GONE
    }
    private fun editGame(game: GameModel) {
        val intent = Intent(this, AddEditActivity::class.java)
        intent.putExtra("id", game.id)
        startActivity(intent)
    }
    private fun confirmDelete(game: GameModel){
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_dialog_title)
            .setMessage(getString(R.string.delete_dialog_message, game.title))
            .setPositiveButton(R.string.delete) { _, _ ->
                deleteGame(game)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
    private fun deleteGame(game: GameModel){
        if(AppData.games.delete(game.id)){
            refreshList()
            Toast.makeText(this, getString(R.string.game_deleted, game.title), Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, R.string.game_not_found, Toast.LENGTH_SHORT).show()
        }
    }
    private fun showComingSoon() {
        Toast.makeText(this, R.string.coming_soon, Toast.LENGTH_SHORT).show()
    }
}