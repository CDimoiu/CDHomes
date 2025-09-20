package com.example.cdhomes.presentation.listings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.cdhomes.presentation.common.UiState

@Composable
fun ListingsScreen(
  onItemClick: (Int) -> Unit,
  viewModel: ListingsViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsState()

  when (uiState) {
    is UiState.Loading -> {
      Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
      }
    }

    is UiState.Error -> {
      Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Error: ${(uiState as UiState.Error).message}")
      }
    }

    is UiState.Success -> {
      val listings = (uiState as UiState.Success<List<Listing>>).data
      LazyColumn {
        items(listings) { listing ->
          ListingItem(listing = listing, onClick = { onItemClick(listing.id) })
        }
      }
    }
  }
}

@Composable
fun ListingItem(listing: Listing, onClick: () -> Unit) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp)
      .clickable { onClick() },
    elevation = CardDefaults.cardElevation(4.dp)
  ) {
    Row(modifier = Modifier.padding(8.dp)) {
      AsyncImage(
        model = listing.url ?: "",
        contentDescription = listing.city,
        modifier = Modifier
          .size(120.dp)
          .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
      )

      Spacer(modifier = Modifier.width(8.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(listing.city)
        Text("${listing.price} €")
        Text("${listing.rooms ?: "-"} rooms • ${listing.area} m²")
      }
    }
  }
}
