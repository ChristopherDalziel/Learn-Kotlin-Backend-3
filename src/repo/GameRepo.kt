package com.example.repo

import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicInteger

// This repo object will be where we store our state - our logic will also be where we define our logic to add, remove etc
object GameRepo {
    // Incrementing ID
    private val idCounter =  AtomicInteger()
    // Database
    private val games = CopyOnWriteArraySet<Game>()

    // Allows us to insert a new Game, inputs/outputs a game type
    fun add(g: Game): Game {
        if(games.contains(g)) {
            return games.find {it == g} !!
        }
        g.id = idCounter.incrementAndGet()
        games.add(g)
        return g
    }

    fun get(id: String): Game = games.find {it.id.toString() == id} ?: throw IllegalArgumentException("No entity found for $id")

    // If the user passes in an Int id we change it to a string and pass it to the above get function retaining our error handling
    fun get(id: Int): Game = get(id.toString())

    fun getAll(): List<Game> = games.toList()

    // Remove an entire Game by passing in an existing Game
    fun remove(g: Game) {
        // If Game doesn't exist throw error
        if(!games.contains(g)) {
            throw IllegalArgumentException("Game not stored in repo")
        }
        games.remove(g)
    }

    // Using the ID get the Game object, and then remove it
    fun remove(id: String): Boolean = games.remove(get(id))

    // The same as the previous remove but using an Int ID
    fun remove(id: Int): Boolean = games.remove(get(id))

    // Remove all Games from the game array set
    fun clear(): Unit = games.clear()
}