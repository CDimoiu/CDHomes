package com.example.cdhomes.presentation.listingdetail

sealed class ListingDetailUiState<out T> {
  object Loading : ListingDetailUiState<Nothing>()
  data class Success<T>(val data: T) : ListingDetailUiState<T>()
  data class Error(val message: String) : ListingDetailUiState<Nothing>()
}