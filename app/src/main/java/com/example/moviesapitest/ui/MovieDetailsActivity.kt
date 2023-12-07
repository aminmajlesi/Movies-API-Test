package com.example.moviesapitest.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_movie_details)

        val movieDetailsRepository = MovieDetailsRepository(MovieDatabase(this))
        val viewModelProviderFactory = MovieDetailsViewModelProviderFactory(application,movieDetailsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MovieDetailsViewModel::class.java)

        initObservers()
    }

//    private fun handleIntent() {
//        intent.extras?.apply {
//            if (containsKey(EXTRA_VIDEO))
//                viewModel.imdbId = this.getString(EXTRA_VIDEO)!!
//        }
//    }
    private fun initObservers() {
        if (InternetUtils.hasInternetConnection(this)){
            viewModel.getBatmanMoviesDetails()
            viewModel.movieDetails.observe(this, Observer { response ->
                when (response) {
                    is Resource.Success -> {

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
                        Toast.makeText(this, "An error occured: $response", Toast.LENGTH_LONG).show()

                    }
                    is Resource.Loading -> {

                    }
                }
            })
        } else {

        }

    }

    private fun setDataOnView(title: String, year: String
                              , rated: String, release: String, boxOffice: String, actors: String
                              , country: String, director: String, genre: String, language: String,
                              plot: String, runTime: String, type: String, webSite: String, writers: String,
                              rating: List<Rating>, poster: String) {
        binding.tvMovieTitleDetails.text = title
        binding.tvMovieYearDetails.text = year
        //binding.tvMovieTitleDetails.text = rated
        //binding.tvMovieTitleDetails.text = release
        //binding.tvMovieTitleDetails.text = boxOffice
        binding.tvMovieCastDetails.text = actors
        binding.tvMovieCountryDetails.text = country
        binding.tvMovieDirectorDetails.text = director
        binding.tvMovieGenreDetails.text = genre
        binding.tvMovieLanguageDetails.text = language
        binding.tvMovieStoryDetails.text = plot
        //binding.tvMovieTitleDetails.text = runTime
        //binding.tvMovieTitleDetails.text = type
        //binding.tvMovieTitleDetails.text = webSite
        //binding.tvMovieTitleDetails.text = writers
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