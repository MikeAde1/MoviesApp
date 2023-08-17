package com.example.moviesapp.ui.features.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviesapp.R
import com.example.moviesapp.ui.features.MovieMetaData
import com.example.moviesapp.ui.features.common.CommonCircularProgressIndicator
import com.example.moviesapp.ui.features.common.ErrorText
import com.example.moviesapp.ui.features.common.textWithLabel
import com.example.moviesapp.ui.model.MovieDetailResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    onBackPressed: () -> Boolean,
    viewModel: MovieDetailsViewModel,
    movieMetaData: MovieMetaData
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = movieMetaData.movieTitle.orEmpty(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = TextUnit(18f, TextUnitType.Sp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { contentPadding ->
        LaunchedEffect(key1 = viewModel) {
            movieMetaData.movieId?.let { viewModel.getMovieDetails(movieMetaData.movieId) }
        }
        val movieDetailUiState by viewModel.movieDetailsUiState.observeAsState()

        when {
            movieDetailUiState?.isLoading == true -> CommonCircularProgressIndicator()

            movieDetailUiState?.movieDetail != null ->
                movieDetailUiState?.movieDetail?.let { MovieDetailCover(it, contentPadding) }

            movieDetailUiState?.errorMessage.isNullOrBlank().not() ->
                ErrorText(message = movieDetailUiState?.errorMessage.orEmpty())
        }
    }
}

@Composable
fun MovieDetailCover(movieDetail: MovieDetailResult, contentPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp), contentPadding = contentPadding
    ) {
        item {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movieDetail.finalImageUrl)
                    .error(R.drawable.ic_img_placeholder)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
            )
        }

        item {
            Column {
                Text(
                    modifier = Modifier.padding(vertical = 5.dp),
                    text = stringResource(R.string.overview),
                    style = MaterialTheme.typography.titleLarge,
                    textDecoration = TextDecoration.Underline
                )
                Text(text = movieDetail.overview)
            }
        }

        item {
            Text(
                modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
                text = textWithLabel("Release Date: ", movieDetail.releaseDate)
            )
        }

        item {
            Text(
                modifier = Modifier.padding(vertical = 5.dp),
                text = textWithLabel("Status: ", movieDetail.status)
            )
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                text = textWithLabel("Release Date: ", movieDetail.genres.joinToString { it.name })
            )
        }
    }
}