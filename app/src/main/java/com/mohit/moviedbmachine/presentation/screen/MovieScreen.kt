package com.mohit.moviedbmachine.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mohit.moviedbmachine.navigation.Routes
import com.mohit.moviedbmachine.presentation.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun MovieScreen(
    navController: NavController
) {

    val viewModel: MovieViewModel = hiltViewModel()

    val movies by viewModel.movie.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isLoadingMore by viewModel.isLoadingMore.collectAsStateWithLifecycle()
    val error by viewModel.isError.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    val keyboardController = LocalSoftwareKeyboardController.current

    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    val latestMovies by rememberUpdatedState(movies)
    val latestLoadingMore by rememberUpdatedState(isLoadingMore)

    LaunchedEffect(listState) {

        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo
                .lastOrNull()
                ?.index
        }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { index ->

                if (
                    latestMovies.isNotEmpty() &&
                    !latestLoadingMore &&
                    index >= latestMovies.lastIndex - 5
                ) {
                    viewModel.loadNextPage()
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Search Movie")
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {

                    keyboardController?.hide()

                    if (searchQuery.isBlank()) {
                        viewModel.clearSearch()
                    } else {
                        viewModel.searchMovie(searchQuery)
                    }
                }
            )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {

            when {

                isLoading && movies.isEmpty() -> {

                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                error != null && movies.isEmpty() -> {

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

                movies.isEmpty() -> {

                    Text(
                        text = "No movies found.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {

                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {

                        items(
                            items = movies,
                            key = { it.id ?: it.hashCode() }
                        ) { movie ->

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        movie.id?.let {

                                            if (
                                                navController.currentDestination?.route ==
                                                Routes.MovieScreen.routes
                                            ) {

                                                navController.navigate(
                                                    Routes.MovieDetailScreen.routes.replace(
                                                        "{movieId}",
                                                        it.toString()
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    .padding(vertical = 12.dp)
                            ) {

                                Text(
                                    text = movie.title.orEmpty()
                                )

                                Text(
                                    text = movie.releaseDate.orEmpty()
                                )

                                Text(
                                    text = "⭐ ${movie.rating}"
                                )
                            }
                        }

                        if (isLoadingMore) {

                            item {

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {

                                    CircularProgressIndicator()

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}