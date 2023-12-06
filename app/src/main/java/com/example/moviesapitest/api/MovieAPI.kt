package com.example.moviesapitest.api

import com.example.moviesapitest.models.MovieResponse
import retrofit2.Response
import retrofit2.http.GET

interface MovieAPI {

    @GET("?apikey=3e974fca&s=batman")
    suspend fun getBatmanMovies(

    ): Response<MovieResponse>
}