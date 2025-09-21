package com.example.cdhomes.presentation.listingdetail

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.cdhomes.R
import com.example.cdhomes.domain.model.Listing

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
              .padding(16.dp)
          ) {
            AsyncImage(
              model = listing.url ?: "",
              contentDescription = listing.city,
              modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp)),
              contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(16.dp))
            Text(
              listing.city,
              style = MaterialTheme.typography.headlineSmall,
              color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
              "${listing.price} €",
              style = MaterialTheme.typography.titleLarge,
              color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(4.dp))
            Text(
              "${listing.rooms ?: "-"} rooms • ${listing.area} m²",
              color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(2.dp))
            Text(
              "Bedrooms: ${listing.bedrooms ?: "-"}",
              color = MaterialTheme.colorScheme.onBackground
            )
            Text("Type: ${listing.propertyType}", color = MaterialTheme.colorScheme.onBackground)
            Text("By: ${listing.professional}", color = MaterialTheme.colorScheme.onBackground)
          }
        }
      }
    }
  }
}
