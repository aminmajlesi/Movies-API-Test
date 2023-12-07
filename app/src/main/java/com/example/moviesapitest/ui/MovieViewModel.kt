package com.example.moviesapitest.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviesapitest.models.MovieResponse
import com.example.moviesapitest.models.Search
import com.example.moviesapitest.repository.MovieRepository
import com.example.moviesapitest.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MovieViewModel(
    app: Application,
    val movieRepository: MovieRepository
): AndroidViewModel(app) {

    val movie: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()

    var movieList: MutableLiveData<List<Search>> = MutableLiveData()

    init {
        getBatmanMovies()
    }

    fun getBatmanMovies() = viewModelScope.launch {
        try {
            val response = movieRepository.getBatmanMovies()
            if (response.isSuccessful) {
                Log.d("dataState", "getBatmanMovies called  ")
                movieList.value = response.body()?.Search
            } else {
                // handle error
            }
        } catch (e: Exception) {
        }
    }

    fun saveMovies(search: Search) = viewModelScope.launch {
        movieRepository.upsertMovie(search)
    }

    fun saveMoviesList(moviesList: List<Search>?) {
        if (moviesList != null) {
            for (movie in moviesList) {
                saveMovies(movie)
            }
        }
    }

    fun getMoviesFromDB() : LiveData<List<Search>> {
        return movieRepository.getMoviesFromDB()
    }



    private fun handleMoviesResponse(response: Response<MovieResponse>) : Resource<MovieResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }




}