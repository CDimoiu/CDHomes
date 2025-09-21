package com.example.cdhomes.presentation.listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdhomes.domain.model.Listing
import com.example.cdhomes.domain.model.ListingFilter
import com.example.cdhomes.domain.usecase.DeleteListingUseCase
import com.example.cdhomes.domain.usecase.GetListingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@HiltViewModel
class ListingsViewModel @Inject constructor(
  private val getListingsUseCase: GetListingsUseCase,
  private val deleteListingUseCase: DeleteListingUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow<ListingsUiState>(ListingsUiState.Loading)
  val uiState: StateFlow<ListingsUiState> = _uiState.asStateFlow()

  private var currentFilter = ListingFilter()

  init {
    loadListings()
  }

  fun updateFilter(newFilter: ListingFilter) {
    currentFilter = newFilter
    loadListings()
  }

  private fun loadListings() {
    viewModelScope.launch {
      getListingsUseCase() // do NOT pass filter to repo
        .map { applyFilter(it) } // filter in ViewModel
        .onStart { _uiState.value = ListingsUiState.Loading }
        .catch { e ->
          _uiState.value = ListingsUiState.Error(
            message = e.message ?: "Oops, something went wrong. Please try again later.",
            filter = currentFilter
          )
        }
        .collect { filteredListings ->
          _uiState.value = ListingsUiState.Success(
            listings = filteredListings,
            filter = currentFilter
          )
        }
    }
  }

  private fun applyFilter(listings: List<Listing>) = listings.filter { listing ->
    (currentFilter.minPrice?.let { listing.price >= it } ?: true) &&
      (currentFilter.maxPrice?.let { listing.price <= it } ?: true) &&
      (currentFilter.minRooms?.let { listing.rooms?.let { r -> r >= it } ?: false } ?: true) &&
      (currentFilter.maxRooms?.let { listing.rooms?.let { r -> r <= it } ?: false } ?: true) &&
      (currentFilter.minArea?.let { listing.area >= it } ?: true) &&
      (currentFilter.maxArea?.let { listing.area <= it } ?: true) &&
      (currentFilter.city?.let { listing.city.contains(it, ignoreCase = true) } ?: true)
  }


  fun refreshListings() {
    viewModelScope.launch {
      _uiState.value = ListingsUiState.Loading

      runCatching { getListingsUseCase.refreshRemote() }
        .onFailure { e ->
          _uiState.value = ListingsUiState.Error(
            message = e.message ?: "Refresh failed",
            filter = currentFilter
          )
        }
        .onSuccess {
          loadListings()
        }
    }
  }

  fun deleteListing(id: Int) {
    viewModelScope.launch {
      deleteListingUseCase(id)
      loadListings()
    }
  }
}
