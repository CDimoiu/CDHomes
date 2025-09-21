package com.example.cdhomes.presentation.listings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cdhomes.domain.model.Listing

@Composable
fun ListingItem(listing: Listing, onClick: (Int) -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick(listing.id) }
      .padding(16.dp)
  ) {
    AsyncImage(
      model = listing.url,
      contentDescription = null,
      modifier = Modifier
        .size(100.dp)
        .clip(RoundedCornerShape(8.dp)),
      contentScale = ContentScale.Crop
    )
    Spacer(modifier = Modifier.width(16.dp))
    Column(modifier = Modifier.fillMaxWidth()) {
      Text(text = listing.propertyType, fontWeight = FontWeight.Bold)
      Text(text = "${listing.rooms ?: "-"} rooms • ${listing.area} m²")
      Text(text = "${listing.city} • ${listing.price} €")
    }
  }
}
