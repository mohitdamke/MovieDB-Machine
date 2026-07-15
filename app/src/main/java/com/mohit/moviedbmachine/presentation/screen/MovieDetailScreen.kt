package com.mohit.moviedbmachine.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mohit.moviedbmachine.presentation.viewmodel.MovieDetailViewModel

@Composable
fun MovieDetailScreen(
    movieId: Int,
    modifier: Modifier = Modifier
) {

    val viewModel: MovieDetailViewModel = hiltViewModel()

    val movie by viewModel.movieDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.getMovieDetail(movieId)
    }

    when {

        isLoading -> {

            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        error != null -> {

            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = error!!)
            }
        }

        movie != null -> {

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Text(text = "Title: ${movie!!.title}")

                Text(text = "Overview: ${movie!!.overview}")

                Text(text = "Release Date: ${movie!!.releaseDate}")

                Text(text = "Runtime: ${movie!!.runtime} min")

                Text(text = "Rating: ⭐ ${movie!!.rating}")

                Text(
                    text = "Genres: ${
                        movie!!.genres.joinToString(", ")
                    }"
                )

                Text(text = "Language: ${movie!!.language}")

                Text(text = "Status: ${movie!!.status}")

                Text(text = "Budget: ${movie!!.budget}")

                Text(text = "Revenue: ${movie!!.revenue}")
            }
        }
    }
}