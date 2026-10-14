package org.setu.gamebacklog.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.setu.gamebacklog.AddEditActivity
import org.setu.gamebacklog.AppData
import org.setu.gamebacklog.GameListActivity
import org.setu.gamebacklog.R
import org.setu.gamebacklog.models.GameStatus
import android.widget.TextView
import android.widget.Toast
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.viewGamesButton).setOnClickListener {
            startActivity(Intent(this, GameListActivity::class.java))
        }
        findViewById<Button>(R.id.addGameButton).setOnClickListener {
            startActivity(Intent(this, AddEditActivity::class.java))
        }
        //placeholder for ca2
        findViewById<Button>(R.id.statsButton).setOnClickListener { showComingSoon() }
        findViewById<Button>(R.id.settingsButton).setOnClickListener { showComingSoon() }
        findViewById<ImageButton>(R.id.profileButton).setOnClickListener {
            Toast.makeText(this, R.string.accounts_coming_soon, Toast.LENGTH_SHORT).show()
        }
    }
    override fun onResume() {
        super.onResume()
        updateStats()
    }
    private fun updateStats() {
        val games = AppData.games.findAll()
        findViewById<TextView>(R.id.statTotalCount).text = games.size.toString()
        findViewById<TextView>(R.id.statPlayingCount).text =
            games.count { it.status == GameStatus.PLAYING }.toString()
        findViewById<TextView>(R.id.statCompletedCount).text =
            games.count { it.status == GameStatus.COMPLETED }.toString()
    }
    private fun showComingSoon() {
        Toast.makeText(this, R.string.coming_soon, Toast.LENGTH_SHORT).show()
    }
}