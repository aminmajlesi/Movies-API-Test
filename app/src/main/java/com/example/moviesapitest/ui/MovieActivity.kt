package com.example.moviesapitest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapitest.adapters.MovieAdapter
import com.example.moviesapitest.databinding.ActivityMoviesBinding
import com.example.moviesapitest.db.MovieDatabase
import com.example.moviesapitest.repository.MovieRepository
import com.example.moviesapitest.util.InternetUtils
import com.example.moviesapitest.util.Resource
import kotlinx.coroutines.launch

class MovieActivity : AppCompatActivity() {

    lateinit var binding: ActivityMoviesBinding
    lateinit var movieAdapter: MovieAdapter
    lateinit var viewModel: MovieViewModel
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMoviesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_main)

        val movieRepository = MovieRepository(MovieDatabase(this))
        val viewModelProviderFactory = MovieViewModelProviderFactory(application, movieRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MovieViewModel::class.java)

        setupRecyclerView()
        initObservers()
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(
            onClick = {
                MovieDetailsActivity.showActivity(this@MovieActivity, it.imdbID!!)
            }
        )
        binding.rvMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(this@MovieActivity)
        }
    }


    private fun initObservers() {

        if (InternetUtils.hasInternetConnection(this)) {
            getBatmanMovies()
            viewModel.movie.observe(this, Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { movieResponse ->
                            movieAdapter.differ.submitList(movieResponse.Search)
                        }
                    }

                    is Resource.Error -> {
                        hideProgressBar()
                        response.search?.let { message ->
                            Toast.makeText(this, "An error occured: $message", Toast.LENGTH_LONG)
                                .show()
                        }

                    }

                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            })


            viewModel.movieList.observe(this) { movieList ->
                movieAdapter.differ.submitList(movieList)
                viewModel.saveMoviesList(movieList)
            }

        } else {
            loadDataFromCache()
        }

    }

    private fun getBatmanMovies() {
        viewModel.getBatmanMovies()
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            viewModel.getMoviesFromDB().observe(this@MovieActivity) { database ->
                if (database.isNotEmpty()) {
                    movieAdapter.differ.submitList(database)
                }
            }
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }


}