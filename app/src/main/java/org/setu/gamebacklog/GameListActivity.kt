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
import org.setu.gamebacklog.models.GameModel

class GameListActivity : AppCompatActivity() {
    private lateinit var adapter: GameAdapter
    private lateinit var emptyText: TextView
    private fun editGame(game: GameModel){
        val intent = Intent(this, AddEditActivity::class.java)
        intent.putExtra("id", game.id)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_list)

        emptyText = findViewById(R.id.emptyText)

        adapter = GameAdapter(games = AppData.games.findAll(), onEdit = {game -> editGame(game)})

        val recyclerView = findViewById<RecyclerView>(R.id.gamesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.returnButton).setOnClickListener {
            finish()
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
}