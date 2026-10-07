package org.setu.gamebacklog

import org.setu.gamebacklog.models.GameMemStore
import org.setu.gamebacklog.models.GameStore

object AppData {
    val games: GameStore = GameMemStore()
}