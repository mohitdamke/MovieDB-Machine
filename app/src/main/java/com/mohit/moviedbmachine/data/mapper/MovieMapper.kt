package com.mohit.moviedbmachine.data.mapper

import com.mohit.moviedbmachine.data.dto.movie.MovieDto
import com.mohit.moviedbmachine.domain.model.Movie

fun MovieDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = poster_path,
        backdropPath = backdrop_path,
        releaseDate = release_date,
        rating = vote_average

    )
}