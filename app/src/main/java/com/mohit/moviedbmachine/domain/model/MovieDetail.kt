package com.mohit.moviedbmachine.domain.model

data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val runtime: Int?,
    val rating: Double,
    val genres: List<String>,
    val budget: Long,
    val revenue: Long,
    val status: String,
    val tagline: String?,
    val language: String
)
