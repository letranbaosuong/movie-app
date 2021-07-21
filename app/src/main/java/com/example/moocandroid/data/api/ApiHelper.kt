package com.example.moocandroid.data.api

class ApiHelper(private val apiService: ApiService) {

    suspend fun getMovies(type: String?, language: String?, page: Int?) =
        apiService.getMovies(type = type, language = language, page = page)

    suspend fun getMovieById(id: Int?, language: String?) =
        apiService.getMovieById(id = id, language = language)
}