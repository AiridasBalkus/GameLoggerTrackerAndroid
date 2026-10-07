package org.setu.gamebacklog.main

import org.setu.gamebacklog.models.GameMemStore
import org.setu.gamebacklog.models.GameStore

object AppData {
    val games: GameStore = GameMemStore()
}