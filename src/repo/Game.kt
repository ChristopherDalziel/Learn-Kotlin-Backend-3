package com.example.repo

data class Game(val name: String, val releaseYear: Int, val rating: String, val developer: String) {
    // Like a database we want each object to have a ID, it will automatically increment with each new object we add.
    var id: Int? = null
}