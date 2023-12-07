package com.example.moviesapitest.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesapitest.models.MovieDetailsResponse
import com.example.moviesapitest.models.Search
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(search: Search): Long

    @Query("SELECT * FROM movie_table")
    fun getAllMovies(): LiveData<List<Search>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetailMovie(movieDetail: MovieDetailsResponse)

    @Query("SELECT * FROM movie_details_table WHERE imdbID = :imdbID")
    fun getAllDetailMovie(imdbID: String) : LiveData<MovieDetailsResponse>


}