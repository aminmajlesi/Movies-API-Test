package com.example.moviesapitest.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviesapitest.databinding.ActivityMovieDetailsBinding
import com.example.moviesapitest.db.MovieDatabase
import com.example.moviesapitest.models.Rating
import com.example.moviesapitest.repository.MovieDetailsRepository
import com.example.moviesapitest.util.InternetUtils
import com.example.moviesapitest.util.Resource

class MovieDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityMovieDetailsBinding
    lateinit var viewModel: MovieDetailsViewModel
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_movie_details)

        val movieDetailsRepository = MovieDetailsRepository(MovieDatabase(this))
        val viewModelProviderFactory = MovieDetailsViewModelProviderFactory(application,movieDetailsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MovieDetailsViewModel::class.java)

        handleIntent()
        initObservers()

    }

    private fun handleIntent() {
        intent.extras?.apply {
            if (containsKey(EXTRA_VIDEO))
                viewModel.imdbID = this.getString(EXTRA_VIDEO)!!
        }
    }
    private fun initObservers() {
        if (InternetUtils.hasInternetConnection(this)) {
            viewModel.getBatmanMoviesDetails()
            viewModel.movieDetails.observe(this, Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()

                        response.data?.let {
                            setDataOnView(
                                it.Title,response.data.Year,response.data.Rated,
                                response.data.Released,response.data.BoxOffice,response.data.Actors,
                                response.data.Country,response.data.Director, response.data.Genre,
                                response.data.Language,response.data.Plot,response.data.Runtime,
                                response.data.Type,response.data.Website,response.data.Writer,
                                response.data.Ratings,response.data.Poster)
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        response.search?.let { message ->
                            Log.e("movie", "initObservers: $message" )
                            Toast.makeText(this, "An error occured: $message", Toast.LENGTH_LONG).show()
                        }
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            })

            viewModel.movieDetailsList.observe(this) { movieList ->
                viewModel.saveMovieDetailsList(movieList)
            }

        } else {
            loadFromCache()
        }

    }

    private fun loadFromCache() {
        viewModel.getMovieDetailFromDB().observe(this, Observer { filmDB ->
            Log.i("internet","is off")
            if (filmDB != null) {
                setDataOnView(filmDB.Title,filmDB.Year,filmDB.Rated,filmDB.Released
                    ,filmDB.BoxOffice,filmDB.Actors,filmDB.Country,filmDB.Director,
                    filmDB.Genre,filmDB.Language,filmDB.Plot,filmDB.Runtime,
                    filmDB.Type,filmDB.Website,filmDB.Writer,filmDB.Ratings,filmDB.Poster)
            } else {
                Toast.makeText(
                    applicationContext,
                    "there is no data please turn on your internet", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun hideProgressBar() {
        binding.paginationProgressBarDetails.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBarDetails.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setDataOnView(title: String, year: String
                              , rated: String, imdbRating: String, boxOffice: String, actors: String
                              , country: String, director: String, genre: String, language: String,
                              plot: String, runTime: String, type: String, webSite: String, writers: String,
                              rating: List<Rating>, poster: String) {
        binding.tvMovieTitleDetails.text = title
        binding.tvMovieYearDetails.text = year
        binding.tvMovieAgeDetails.text = rated
        binding.tvMovieRateImdbDetails.text = imdbRating
        binding.tvMovieCastDetails.text = actors
        binding.tvMovieCountryDetails.text = country
        binding.tvMovieDirectorDetails.text = director
        binding.tvMovieGenreDetails.text = genre
        binding.tvMovieLanguageDetails.text = language
        binding.tvMovieStoryDetails.text = plot
        binding.tvMovieTimeDetails.text = runTime
        binding.tvMovieWritersDetails.text = writers
        Glide.with(this).load(poster).into(binding.ivMoviePictureDetails)
    }

    companion object {

        private const val EXTRA_VIDEO = "video_extra"
        fun showActivity(context: Context, imdbId: String) {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra(EXTRA_VIDEO, imdbId)
            context.startActivity(intent)
        }
    }

}