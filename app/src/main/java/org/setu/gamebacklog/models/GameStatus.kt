package org.setu.gamebacklog.models

enum class GameStatus(val label: String){
    WISHLIST("Wishlist"),
    BACKLOG("Backlog"),
    PLAYING("Playing"),
    COMPLETED("Completed"),
    DROPPED("Dropped")
}