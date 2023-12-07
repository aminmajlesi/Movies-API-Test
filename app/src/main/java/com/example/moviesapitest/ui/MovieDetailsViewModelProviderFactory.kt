package com.example.moviesapitest.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviesapitest.repository.MovieDetailsRepository
import com.example.moviesapitest.repository.MovieRepository

class MovieDetailsViewModelProviderFactory(
    val app: Application,
    val movieDetailsRepository: MovieDetailsRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(app,movieDetailsRepository) as T
    }
}