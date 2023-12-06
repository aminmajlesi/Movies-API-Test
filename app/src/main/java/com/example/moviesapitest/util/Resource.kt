package com.example.moviesapitest.util

sealed class Resource <T>(
    val data: T? = null,
    val search: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(search: String, data: T? = null) : Resource<T>(data, search)
    class Loading<T> : Resource<T>()
}