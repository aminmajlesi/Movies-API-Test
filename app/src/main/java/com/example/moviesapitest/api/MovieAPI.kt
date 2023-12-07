package com.example.moviesapitest.api

import com.example.moviesapitest.models.MovieDetailsResponse
import com.example.moviesapitest.models.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieAPI {

    @GET("?apikey=3e974fca&s=batman")
    suspend fun getBatmanMovies(

    ): Response<MovieResponse>

    @GET("?apikey=3e974fca&i={imdbID}")
    suspend fun getBatmanMoviesDetails(
        @Path("imdbID")
        imdbID: String
    ): Response<MovieDetailsResponse>
}