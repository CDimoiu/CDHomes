package com.example.cdhomes.presentation.listings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.cdhomes.domain.model.ListingFilter
import com.example.cdhomes.presentation.theme.Dimens.PaddingMedium
import com.example.cdhomes.presentation.theme.Dimens.PaddingSmall

@Composable
fun FiltersSection(
  filter: ListingFilter,
  onFilterChange: (ListingFilter) -> Unit,
) {
  var expanded by remember { mutableStateOf(false) }

  var maxPriceSlider by remember { mutableDoubleStateOf(filter.maxPrice ?: 10000000.0) }
  var minRoomsSlider by remember { mutableIntStateOf(filter.minRooms ?: 0) }
  var minAreaSlider by remember { mutableDoubleStateOf(filter.minArea ?: 0.0) }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(PaddingMedium)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth()
        .clickable { expanded = !expanded }
        .padding(vertical = PaddingSmall)
    ) {
      Text("Filters", style = MaterialTheme.typography.titleMedium)
      Spacer(modifier = Modifier.weight(1f))
      Icon(
        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
        contentDescription = null,
      )
    }

    if (expanded) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PaddingMedium)
      ) {
        val maxPriceLabel = if (maxPriceSlider < 1_000_000) {
          "${(maxPriceSlider / 1000).toInt()}k"
        } else {
          "${(maxPriceSlider / 1_000_000.0).format(1)}M"
        }

        Text("Max Price: $maxPriceLabel")

        Slider(
          value = maxPriceSlider.toFloat(),
          onValueChange = { maxPriceSlider = it.toDouble() },
          onValueChangeFinished = {
            onFilterChange(filter.copy(maxPrice = maxPriceSlider))
          },
          valueRange = 0f..10_000_000f,
          steps = 39
        )


        Text("Min Rooms: $minRoomsSlider")
        Slider(
          value = minRoomsSlider.toFloat(),
          onValueChange = { minRoomsSlider = it.toInt() },
          onValueChangeFinished = { onFilterChange(filter.copy(minRooms = minRoomsSlider)) },
          valueRange = 0f..10f,
          steps = 10
        )

        Text("Min Area: ${minAreaSlider.toInt()} m²")
        Slider(
          value = minAreaSlider.toFloat(),
          onValueChange = { minAreaSlider = it.toDouble() },
          onValueChangeFinished = { onFilterChange(filter.copy(minArea = minAreaSlider)) },
          valueRange = 0f..1000f,
          steps = 19
        )
      }
    }
  }
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)
