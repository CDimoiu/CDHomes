package com.example.cdhomes.presentation.listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
      getListingsUseCase(currentFilter)
        .onStart { _uiState.value = ListingsUiState.Loading }
        .catch { e ->
          _uiState.value = ListingsUiState.Error(
            message = e.message ?: "Oops, something went wrong. Please try again later.",
            filter = currentFilter
          )
        }
        .collect { listings ->
          _uiState.value = ListingsUiState.Success(
            listings = listings,
            filter = currentFilter
          )
        }
    }
  }

  fun refreshListings() {
    viewModelScope.launch {
      _uiState.value = ListingsUiState.Loading

      val result = runCatching { getListingsUseCase.refreshRemote() }

      result.onFailure { e ->
        _uiState.value = ListingsUiState.Error(
          message = e.message ?: "Refresh failed",
          filter = currentFilter
        )
      }.onSuccess {
        val listings = getListingsUseCase(currentFilter).first()
        _uiState.value = ListingsUiState.Success(listings = listings, filter = currentFilter)
      }
    }
  }

  fun deleteListing(id: Int) {
    viewModelScope.launch {
      deleteListingUseCase(id)
      val listings = getListingsUseCase(currentFilter).first()
      _uiState.value = ListingsUiState.Success(listings, currentFilter)
    }
  }
}
