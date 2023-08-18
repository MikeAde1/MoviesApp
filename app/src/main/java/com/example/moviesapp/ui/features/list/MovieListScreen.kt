@file:OptIn(ExperimentalMaterialApi::class)

package com.example.moviesapp.ui.features.list

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviesapp.R
import com.example.moviesapp.ui.features.MovieMetaData
import com.example.moviesapp.ui.features.common.ErrorText
import com.example.moviesapp.ui.model.MovieItemResult
import com.example.moviesapp.util.extensions.capitalize

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MovieListScreen(
    movieListViewModel: MovieListViewModel,
    onMovieClicked: (MovieMetaData) -> Unit
) {
    var showSheet by rememberSaveable { mutableStateOf(false) }
    val movieListUiState by movieListViewModel.moviesUiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Surface(shadowElevation = 3.dp) {
                TopAppBar(
                    title = { Text(text = stringResource(R.string.movies_app)) },
                    actions = {
                        if (movieListUiState.movieList.isEmpty().not()) {
                            IconButton(onClick = {
                                showSheet = true
                            }) {
                                Icon(
                                    modifier = Modifier
                                        .wrapContentSize(Alignment.CenterEnd),
                                    painter = painterResource(id = R.drawable.baseline_filter_alt_24),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { contentPadding ->
        val pullRefreshState = rememberPullRefreshState(
            movieListUiState.isLoading,
            { movieListViewModel.refresh() }
        )

        Box(
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .pullRefresh(pullRefreshState)
        ) {
            MovieListScreenByState(movieListUiState, onMovieClicked)

            PullRefreshIndicator(
                movieListUiState.isLoading,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }

    if (showSheet) {
        FilterBottomSheet(
            { movieListViewModel.getFavouriteMovies() },
            { movieListViewModel.getAllMovies() }
        ) { showSheet = false }
    }
}

@Composable
private fun MovieListScreenByState(
    movieListUiState: MovieListUiState?,
    onMovieClicked: (MovieMetaData) -> Unit
) {
    when {
        movieListUiState?.movieList?.isNotEmpty() == true ->
            MovieList(movieListUiState.movieList, onMovieClicked)

        movieListUiState?.errorMessage.isNullOrBlank().not() ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    ErrorText(message = movieListUiState?.errorMessage.orEmpty())
                }
            }
    }
}

@Composable
private fun MovieList(
    movieList: List<MovieItemResult>,
    onMovieClicked: (MovieMetaData) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        items(movieList) { movie ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp)
                    .clickable {
                        onMovieClicked(MovieMetaData(movie.id, movie.title))
                    },
                shadowElevation = 8.dp
            ) {
                Column {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(movie.finalImageUrl)
                            .error(R.drawable.ic_img_placeholder)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                    Text(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 5.dp),
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = FontFamily(Font(R.font.raleway_semibold))
                                )
                            ) {
                                append("Movie Title: ")
                            }
                            append(movie.title.capitalize())
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onFilterByFavoriteClicked: () -> Unit,
    ongGetAllMoviesClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(modifier = Modifier.padding(bottom = 20.dp)) {
            BottomSheetItem(onActionClicked = {
                onFilterByFavoriteClicked()
                onDismiss()
            }, actionLabel = R.string.filter_by_favourite)

            HorizontalDivider()

            BottomSheetItem(onActionClicked = {
                ongGetAllMoviesClicked()
                onDismiss()
            }, actionLabel = R.string.show_all_movies)
        }
    }
}
@Composable
private fun BottomSheetItem(onActionClicked: () -> Unit, @StringRes actionLabel: Int) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onActionClicked()
        }
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            text = stringResource(id = actionLabel),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun MoviesListPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        MovieListScreenByState(
            MovieListUiState(
                isLoading = true,
                errorMessage = "",
                movieList = listOf(MovieItemResult(title = "title", originalTitle = "Original"))
            )
        ) { }
    }
}
