package com.example.moviesapitest.models

data class MovieResponse(
    val Response: String,
    val Search: List<Search>,
    val totalResults: String
)