package com.example.moviesapitest.db

import androidx.room.TypeConverter
import com.example.moviesapitest.models.MovieDetailsResponse
import com.example.moviesapitest.models.Rating
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromRatingList(ratings: List<Rating>?): String? {
        return gson.toJson(ratings)
    }

    @TypeConverter
    fun toRatingList(ratingsJson: String?): List<Rating>? {
        val type = object : TypeToken<List<Rating>>() {}.type
        return gson.fromJson(ratingsJson, type)
    }

}