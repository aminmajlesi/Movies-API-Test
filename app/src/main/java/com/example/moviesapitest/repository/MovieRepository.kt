package com.example.moviesapitest.repository

import com.example.moviesapitest.api.RetrofitInstance
import com.example.moviesapitest.db.MovieDatabase
import com.example.moviesapitest.models.Search

class MovieRepository(
    val db: MovieDatabase
) {
    suspend fun getBatmanMovies() = RetrofitInstance.api.getBatmanMovies()

    suspend fun upsertMovie(search: Search) = db.getMovieDao().upsert(search)

    fun getMoviesFromDB() = db.getMovieDao().getAllMovies()

}