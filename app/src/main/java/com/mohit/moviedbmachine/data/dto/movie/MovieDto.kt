package com.mohit.moviedbmachine.data.dto.movie

data class MovieDto(
    val id: Int?,
    val title: String?,
    val overview: String?,
    val poster_path: String?,
    val backdrop_path: String?,
    val release_date: String?,
    val vote_average: Double?,
)