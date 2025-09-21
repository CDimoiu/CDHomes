package com.example.cdhomes.presentation.listingdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.cdhomes.domain.model.Listing

@Composable
fun ListingDetailScreen(
  viewModel: ListingDetailViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsState()

  when (state) {
    is ListingDetailUiState.Loading -> Box(
      Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }

    is ListingDetailUiState.Error -> Box(
      Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Text("Error: ${(state as ListingDetailUiState.Error).message}")
    }

    is ListingDetailUiState.Success -> {
      val listing = (state as ListingDetailUiState.Success<Listing>).data
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        AsyncImage(
          model = listing.url ?: "",
          contentDescription = listing.city,
          modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp)),
          contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(16.dp))
        Text(listing.city, style = MaterialTheme.typography.headlineSmall)
        Text("${listing.price} €", style = MaterialTheme.typography.titleLarge)
        Text("${listing.rooms ?: "-"} rooms • ${listing.area} m²")
        Text("Bedrooms: ${listing.bedrooms ?: "-"}")
        Text("Type: ${listing.propertyType}")
        Text("By: ${listing.professional}")
      }
    }
  }
}
