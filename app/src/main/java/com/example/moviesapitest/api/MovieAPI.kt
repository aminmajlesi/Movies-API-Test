package com.example.moviesapitest.api

import com.example.moviesapitest.models.MovieDetailsResponse
import com.example.moviesapitest.models.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {

    @GET("?apikey=3e974fca&s=batman")
    suspend fun getBatmanMovies(

    ): Response<MovieResponse>

    @GET(".")
    suspend fun getBatmanMoviesDetails(
        @Query("apikey")
        apikey: String = "3e974fca",
        @Query("i")
        imdbID: String,
    ): Response<MovieDetailsResponse>
}