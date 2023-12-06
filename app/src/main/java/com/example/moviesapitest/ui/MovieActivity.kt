package com.example.moviesapitest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapitest.adapters.MovieAdapter
import com.example.moviesapitest.databinding.ActivityMoviesBinding
import com.example.moviesapitest.db.MovieDatabase
import com.example.moviesapitest.repository.MovieRepository

class MovieActivity : AppCompatActivity() {

    lateinit var binding: ActivityMoviesBinding
    lateinit var movieAdapter: MovieAdapter
    lateinit var viewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMoviesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_main)



        val movieRepository = MovieRepository(MovieDatabase(this))
        val viewModelProviderFactory = MovieViewModelProviderFactory(application,movieRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MovieViewModel::class.java)

        setupRecyclerView()
        initObservers()
    }


    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter()

        binding.rvMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(this@MovieActivity)
        }

    }



    private fun initObservers() {

        viewModel.movieList.observe(this) { movieList ->
            movieAdapter.differ.submitList(movieList)
            viewModel.saveMoviesList(movieList)
        }

//        viewModel.readMessage.observeOnce(viewLifecycleOwner) { database ->
//            if (database.isNotEmpty()) {
//                Log.d("dataState", "readDatabase called  ")
//                newsAdapter.differ.submitList(database)
//                hideShimmerEffect()
//            }
//            getBreakingNews()
//        }


//        viewModel.movieList.observe(this, Observer { response ->
//            when(response) {
//                is Resource.Success -> {
//                    //hideProgressBar()
//                    response.data?.let { movieResponse ->
//                        movieAdapter.differ.submitList(movieResponse.data.toList())
//                    }
//                }
//                is Resource.Error -> {
//                    //hideProgressBar()
//                    response.search?.let { message ->
//                        Toast.makeText(this, "An error occured: $message", Toast.LENGTH_LONG).show()
//                    }
//                }
//                is Resource.Loading -> {
//                    //showProgressBar()
//                }
//            }
//        })








    }


    private fun getBatmanMovies(){
        viewModel.getBatmanMovies()
    }

//    private fun requestApiData() {
//        Log.d("dataState", "requestApiData called  ")
//        viewModel.movie.observe(viewLifecycleOwner) { response ->
//            when (response) {
//                is Resource.Success -> {
//                    //hideShimmerEffect()
//                    response.data?.let { movieResponse ->
//                        movieAdapter.differ.submitList(movieResponse.Search)
//                    }
//                }
//                is Resource.Error -> {
//                    //hideShimmerEffect()
//                    //loadDataFromCache()
//                    Toast.makeText(
//                        this,
//                        response.search.toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//                is Resource.Loading -> {
//                    //showShimmerEffect()
//                }
//            }
//
//        }
//    }

//    private fun loadDataFromCache() {
//        lifecycleScope.launch {
//            viewModel.readMessage.observe(viewLifecycleOwner) { database ->
//                if (database.isNotEmpty()) {
//                    newsAdapter.differ.submitList(database)
//                }
//            }
//        }
//    }

//    private fun showShimmerEffect() {
//        binding.rvMovies.showShimmer()
//    }
//
//    private fun hideShimmerEffect() {
//        binding.rvMovies.hideShimmer()
//    }

//    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>){
//        observe(lifecycleOwner,object :Observer<T> {
//            override fun onChanged(t: T) {
//                removeObserver(this)
//                observer.onChanged(t)
//            }
//        })
//    }



}