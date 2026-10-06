package org.setu.gamebacklog.models

data class GameModel (
    var id: Long = 0L,
    var title: String,
    var platform: String,
    var status: GameStatus = GameStatus.BACKLOG,
    var rating: Int = 0,
    var playtimeHours: Int = 0,
    var notes: String = ""
)