package com.example.moviesapitest.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie_table" ,indices = [Index(value = ["imdbID"], unique = true)])
data class Search(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val Poster: String,
    val Title: String,
    val Type: String,
    val Year: String,
    val imdbID: String
)