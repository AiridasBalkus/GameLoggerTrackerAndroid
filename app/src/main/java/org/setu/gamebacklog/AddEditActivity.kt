package org.setu.gamebacklog

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.setu.gamebacklog.models.GameModel
import org.setu.gamebacklog.models.GameStatus

class AddEditActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var platformInput: EditText
    private lateinit var statusSpinner: Spinner
    private lateinit var ratingBar: RatingBar
    private lateinit var playtimeInput: EditText
    private lateinit var notesInput: EditText
    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_edit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        titleInput = findViewById(R.id.titleInput)
        platformInput = findViewById(R.id.platformInput)
        statusSpinner = findViewById(R.id.statusSpinner)
        ratingBar = findViewById(R.id.ratingBar)
        playtimeInput = findViewById(R.id.playTimeInput)
        notesInput = findViewById(R.id.notesInput)

        setupStatusSpinner()

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            saveGame()
        }
        findViewById<Button>(R.id.cancelButton).setOnClickListener {
            finish()
        }
        //placeholder for CA2
        findViewById<Button>(R.id.coverImageButton).setOnClickListener {
            Toast.makeText(this, R.string.coming_soon, Toast.LENGTH_SHORT).show()
        }

        editingId = intent.getLongExtra("id", -1L).takeIf { it != -1L }

        editingId?.let { id ->
            findViewById<TextView>(R.id.formTitle).setText(R.string.edit_game_title)
            findViewById<Button>(R.id.saveButton).setText(R.string.save_changes)
            loadExistingGame(id)
        }
    }
    private fun setupStatusSpinner(){
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, GameStatus.entries.map { it.label }
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = spinnerAdapter
        statusSpinner.setSelection(GameStatus.BACKLOG.ordinal)
    }
    private fun loadExistingGame(id: Long) {
        val game = AppData.games.findOne(id)
        if (game == null) {
            Toast.makeText(this, R.string.game_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        titleInput.setText(game.title)
        platformInput.setText(game.platform)
        statusSpinner.setSelection(game.status.ordinal)
        ratingBar.rating = game.rating.toFloat()
        playtimeInput.setText(game.playtimeHours.toString())
        notesInput.setText(game.notes)
    }
    private fun saveGame(){
        val title = titleInput.text.toString().trim()

        if(title.isEmpty()){
            titleInput.error = getString(R.string.error_title_required)
            return
        }
        val playtimeText = playtimeInput.text.toString().trim()
        val playtime = if(playtimeText.isEmpty()) 0 else playtimeText.toIntOrNull()

        if(playtime == null){
            playtimeInput.error = getString(R.string.error_playtime_invalid)
            return
        }
        val game = GameModel(
            title = title,
            platform = platformInput.text.toString().trim(),
            status = GameStatus.entries[statusSpinner.selectedItemPosition],
            rating = ratingBar.rating.toInt(),
            playtimeHours = playtime,
            notes = notesInput.text.toString().trim()
        )
        val currentId = editingId
        if(currentId == null){
            AppData.games.create(game)
            Toast.makeText(this, R.string.game_added, Toast.LENGTH_SHORT).show()
        }else{
            game.id = currentId
            if(AppData.games.update(game)){
                Toast.makeText(this, R.string.game_updated, Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, R.string.game_not_found, Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }
}