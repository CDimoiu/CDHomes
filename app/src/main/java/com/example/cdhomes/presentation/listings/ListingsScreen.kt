package com.example.cdhomes.presentation.listings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.cdhomes.domain.model.ListingFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingsScreen(
  viewModel: ListingsViewModel = hiltViewModel(),
  onItemClick: (Int) -> Unit,
) {
  val uiState by viewModel.uiState.collectAsState()

  Column(modifier = Modifier.fillMaxSize()) {
    val currentFilter = when (uiState) {
      is ListingsUiState.Success -> (uiState as ListingsUiState.Success).filter
      is ListingsUiState.Error -> (uiState as ListingsUiState.Error).filter
      else -> ListingFilter()
    }

    FiltersSection(
      filter = currentFilter,
      onFilterChange = { viewModel.updateFilter(it) }
    )

    PullToRefreshBox(
      isRefreshing = uiState is ListingsUiState.Loading,
      onRefresh = { viewModel.refreshListings() },
      modifier = Modifier.fillMaxSize()
    ) {
      when (uiState) {
        is ListingsUiState.Loading -> {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
          }
        }

        is ListingsUiState.Error -> {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = (uiState as ListingsUiState.Error).message)
          }
        }

        is ListingsUiState.Success -> {
          val listings = (uiState as ListingsUiState.Success).listings
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(items = listings, key = { it.id }) { listing ->
              val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                  if (value == SwipeToDismissBoxValue.EndToStart ||
                    value == SwipeToDismissBoxValue.StartToEnd
                  ) {
                    viewModel.deleteListing(listing.id)
                    true
                  } else false
                }
              )

              SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                  Box(modifier = Modifier.fillMaxSize())
                }
              ) {
                ListingItem(listing = listing) { onItemClick(listing.id) }
              }
            }
          }
        }
      }
    }
  }
}
