package com.mohit.moviedbmachine.data.mapper

import com.mohit.moviedbmachine.data.dto.detail.MovieDetailsResponse
import com.mohit.moviedbmachine.domain.model.MovieDetail

fun MovieDetailsResponse.toMovieDetail(): MovieDetail{

    return MovieDetail(

        id = id,
        title = title,
        overview = overview,
        posterPath = poster_path,
        backdropPath = backdrop_path,
        releaseDate = release_date,
        runtime = runtime,
        rating = vote_average,
        genres = genres.map { it.name },
        budget = budget,
        revenue = revenue,
        status = status,
        tagline = tagline,
        language = original_language

    )



}