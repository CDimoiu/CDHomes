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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@HiltViewModel
class ListingsViewModel @Inject constructor(
  private val getListingsUseCase: GetListingsUseCase,
  private val deleteListingUseCase: DeleteListingUseCase,
) : ViewModel() {

  private val _uiState =
    MutableStateFlow<ListingsUiState>(ListingsUiState.Loading(cachedListings = emptyList()))
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
      getListingsUseCase()
        .map { applyFilter(it) }
        .onStart { _uiState.value = ListingsUiState.Loading(cachedListings = emptyList()) }
        .catch { exception ->
          _uiState.value = ListingsUiState.Error(
            message = exception.message,
            filter = currentFilter,
            cachedListings = emptyList()
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

  fun refreshListings() {
    viewModelScope.launch {
      val currentListings = when (val state = _uiState.value) {
        is ListingsUiState.Success -> state.listings
        is ListingsUiState.Error -> state.cachedListings
        else -> emptyList()
      }

      _uiState.value = ListingsUiState.Loading(cachedListings = currentListings)

      runCatching { getListingsUseCase.refreshRemote() }
        .onFailure { exception ->
          _uiState.value = ListingsUiState.Error(
            message = exception.message,
            filter = currentFilter,
            cachedListings = currentListings
          )
        }
        .onSuccess {
          val allListings = applyFilter(getListingsUseCase().first())
          _uiState.value = ListingsUiState.Success(
            listings = allListings,
            filter = currentFilter
          )
        }
    }
  }

  fun deleteListing(id: Int) {
    viewModelScope.launch {
      deleteListingUseCase(id)
      loadListings()
    }
  }

  private fun applyFilter(listings: List<Listing>) = listings.filter { listing ->
    (currentFilter.minPrice?.let { listing.price >= it } ?: true) &&
      (currentFilter.maxPrice?.let { listing.price <= it } ?: true) &&
      (currentFilter.minRooms?.let { listing.rooms?.let { rooms -> rooms >= it } ?: false }
        ?: true) &&
      (currentFilter.maxRooms?.let { listing.rooms?.let { rooms -> rooms <= it } ?: false }
        ?: true) &&
      (currentFilter.minArea?.let { listing.area >= it } ?: true) &&
      (currentFilter.maxArea?.let { listing.area <= it } ?: true)
  }
}
