package org.setu.gamebacklog.models

import java.util.concurrent.atomic.AtomicLong

class GameMemStore : GameStore {
    private val games = ArrayList<GameModel>()
    private val lastId = AtomicLong(0L)

    override fun findAll(): List<GameModel>{
        return games
    }
    override fun findOne(id: Long): GameModel? {
        return games.find{g -> g.id == id}
    }
    override fun create(game: GameModel) {
        game.id = lastId.incrementAndGet()
        games.add(game)
    }

    override fun update(game: GameModel): Boolean {
        val foundGame = findOne(game.id)
        return if (foundGame != null) {
            foundGame.title = game.title
            foundGame.platform = game.platform
            foundGame.status = game.status
            foundGame.rating = game.rating
            foundGame.playtimeHours = game.playtimeHours
            foundGame.notes = game.notes
            true
        }else{
            false
        }
    }

    override fun delete(id: Long): Boolean {
        val foundGame = findOne(id)
        return if(foundGame != null){
            games.remove(foundGame)
            true
        }else{
            false
        }

    }
}