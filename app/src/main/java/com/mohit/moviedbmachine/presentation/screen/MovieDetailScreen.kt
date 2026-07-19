package com.mohit.moviedbmachine.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohit.moviedbmachine.presentation.viewmodel.MovieDetailViewModel

@Composable
fun MovieDetailScreen(
    movieId: Int,
    modifier: Modifier = Modifier
) {

    val viewModel: MovieDetailViewModel = hiltViewModel()

    val movie by viewModel.movieDetail.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    LaunchedEffect(movieId) {
        viewModel.getMovieDetail(movieId)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        when {

            isLoading -> {

                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

            }

            error != null -> {

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(error!!)

                    Button(
                        onClick = {
                            viewModel.retry()
                        }
                    ) {
                        Text("Retry")
                    }

                }

            }

            movie != null -> {

                val detail = movie!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    MovieInfo("Title", detail.title.orEmpty())

                    MovieInfo("Overview", detail.overview.orEmpty())

                    MovieInfo("Release Date", detail.releaseDate.orEmpty())

                    MovieInfo(
                        "Runtime",
                        "${detail.runtime ?: 0} min"
                    )

                    MovieInfo(
                        "Rating",
                        "⭐ ${detail.rating}"
                    )

                    MovieInfo(
                        "Genres",
                        detail.genres.joinToString()
                    )

                    MovieInfo(
                        "Language",
                        detail.language.orEmpty()
                    )

                    MovieInfo(
                        "Status",
                        detail.status.orEmpty()
                    )

                    MovieInfo(
                        "Budget",
                        detail.budget.toString()
                    )

                    MovieInfo(
                        "Revenue",
                        detail.revenue.toString()
                    )

                }

            }

            else -> {

                Text(
                    text = "Movie not found.",
                    modifier = Modifier.align(Alignment.Center)
                )

            }
        }
    }
}

@Composable
private fun MovieInfo(
    label: String,
    value: String
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(text = label)

        Text(text = value)

    }
}