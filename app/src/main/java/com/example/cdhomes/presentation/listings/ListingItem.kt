package com.example.cdhomes.presentation.listings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cdhomes.R
import com.example.cdhomes.domain.model.Listing
import com.example.cdhomes.presentation.theme.Dimens.ImageHeightSmall
import com.example.cdhomes.presentation.theme.Dimens.PaddingLarger
import com.example.cdhomes.presentation.theme.Dimens.PaddingMedium
import com.example.cdhomes.presentation.theme.Dimens.PaddingSmall

@Composable
fun ListingItem(listing: Listing, onClick: (Int) -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick(listing.id) }
      .padding(PaddingLarger)
  ) {
    AsyncImage(
      model = ImageRequest.Builder(LocalContext.current)
        .data(listing.url)
        .crossfade(true)
        .build(),
      contentDescription = null,
      placeholder = painterResource(R.drawable.icon_home),
      error = painterResource(R.drawable.icon_home),
      modifier = Modifier
        .size(ImageHeightSmall)
        .clip(RoundedCornerShape(PaddingMedium)),
      contentScale = ContentScale.Crop
    )

    Spacer(modifier = Modifier.width(PaddingLarger))
    Column(modifier = Modifier.fillMaxWidth()) {
      Text(text = listing.propertyType, fontWeight = FontWeight.Bold)
      Spacer(Modifier.height(PaddingSmall))
      Text(text = stringResource(R.string.listing_rooms, listing.rooms ?: "-"))
      Spacer(Modifier.height(PaddingSmall))
      Text(text = stringResource(R.string.listing_area, listing.area.toInt()))
      Spacer(Modifier.height(PaddingSmall))
      Text(
        text = stringResource(R.string.listing_price, listing.price.toInt()),
        color = MaterialTheme.colorScheme.primary
      )
      Spacer(Modifier.height(PaddingSmall))
      Text(text = stringResource(R.string.listing_city, listing.city))
    }
  }
}
