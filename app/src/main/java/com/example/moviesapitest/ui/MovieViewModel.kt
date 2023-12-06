package com.example.moviesapitest.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
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
                Log.d("dataState", "getBreakingNews called  ")
                movieList.value = response.body()?.Search
            } else {
                // handle error
            }
        } catch (e: Exception) {
        }
    }

    fun saveMovies(search: Search) = viewModelScope.launch {
        movieRepository.upsert(search)
    }

    fun saveMoviesList(moviesList: List<Search>?) {
        if (moviesList != null) {
            for (movie in moviesList) {
                saveMovies(movie)
            }
        }
    }



    private fun handleMoviesResponse(response: Response<MovieResponse>) : Resource<MovieResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }


}