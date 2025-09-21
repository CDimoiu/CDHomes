package com.example.cdhomes.presentation.listings

import com.example.cdhomes.domain.model.Listing
import com.example.cdhomes.domain.model.ListingFilter

sealed class ListingsUiState {
  object Loading : ListingsUiState()
  data class Success(
    val listings: List<Listing>,
    val filter: ListingFilter = ListingFilter(),
  ) : ListingsUiState()

  data class Error(
    val message: String,
    val cachedListings: List<Listing> = emptyList(),
    val filter: ListingFilter = ListingFilter(),
  ) : ListingsUiState()
}
