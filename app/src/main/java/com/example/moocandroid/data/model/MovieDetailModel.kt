package com.example.moocandroid.data.model

import com.google.gson.annotations.SerializedName

data class MovieDetailModel(
    @SerializedName("adult")
    val adult: Boolean?,
    @SerializedName("backdrop_path")
    val backdrop_path: String?,
    @SerializedName("belongs_to_collection")
    val belongs_to_collection: Any?,
    @SerializedName("budget")
    val budget: Double?,
    @SerializedName("genres")
    val genres: ArrayList<Genre>?,
    @SerializedName("homepage")
    val homepage: String?,
    @SerializedName("id")
    val id: Double?,
    @SerializedName("imdb_id")
    val imdb_id: String?,
    @SerializedName("original_language")
    val original_language: String?,
    @SerializedName("original_title")
    val original_title: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("popularity")
    val popularity: Double?,
    @SerializedName("poster_path")
    val poster_path: String?,
    @SerializedName("production_companies")
    val production_companies: ArrayList<ProductionCompany>?,
    @SerializedName("production_countries")
    val production_countries: ArrayList<ProductionCountry>?,
    @SerializedName("release_date")
    val release_date: String?,
    @SerializedName("revenue")
    val revenue: Double?,
    @SerializedName("runtime")
    val runtime: Double?,
    @SerializedName("spoken_languages")
    val spoken_languages: ArrayList<SpokenLanguage>?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("tagline")
    val tagline: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("video")
    val video: Boolean?,
    @SerializedName("vote_average")
    val vote_average: Double?,
    @SerializedName("vote_count")
    val vote_count: Double
) {
    data class Genre(
        @SerializedName("id")
        val id: Double?,
        @SerializedName("name")
        val name: String
    )

    data class ProductionCompany(
        @SerializedName("id")
        val id: Double?,
        @SerializedName("logo_path")
        val logo_path: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("origin_country")
        val origin_country: String
    )

    data class ProductionCountry(
        @SerializedName("iso_3166_1")
        val iso_3166_1: String?,
        @SerializedName("name")
        val name: String
    )

    data class SpokenLanguage(
        @SerializedName("english_name")
        val english_name: String?,
        @SerializedName("iso_639_1")
        val iso_639_1: String?,
        @SerializedName("name")
        val name: String
    )
}