package com.example.moocandroid.data.repository

import com.example.moocandroid.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getMovies(type: String?, language: String?, page: Int?) =
        apiHelper.getMovies(type = type, language = language, page = page)

    suspend fun getMovieById(id: Int?, language: String?) =
        apiHelper.getMovieById(id = id, language = language)
}