package com.example.moviesapitest.repository

import com.example.moviesapitest.api.RetrofitInstance
import com.example.moviesapitest.db.MovieDatabase
import com.example.moviesapitest.models.MovieDetailsResponse

class MovieDetailsRepository(
    val db: MovieDatabase
) {
    suspend fun getBatmanMoviesDetails(imdbID: String) = RetrofitInstance.api.getBatmanMoviesDetails(imdbID = imdbID)

    suspend fun upsertMovieDetails(movieDetailsResponse: MovieDetailsResponse) = db.getMovieDao().insertDetailMovie(movieDetailsResponse)

    fun getMoviesDetailsFromDB(imdbID: String) = db.getMovieDao().getAllDetailMovie(imdbID = imdbID)

}