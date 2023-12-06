package com.example.moviesapitest.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "movie_table" ,indices = [Index(value = ["id"], unique = true)])
data class Search(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val Poster: String,
    val Title: String,
    val Type: String,
    val Year: String,
    val imdbID: String
): Serializable