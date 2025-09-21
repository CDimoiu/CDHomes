package com.example.cdhomes.presentation.listingdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.cdhomes.R
import com.example.cdhomes.domain.model.Listing
import com.example.cdhomes.presentation.theme.Dimens.ImageHeightLarge
import com.example.cdhomes.presentation.theme.Dimens.PaddingLarge
import com.example.cdhomes.presentation.theme.Dimens.PaddingLarger
import com.example.cdhomes.presentation.theme.Dimens.PaddingSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingDetailScreen(
  viewModel: ListingDetailViewModel = hiltViewModel(),
  onBackClick: () -> Unit,
) {
  val uiState by viewModel.uiState.collectAsState()
  val genericError = stringResource(R.string.generic_error)
  val appName = stringResource(R.string.app_name)

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = appName) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
          containerColor = MaterialTheme.colorScheme.primary,
          titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
          IconButton(onClick = { onBackClick() }) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onPrimary
            )
          }
        }
      )
    }
  ) { innerPadding ->
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      color = MaterialTheme.colorScheme.background
    ) {
      when (uiState) {
        is ListingDetailUiState.Loading -> Box(
          Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }

        is ListingDetailUiState.Error -> Box(
          Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = (uiState as ListingDetailUiState.Error).message.takeIf { !it.isNullOrBlank() }
              ?: genericError,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
          )
        }

        is ListingDetailUiState.Success -> {
          val listing = (uiState as ListingDetailUiState.Success<Listing>).data
          Column(
            modifier = Modifier
              .fillMaxSize()
              .verticalScroll(rememberScrollState())
              .padding(PaddingLarger)
          ) {
            SubcomposeAsyncImage(
              model = ImageRequest.Builder(LocalContext.current)
                .data(listing.url ?: "")
                .crossfade(true)
                .build(),
              contentDescription = listing.city,
              modifier = Modifier
                .fillMaxWidth()
                .height(ImageHeightLarge)
                .clip(RoundedCornerShape(PaddingLarge))
            ) {
              when (painter.state) {
                is AsyncImagePainter.State.Loading,
                is AsyncImagePainter.State.Error,
                  -> {
                  Image(
                    painter = painterResource(R.drawable.icon_home),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                  )
                }

                else -> {
                  SubcomposeAsyncImageContent(
                    contentScale = ContentScale.Crop
                  )
                }
              }
            }

            Spacer(Modifier.height(PaddingLarger))
            Text(
              listing.city,
              style = MaterialTheme.typography.headlineSmall,
              color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(PaddingSmall))
            Text(
              text = stringResource(R.string.listing_detail_price, listing.price.toInt()),
              style = MaterialTheme.typography.titleLarge,
              color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(PaddingLarger))

            Text(
              text = stringResource(R.string.listing_detail_property_ype, listing.propertyType),
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(PaddingSmall))
            Text(
              text = stringResource(R.string.listing_detail_area, listing.area.toInt()),
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(PaddingSmall))
            Text(
              text = stringResource(R.string.listing_detail_rooms, listing.rooms ?: "-"),
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(PaddingSmall))
            Text(
              text = stringResource(R.string.listing_detail_bedrooms, listing.bedrooms ?: "-"),
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(PaddingSmall))
            Text(
              text = stringResource(R.string.listing_detail_professional, listing.professional),
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(PaddingSmall))
          }
        }
      }
    }
  }
}
