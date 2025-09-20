package com.example.cdhomes.presentation.listingdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdhomes.domain.model.Listing
import com.example.cdhomes.domain.usecase.GetListingDetailsUseCase
import com.example.cdhomes.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ListingDetailViewModel @Inject constructor(
  private val getListingUseCase: GetListingDetailsUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  val uiState: StateFlow<UiState<Listing>> = savedStateHandle.get<Int>("id")?.let { id ->
    getListingUseCase(id)
      .filterNotNull()
      .map { UiState.Success(it) as UiState<Listing> }
      .catch { emit(UiState.Error(it.message ?: "Unknown error")) }
      .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading)
  } ?: MutableStateFlow(UiState.Error("Invalid ID")).asStateFlow()
}
