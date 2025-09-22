package com.example.cdhomes.presentation.listings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.cdhomes.R
import com.example.cdhomes.domain.model.ListingFilter
import com.example.cdhomes.presentation.theme.Dimens.PaddingMedium
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingsScreen(
  viewModel: ListingsViewModel = hiltViewModel(),
  onItemClick: (Int) -> Unit,
) {
  val uiState by viewModel.uiState.collectAsState()
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

  val genericError = stringResource(R.string.generic_error)
  val appName = stringResource(R.string.app_name)

  LaunchedEffect(uiState) {
    if (uiState is ListingsUiState.Error) {
      val message = (uiState as ListingsUiState.Error).message.takeIf { !it.isNullOrBlank() }
        ?: genericError
      scope.launch { snackbarHostState.showSnackbar(message) }
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = appName) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
          containerColor = MaterialTheme.colorScheme.primary,
          titleContentColor = MaterialTheme.colorScheme.onPrimary,
        )
      )
    },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      val currentFilter = when (uiState) {
        is ListingsUiState.Success -> (uiState as ListingsUiState.Success).filter
        is ListingsUiState.Error -> (uiState as ListingsUiState.Error).filter
        is ListingsUiState.Loading -> ListingFilter()
      }

      FiltersSection(
        filter = currentFilter,
        onFilterChange = { viewModel.updateFilter(it) }
      )

      val listings = when (uiState) {
        is ListingsUiState.Success -> (uiState as ListingsUiState.Success).listings
        is ListingsUiState.Error -> (uiState as ListingsUiState.Error).cachedListings
        is ListingsUiState.Loading -> (uiState as ListingsUiState.Loading).cachedListings
      }

      PullToRefreshBox(
        isRefreshing = uiState is ListingsUiState.Loading,
        onRefresh = { viewModel.refreshListings() },
        modifier = Modifier.fillMaxSize()
      ) {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(PaddingMedium),
          verticalArrangement = Arrangement.spacedBy(PaddingMedium)
        ) {
          if (listings.isEmpty()) {
            item {
              Box(
                modifier = Modifier
                  .fillParentMaxSize(),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = when (uiState) {
                    is ListingsUiState.Error -> stringResource(R.string.listings_load_error)
                    is ListingsUiState.Loading -> stringResource(R.string.listings_loading)
                    else -> stringResource(R.string.listings_unavailable)
                  },
                  color = MaterialTheme.colorScheme.onBackground,
                  textAlign = TextAlign.Center
                )
              }
            }
          } else {
            items(
              items = listings,
              key = { it.id }
            ) { listing ->
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
                backgroundContent = { Box(modifier = Modifier.fillMaxSize()) }
              ) {
                ListingItem(listing = listing) { onItemClick(listing.id) }
              }
            }
          }
        }

        if (uiState is ListingsUiState.Loading) {
          Box(
            Modifier
              .fillMaxSize()
              .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
          ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
          }
        }
      }
    }
  }
}
