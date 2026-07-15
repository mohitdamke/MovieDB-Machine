package com.mohit.moviedbmachine.data.dto.movie

data class MovieDto(
//    val adult: Boolean?,
//    val genre_ids: List<Int>?,
//    val original_language: String?,
//    val original_title: String?,
//    val video: Boolean?,
//    val vote_count: Int?
//    val popularity: Double?,
    val id: Int?,
    val title: String?,
    val overview: String?,
    val poster_path: String?,
    val backdrop_path: String?,
    val release_date: String?,
    val vote_average: Double?,
)