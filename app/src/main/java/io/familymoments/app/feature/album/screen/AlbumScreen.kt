package io.familymoments.app.feature.album.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.album.viewmodel.AlbumViewModel

@Composable
fun AlbumScreen(
    modifier: Modifier,
    viewModel: AlbumViewModel
) {
    val albumUiState = viewModel.albumUiState.collectAsStateWithLifecycle()
    val album = albumUiState.value.album
    val lazyGridState = rememberLazyGridState()
    val isScrolledToLast by remember(lazyGridState.canScrollForward) {
        if (album.isEmpty()) {
            mutableStateOf(false)
        } else {
            mutableStateOf(!lazyGridState.canScrollForward)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getAlbum()
    }
    LaunchedEffect(isScrolledToLast) {
        if (isScrolledToLast) {
            viewModel.loadMoreAlbum()
        }
    }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 100.dp),
        state = lazyGridState,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
    ) {
        item(span = { GridItemSpan(this.maxLineSpan) }) {
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp),
                    text = "우리 가족 앨범",
                    style = AppTypography.B1_16,
                    color = AppColors.black1,
                    textAlign = TextAlign.Center
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(AppColors.deepPurple1)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 21.dp, top = 15.dp, end = 21.dp, bottom = 13.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(5) {
                        Image(
                            modifier = Modifier.size(13.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_album_dot),
                            contentDescription = null
                        )
                    }
                }
            }
        }
        items(album.size) { index ->
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(5.dp)),
                model = album[index].img1,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlbumScreenPreview() {
    LazyVerticalGrid(
        modifier = Modifier,
        columns = GridCells.Adaptive(minSize = 100.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        item(span = { GridItemSpan(this.maxLineSpan) }) {
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp),
                    text = "우리 가족 앨범",
                    style = AppTypography.B1_16,
                    color = AppColors.black1,
                    textAlign = TextAlign.Center
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(AppColors.deepPurple1)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 21.dp, top = 15.dp, end = 21.dp, bottom = 13.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(5) {
                        Image(
                            modifier = Modifier.size(13.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_album_dot),
                            contentDescription = null
                        )
                    }
                }
            }
        }
        items(20) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(5.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_sample_trees),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
