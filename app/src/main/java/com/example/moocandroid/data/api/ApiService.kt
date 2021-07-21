package com.example.moocandroid.data.api

import com.example.moocandroid.data.model.MovieDetailModel
import com.example.moocandroid.data.model.MovieModel
import com.example.moocandroid.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Path: now_playing, popular
    @GET("movie/{type}?api_key=${Constants.apiKey}")
    suspend fun getMovies(
        @Path("type") type: String?,
        @Query("language") language: String?,
        @Query("page") page: Int?,
    ): MovieModel

    @GET("movie/{id}?api_key=${Constants.apiKey}")
    suspend fun getMovieById(
        @Path("id") id: Int?,
        @Query("language") language: String?,
    ): MovieDetailModel
}