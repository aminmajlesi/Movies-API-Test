package com.example.moviesapitest.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviesapitest.models.MovieDetailsResponse
import com.example.moviesapitest.repository.MovieDetailsRepository
import com.example.moviesapitest.util.Resource
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    app: Application,
    val movieDetailsRepository: MovieDetailsRepository
): AndroidViewModel(app) {

    lateinit var imdbID: String

    val movieDetails: MutableLiveData<Resource<MovieDetailsResponse>> = MutableLiveData()
    var movieDetailsList: MutableLiveData<List<MovieDetailsResponse>> = MutableLiveData()

    fun getBatmanMoviesDetails() = viewModelScope.launch {
        try {
            val response = movieDetailsRepository.getBatmanMoviesDetails(imdbID)
            if (response.isSuccessful) {
                Log.d("dataState", "getBatmanMovies called  ")
                //movieDetails.value = response.body()?
                movieDetails.postValue(Resource.Success(response.body()!!))
            } else {
                // handle error
                movieDetails.postValue(Resource.Error("Error occurred"))
            }
        } catch (e: Exception) {
            movieDetails.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    fun saveMovieDetails(search: MovieDetailsResponse) = viewModelScope.launch {
        movieDetailsRepository.upsertMovieDetails(search)
    }

    fun saveMovieDetailsList(moviesList: List<MovieDetailsResponse>?) {
        if (moviesList != null) {
            for (movie in moviesList) {
                saveMovieDetails(movie)
            }
        }
    }

    fun getMovieDetailFromDB(imdbID: String) : LiveData<MovieDetailsResponse> {
        return movieDetailsRepository.getMoviesDetailsFromDB(imdbID)
    }

}